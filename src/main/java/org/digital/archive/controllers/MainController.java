package org.digital.archive.controllers;

import org.digital.archive.entities.*;
import org.digital.archive.services.ArchiveService;
import org.digital.archive.services.ProfessorService;
import org.digital.archive.services.StudentService;
import org.digital.archive.services.UserService;
import org.digital.archive.utils.ArchiveHelper;
import org.digital.archive.utils.SecurityConstants;
import org.digital.archive.utils.SecurityHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/")
public class MainController {

    @Autowired
    private SecurityHelper securityHelper;

    @Autowired
    private ArchiveHelper archiveHelper;

    @Autowired
    private StudentService studentService;

    @Autowired
    private ProfessorService professorService;

    @Autowired
    private UserService userService;

    @Autowired
    private ArchiveService archiveService;

    @Autowired
    private Map<RoleType, String> rolesMap;

    @Value("${file.upload-dir}")
    private String path;

    private final int size = 10;

    /*
     * Home page controller (GET REQUESTS)
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView home(@RequestParam(name = "professor", required = false) Long professorId, @RequestParam(name = "page", defaultValue = "0") int page, @RequestParam(value = "archive-search", required = false) String search) {
        ModelAndView modelAndView = new ModelAndView("index");

        // Put professor in the model
        if (professorId != null) {
            modelAndView.addObject("professor", this.professorService.getProfessor(professorId));
        }

        if (search != null) {
            if (professorId == null) {
                modelAndView.addObject("archives", this.archiveService.getArchives(search, page, size));
            } else {
                modelAndView.addObject("archives", this.archiveService.getArchives(professorId, search, page, size));
            }
        } else {
            if (professorId == null) {
                modelAndView.addObject("archives", this.archiveService.getArchives(page, size));
            } else {
                modelAndView.addObject("archives", this.archiveService.getArchives(professorId, page, size));
            }
        }

        modelAndView.addObject("search", search);
        return modelAndView;
    }

    /*
     * Login page controller (GET REQUESTS)
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loginGet(RedirectAttributes redirectAttributes) {
        if (this.securityHelper.getConnectedUser() == null) {
            return "login";
        }

        // Redirect the user if already connected
        redirectAttributes.addFlashAttribute("error", "Vous êtes déja connecté");
        return "redirect:/";
    }

    /*
     * Profile page controller (GET REQUESTS)
     */
    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public String profileGet(@RequestParam(name = "page", defaultValue = "0") int page, Model model, RedirectAttributes redirectAttributes) {
        User user = this.securityHelper.getConnectedUser();

        // Retrieve the specialized classes from the parent one
        Professor professor = this.professorService.getProfessor(user.getId());
        Student student = this.studentService.getStudent(user.getId());


        model.addAttribute("user", user);
        model.addAttribute("userModal", user);
        model.addAttribute("professor", professor);
        model.addAttribute("student", student);
        model.addAttribute("archivesCounter", this.archiveService.getArchives().stream().filter(archive -> archive.getPublisher().getId() == user.getId()).count());
        model.addAttribute("archives", this.archiveService.getArchives(user.getId(), page, this.size));
        model.addAttribute("roles", this.rolesMap);
        return "profile";
    }

    /*
     * Profile page controller (POST REQUESTS)
     */
    @RequestMapping(value = "/profile", method = RequestMethod.POST)
    public String postProfile(@Valid @ModelAttribute("userModal") User user, BindingResult bindingResult, @RequestParam(name = "page", defaultValue = "0") int page, Model model, RedirectAttributes redirectAttributes) {

        // Adding archives to the model
        model.addAttribute("archives", this.archiveService.getArchives(page, this.size));

        // Retrieve the specialized classes from the parent one
        Professor professor = this.professorService.getProfessor(user.getId());
        Student student = this.studentService.getStudent(user.getId());

        // Retrieve original user saved in the database
        User originalUser = this.userService.getUser(user.getId());

        // Assign necessary old values for user modal to prevent data clean
        user.setPassword(originalUser.getPassword());
        user.setPicture(originalUser.getPicture());
        user.setRoles(originalUser.getRoles());

        // Check if an address email is already used
        User tempUserByUsername = this.userService.getUserByUsername(user.getUsername());
        User tempUserByEmail = this.userService.getUser(user.getEmail());

        // Check if a user already has the same email or username
        // Check if the existed used tried to change his email address or username
        String username = user.getUsername();
        Long id = user.getId();
        if (!user.getUsername().equalsIgnoreCase(originalUser.getUsername())
                && tempUserByUsername != null && tempUserByUsername.getUsername().equalsIgnoreCase(username) && tempUserByUsername.getId() != id) {
            ObjectError error = new ObjectError("username", "Nom d'utilisateur est déja utilisé");
            bindingResult.addError(error);
            bindingResult.rejectValue("username", "username", "Nom d'utilisateur est déja utilisé");
            System.out.println("Invalid username");
        }
        if (tempUserByEmail != null && !user.getEmail().equalsIgnoreCase(originalUser.getEmail()) && tempUserByEmail.getId() != user.getId() && tempUserByEmail.getEmail().equalsIgnoreCase(user.getEmail())) {
            ObjectError error = new ObjectError("email", "Adresse email est déja utilisée");
            bindingResult.addError(error);
            bindingResult.rejectValue("email", "email", "Adresse email est déja utilisée");
        }

        // Check if there is any error
        // Ignore the following fields errors => picture, password and roles
        if (bindingResult.hasErrors()) {
            model.addAttribute("professor", professor);
            model.addAttribute("student", student);
            model.addAttribute("user", originalUser);
            model.addAttribute("userModal", user);
            model.addAttribute("roles", this.rolesMap);
            model.addAttribute("profileTab", true);
            model.addAttribute("openUserModal", true);
            return "profile";
        }

        // Increment date by one day
        Calendar cal = Calendar.getInstance();
        cal.setTime(user.getBirthDate());
        cal.add(Calendar.DATE, 1); //minus number would decrement the days
        user.setBirthDate(cal.getTime());

        // Save the user using the student or professor object
        if (student != null) {
            student.setLastName(user.getLastName());
            student.setFirstName(user.getFirstName());
            student.setUsername(user.getUsername());
            student.setEmail(user.getEmail());
            student.setBirthDate(user.getBirthDate());
            student.setAbout(user.getAbout());
            student = this.studentService.saveStudent(student);
            user = student;
        } else {
            professor.setLastName(user.getLastName());
            professor.setFirstName(user.getFirstName());
            professor.setUsername(user.getUsername());
            professor.setEmail(user.getEmail());
            professor.setBirthDate(user.getBirthDate());
            professor.setAbout(user.getAbout());
            professor = this.professorService.saveProfessor(professor);
            user = professor;
        }

        if (user != null) {
            redirectAttributes.addFlashAttribute("success", "Les informations de votre profil sont changées avec succés!");
        } else {
            redirectAttributes.addFlashAttribute("error", "Une erreur est survenue, veuillez ressayer!");
        }
        return "redirect:/profile?tab=profile";
    }

    /*
     * @Access denied page
     */
    @RequestMapping(value = "/access-denied")
    public String accessDenied() {
        return "access-denied";
    }

    /*
     * @Profile picture update controller (POST REQUESTS)
     */
    @PostMapping("/profile/update-picture")
    public String updateProfilepicture(@RequestParam("picture") MultipartFile file, Model model, RedirectAttributes redirectAttributes) {
        try {

            // Check picture file validity before processing
            if (file.isEmpty() || !SecurityConstants.imageContentTypes.contains(file.getContentType())) {
                redirectAttributes.addFlashAttribute("error", "L'image sélectionnée n'est pas valide!");
                return "redirect:/profile";
            }

            // Retrieve connected user
            User user = this.securityHelper.getConnectedUser();

            // Change the image name based on the unique user id
            user.setPicture(user.getId() + "." + this.archiveHelper.getExtensionByApacheCommonLib(file.getOriginalFilename()));

            // Save the user on the database
            user = this.userService.saveUser(user);

            // Upload user image or update it if exists
            byte[] bytes = file.getBytes();
            Path path = Paths.get(SecurityConstants.usersPicturesUploadDirectory + user.getPicture());
            System.out.println(Files.exists(path));
            Files.write(path, bytes);

            // Redirect the user to his profile with a success message
            redirectAttributes.addFlashAttribute("success", "L'image de votre profil à été changée avec succés");
            return "redirect:/profile";

        } catch (Exception ex) {
            ex.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Une erreure est survenue, veuillez ressayer!");
            return "redirect:/profile";
        }
    }

    /*
     * @Archive controller (GET REQUESTS)
     * Page protected only for professors, administrators and moderators
     */
    @PreAuthorize(value = "hasRole('ROLE_PROFESSOR')")
    @RequestMapping(value = "/archives/save", method = RequestMethod.GET)
    public String profileGet(Model model) {

        // Retrieve connected
        User user = this.securityHelper.getConnectedUser();

        // Retrieve professors and students
        Collection<Professor> professors = this.professorService.getProfessors();
        Collection<Student> students = this.studentService.getStudents();

        model.addAttribute("user", user);
        model.addAttribute("professors", professors);
        model.addAttribute("students", students);
        Archive archive = new Archive();
        model.addAttribute("archive", archive);

        return "archive-form";

    }

    /*
     * @Archive controller (POST REQUESTS)
     * Page protected only for professors, administrators and moderators
     */
    @PreAuthorize(value = "hasRole('ROLE_PROFESSOR')")
    @RequestMapping(value = "/archives/save", method = RequestMethod.POST)
    public String profilePost(@RequestParam(value = "id", required = false) Long id, @RequestParam(value = "title", defaultValue = "empty") String title, @RequestParam(value = "content", defaultValue = "empty") String content,
                              @RequestParam(value = "professors", defaultValue = "empty") String[] professors, @RequestParam(value = "students", defaultValue = "empty") String[] students,
                              @RequestParam(value = "image", defaultValue = "empty") MultipartFile image, @RequestParam(value = "file", defaultValue = "empty") MultipartFile file,
                              @RequestParam(value = "confirm-image-edit", required = false) String confirmImageEdit,
                              @RequestParam(value = "confirm-file-edit", required = false) String confirmFileEdit, Model model,
                              RedirectAttributes redirectAttributes) {

        // Retrieve connected user
        User user = this.securityHelper.getConnectedUser();

        Archive archive = null;

        if (id == null) {
            // Create the archive
            archive = new Archive();
        } else {
            archive = this.archiveService.getArchive(id);
            if (archive == null) {
                redirectAttributes.addFlashAttribute("error", "Aucun archive n'a été trouvé!");
                return "redirect:/";
            }
        }

        try {

            if (!Arrays.asList(title, content, professors, file, image, confirmFileEdit, confirmImageEdit, students).contains("empty")) {

                // Retrieve the professor
                Professor professor = this.professorService.getProfessor(user.getId());

                // Create a new archive based on the sent ddata from user
                archive.setPublisher(professor);
                archive.setTitle(title);
                archive.setContent(content);
                archive.setPublishDate(new Date());
                archive.setProfessors(null);
                archive.setStudents(null);

                // Save archive to assign a new id
                if (archive.getId() == null) {
                    archive.setImage("temp-image." + this.archiveHelper.getExtensionByApacheCommonLib(image.getOriginalFilename()));
                    archive.setFile("temp-file." + this.archiveHelper.getExtensionByApacheCommonLib(file.getOriginalFilename()));
                    archive.setViews(1L);
                }

                // Set archive's professors
                for (String professorId : professors) {
                    archive.addProfessor(this.professorService.getProfessor(Long.parseLong(professorId)));
                }

                // Set archive's students
                for (String studentId : students) {
                    archive.addStudent(this.studentService.getStudent(Long.parseLong(studentId)));
                }

                System.out.println(archive.getId() == null || (confirmImageEdit != null && !image.isEmpty()));

                // Check image and file validity before proceeding
                if (archive.getId() == null && image.isEmpty()) {
                    throw new Exception();
                } else if (archive.getId() == null && file.isEmpty()) {
                    throw new Exception();
                }

                if (archive.getId() == null || (confirmImageEdit != null && !image.isEmpty())) {
                    // Upload archive image or update it if exists
                    archive.setImage(new Date().getTime() + "-image." + this.archiveHelper.getExtensionByApacheCommonLib(image.getOriginalFilename()));
                    byte[] bytes = image.getBytes();
                    Path path = Paths.get(SecurityConstants.archivesImagesUploadDirectory + archive.getImage());
                    Files.write(path, bytes);
                }

                if (archive.getId() == null || (confirmFileEdit != null && !file.isEmpty())) {
                    // Upload archive image or update it if exists
                    archive.setFile(new Date().getTime() + "-file." + this.archiveHelper.getExtensionByApacheCommonLib(file.getOriginalFilename()));
                    byte[] bytes = file.getBytes();
                    Path path = Paths.get(SecurityConstants.archivesFilesUploadDirectory + archive.getFile());
                    Files.write(path, bytes);
                }


                // Resave archive
                this.archiveService.saveArchive(archive);

                // Redirect user with successfull message
                redirectAttributes.addFlashAttribute("success", "L'archive est enregistré avec succés");
                return "redirect:/archives/" + archive.getId();
            } else {
                throw new Exception();
            }
        } catch (Exception exception) {


            System.out.println(exception.getMessage());

            // Retrieve professors and students
            Collection<Professor> professorCollection = this.professorService.getProfessors();
            Collection<Student> studentCollection = this.studentService.getStudents();

            model.addAttribute("user", user);
            model.addAttribute("professors", professorCollection);
            model.addAttribute("students", studentCollection);
            model.addAttribute("archive", archive);

            Collection<String> profes = new ArrayList<>();
            Collection<String> stds = new ArrayList<>();

            for (String professorId : professors) {
                profes.add(professorId);
            }

            for (String studentId : students) {
                stds.add(studentId);
            }

            model.addAttribute("selectedProfessors", profes);
            model.addAttribute("selectedStudents", stds);

            model.addAttribute("error", "Une erreur est survenue, veuillez verifier les champs puis ressayer!");

            return "archive-form";
        }
    }

    /*
     * Single Archive
     */
    @RequestMapping(value = "/archives/{id}", method = RequestMethod.GET)
    public String archiveGet(@PathVariable(name = "id", required = false) Long id, Model model, RedirectAttributes redirectAttributes) {
        if (id != null) {

            // Retrieve the archive from database
            Archive archive = this.archiveService.getArchive(id);

            if (archive != null) {
                // Increment views counter
                archive.setViews(archive.getViews() != null ? archive.getViews() + 1 : 1);
                archive = this.archiveService.saveArchive(archive);

                String publishDate = SimpleDateFormat.getDateInstance(
                        SimpleDateFormat.LONG, Locale.FRANCE).format(archive.getPublishDate());
                model.addAttribute("archive", archive);
                model.addAttribute("publishDate", publishDate);
                return "archive";
            } else {
                redirectAttributes.addFlashAttribute("error", "Aucun archive n'a été trouvé!");
                return "redirect:/";
            }

        } else {
            return "redirect:/";
        }
    }

    /*
     * @Edit Archive controller (GET REQUESTS)
     */
    @PreAuthorize(value = "hasRole('ROLE_PROFESSOR')")
    @RequestMapping(value = "/archives/{id}/edit", method = RequestMethod.GET)
    public String profileGet(@PathVariable(name = "id", required = false) Long id, Model model, RedirectAttributes redirectAttributes) {

        if (id != null) {
            // Retrieve the archive from database
            Archive archive = this.archiveService.getArchive(id);
            if (archive != null) {

                // Retrieve connected
                User user = this.securityHelper.getConnectedUser();

                // Retrieve professors and students
                Collection<Professor> professors = this.professorService.getProfessors();
                Collection<Student> students = this.studentService.getStudents();

                model.addAttribute("user", user);
                model.addAttribute("professors", professors);
                model.addAttribute("students", students);
                model.addAttribute("archive", archive);


                // Retrieve selected professors and students
                Collection<String> profes = new ArrayList<>();
                Collection<String> stds = new ArrayList<>();

                for (Professor professor : archive.getProfessors()) {
                    profes.add(professor.getId().toString());
                }

                for (Student student : archive.getStudents()) {
                    stds.add(student.getId().toString());
                }


                model.addAttribute("selectedProfessors", profes);
                model.addAttribute("selectedStudents", stds);


                // Reformat date
                String publishDate = SimpleDateFormat.getDateInstance(
                        SimpleDateFormat.LONG, Locale.FRANCE).format(archive.getPublishDate());
                model.addAttribute("archive", archive);
                model.addAttribute("publishDate", publishDate);
                return "archive-form";
            }
        }
        redirectAttributes.addFlashAttribute("error", "Aucun archive n'a été trouvé!");
        return "redirect:/";

    }

    /*
     * Single Archive
     */
    @RequestMapping(value = "/archives/delete", method = RequestMethod.POST)
    public String deleteArchivePost(@RequestParam(name = "id", required = false) Long id, Model model, RedirectAttributes redirectAttributes) {
        System.out.println(id);
        if (id != null) {
            System.out.println(this.archiveService.getArchive(id));
            this.archiveService.deleteArchive(id);
        }
        redirectAttributes.addFlashAttribute("success", "L'archive à été supprimé avec succés!");
        return "redirect:/profile";
    }

    /*
     * Users controllers(GET request)
     */
    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public String usersGet(@RequestParam(name = "type", required = false) String userType, @RequestParam(value = "user-search", required = false) String search, @RequestParam(name = "page", defaultValue = "0") int page, @RequestParam(name = "type", required = false) String type, Model model, RedirectAttributes redirectAttributes) {
        if (userType != null && search != null) {
            if (type != null && type.equalsIgnoreCase("professors")) {
                model.addAttribute("users", this.professorService.getProfessors(search, page, this.size));
                model.addAttribute("user_search", search);
                model.addAttribute("type", "professors");
                model.addAttribute("title", "Liste Des Professeurs");
            } else {
                model.addAttribute("users", this.studentService.getStudents(search, page, this.size));
                model.addAttribute("title", "Liste Des Étudiants");
                model.addAttribute("user_search", search);
                model.addAttribute("type", "students");
            }
        } else {
            if (type != null && type.equalsIgnoreCase("professors")) {
                model.addAttribute("users", this.professorService.getProfessors(page, this.size));
                model.addAttribute("title", "Liste Des Professeurs");
                model.addAttribute("type", "professors");
            } else {
                model.addAttribute("users", this.studentService.getStudents(page, this.size));
                model.addAttribute("title", "Liste Des Étudiants");
                model.addAttribute("type", "students");
            }
        }


        return "users";
    }

    /*
     * User controllers(GET request)
     */
    @RequestMapping(value = "/users/{id}", method = RequestMethod.GET)
    public String userGet(@PathVariable(name = "id", required = true) Long id, Model model, RedirectAttributes redirectAttributes) {

        // Retrieve connected user
        User connectedUser = this.securityHelper.getConnectedUser();

        // Redirect user to his profile if the same profile
        if (connectedUser.getId() == id) {
            return "redirect:/profile";
        }

        // Retrieve user from database
        User user = this.userService.getUser(id);
        Professor professor = this.professorService.getProfessor(id);
        Student student = this.studentService.getStudent(id);

        // Put data in the model
        model.addAttribute("otherUser", user);
        model.addAttribute("professor", professor);
        model.addAttribute("archivesCounter", this.archiveService.getArchives().stream().filter(archive -> archive.getPublisher().getId() == user.getId()).count());
        model.addAttribute("student", student);
        model.addAttribute("roles", this.rolesMap);

        return "user";
    }

}
