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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.util.*;

/**
 * @author Haytham DAHRI
 */
@Controller
@RequestMapping("/")
public class MainController {

    private static final int SIZE = 10;
    private SecurityHelper securityHelper;
    private ArchiveHelper archiveHelper;
    private StudentService studentService;
    private ProfessorService professorService;
    private UserService userService;
    private ArchiveService archiveService;
    private Map<RoleType, String> rolesMap;

    @Autowired
    public void setSecurityHelper(SecurityHelper securityHelper) {
        this.securityHelper = securityHelper;
    }

    @Autowired
    public void setArchiveHelper(ArchiveHelper archiveHelper) {
        this.archiveHelper = archiveHelper;
    }

    @Autowired
    public void setStudentService(StudentService studentService) {
        this.studentService = studentService;
    }

    @Autowired
    public void setProfessorService(ProfessorService professorService) {
        this.professorService = professorService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setArchiveService(ArchiveService archiveService) {
        this.archiveService = archiveService;
    }

    @Autowired
    public void setRolesMap(Map<RoleType, String> rolesMap) {
        this.rolesMap = rolesMap;
    }

    /**
     * Home Page
     *
     * @param professorId: Professor Identifier
     * @param page:        Page
     * @param search:      Search String
     * @return View
     */
    @GetMapping(path = "/")
    public ModelAndView home(@RequestParam(name = "professor", required = false) Long professorId, @RequestParam(name = "page", defaultValue = "0") int page, @RequestParam(value = "archive-search", required = false) String search) {
        ModelAndView modelAndView = new ModelAndView("index");

        // Put professor in the model
        if (professorId != null) {
            modelAndView.addObject("professor", this.professorService.getProfessor(professorId));
        }

        if (search != null) {
            if (professorId == null) {
                modelAndView.addObject("archives", this.archiveService.getArchives(search, page, SIZE));
            } else {
                modelAndView.addObject("archives", this.archiveService.getArchives(professorId, search, page, SIZE));
            }
        } else {
            if (professorId == null) {
                modelAndView.addObject("archives", this.archiveService.getArchives(page, SIZE));
            } else {
                modelAndView.addObject("archives", this.archiveService.getArchives(professorId, page, SIZE));
            }
        }

        modelAndView.addObject("search", search);
        return modelAndView;
    }

    /**
     * Login Page
     *
     * @param redirectAttributes: Redirect Attributes
     * @return View
     */
    @GetMapping(path = "/login")
    public String loginGet(RedirectAttributes redirectAttributes) {
        if (this.securityHelper.getConnectedUser() == null) {
            return "login";
        }

        // Redirect the user if already connected
        redirectAttributes.addFlashAttribute("error", "Vous êtes déja connecté");
        return "redirect:/";
    }

    /**
     * Logout user
     * @param request: HttpServletRequest
     * @return View
     * @throws ServletException: thrown exception
     */
    @GetMapping(path = "/logout")
    public String logout(HttpServletRequest request) throws ServletException {
       request.logout();
       return "redirect:/login";
    }

    /**
     * Get Authenticated User Profile
     *
     * @param page:  Page
     * @param model: Model
     * @return View
     */
    @GetMapping(path = "/profile")
    public String profileGet(@RequestParam(name = "page", defaultValue = "0") int page, Model model) {
        User user = this.securityHelper.getConnectedUser();

        // Retrieve the specialized classes from the parent one
        Professor professor = this.professorService.getProfessor(user.getId());
        Student student = this.studentService.getStudent(user.getId());


        model.addAttribute("user", user);
        model.addAttribute("userModal", user);
        model.addAttribute("professor", professor);
        model.addAttribute("student", student);
        model.addAttribute("archivesCounter", this.archiveService.getArchives().stream().filter(archive -> archive.getPublisher().getId().equals(user.getId())).count());
        model.addAttribute("archives", this.archiveService.getArchives(user.getId(), page, SIZE));
        model.addAttribute("roles", this.rolesMap);
        return "profile";
    }

    /**
     * Save Profile
     *
     * @param user:               User modal
     * @param bindingResult:      Binding Result Attribute to check form validity
     * @param page:               Page
     * @param model:              Model
     * @param redirectAttributes: Redirect Attributes
     * @return View
     */
    @PostMapping(path = "/profile")
    public String postProfile(@Valid @ModelAttribute("userModal") User user, BindingResult bindingResult, @RequestParam(name = "page", defaultValue = "0") int page, Model model, RedirectAttributes redirectAttributes) {

        // Adding archives to the model
        model.addAttribute("archives", this.archiveService.getArchives(page, SIZE));

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
                && tempUserByUsername != null && tempUserByUsername.getUsername().equalsIgnoreCase(username) && !tempUserByUsername.getId().equals(id)) {
            ObjectError error = new ObjectError("username", "Nom d'utilisateur est déja utilisé");
            bindingResult.addError(error);
            bindingResult.rejectValue("username", "username", "Nom d'utilisateur est déja utilisé");
        }
        if (tempUserByEmail != null && !user.getEmail().equalsIgnoreCase(originalUser.getEmail()) && !tempUserByEmail.getId().equals(user.getId()) && tempUserByEmail.getEmail().equalsIgnoreCase(user.getEmail())) {
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

    /**
     * Access Denied Page
     *
     * @return View
     */
    @GetMapping(path = "/access-denied")
    public String accessDenied() {
        return "access-denied";
    }

    /**
     * Update Profile Image
     *
     * @param file:               Image file
     * @param redirectAttributes: Redirect Attributes
     * @return View
     */
    @PostMapping(path = "/profile/update-picture")
    public String updateProfilePicture(@RequestParam("picture") MultipartFile file, RedirectAttributes redirectAttributes) {
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
            Files.write(Paths.get(SecurityConstants.usersPicturesUploadDirectory + user.getPicture()), bytes);

            // Redirect the user to his profile with a success message
            redirectAttributes.addFlashAttribute("success", "L'image de votre profil à été changée avec succés");
            return "redirect:/profile";

        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("error", "Une erreure est survenue, veuillez ressayer!");
            return "redirect:/profile";
        }
    }

    /*
     * @Archive controller (GET REQUESTS)
     *
     */

    /**
     * Get Profile
     *
     * @param model: Model
     * @return View
     */
    @PreAuthorize(value = "hasRole('ROLE_PROFESSOR')")
    @GetMapping(path = "/archives/save")
    public String getArchiveForm(Model model) {

        // Retrieve connected
        // Retrieve professors and students
        Collection<Professor> professors = this.professorService.getProfessors();
        Collection<Student> students = this.studentService.getStudents();

        model.addAttribute("user", this.securityHelper.getConnectedUser());
        model.addAttribute("professors", professors);
        model.addAttribute("students", students);
        Archive archive = new Archive();
        model.addAttribute("archive", archive);

        return "archive-form";

    }

    /**
     * Save Archive
     * Professors are only authroized to access this page
     *
     * @param id:                 Archive Id
     * @param title:              Archive Title
     * @param content:            Archive Content
     * @param professors:         Archive Professors
     * @param students:           Archive Students
     * @param image:              Archive Image
     * @param file:               Archive File
     * @param confirmImageEdit:   Archive Confirm Image Edit
     * @param confirmFileEdit:    Archive File Edit Confirmation
     * @param model:              Model
     * @param redirectAttributes: Redirect Attributes
     * @return View
     */
    @PreAuthorize(value = "hasRole('ROLE_PROFESSOR')")
    @PostMapping(path = "/archives/save")
    public String saveArchive(@RequestParam(value = "id", required = false) Long id, @RequestParam(value = "title", defaultValue = "empty") String title, @RequestParam(value = "content", defaultValue = "empty") String content,
                              @RequestParam(value = "professors", defaultValue = "empty") String[] professors, @RequestParam(value = "students", defaultValue = "empty") String[] students,
                              @RequestParam(value = "image", defaultValue = "empty") MultipartFile image, @RequestParam(value = "file", defaultValue = "empty") MultipartFile file,
                              @RequestParam(value = "confirm-image-edit", required = false) String confirmImageEdit,
                              @RequestParam(value = "confirm-file-edit", required = false) String confirmFileEdit, Model model,
                              RedirectAttributes redirectAttributes) {

        // Retrieve connected user
        User user = this.securityHelper.getConnectedUser();

        Archive archive;

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


                // Re-save archive
                this.archiveService.saveArchive(archive);

                // Redirect user with successfull message
                redirectAttributes.addFlashAttribute("success", "L'archive est enregistré avec succés");
                return "redirect:/archives/" + archive.getId();
            } else {
                throw new Exception();
            }
        } catch (Exception exception) {
            // Retrieve professors and students
            Collection<Professor> professorCollection = this.professorService.getProfessors();
            Collection<Student> studentCollection = this.studentService.getStudents();

            model.addAttribute("user", user);
            model.addAttribute("professors", professorCollection);
            model.addAttribute("students", studentCollection);
            model.addAttribute("archive", archive);

            Collection<String> stds = new ArrayList<>(Arrays.asList(students));
            Collection<String> profes = new ArrayList<>(Arrays.asList(professors));

            model.addAttribute("selectedProfessors", profes);
            model.addAttribute("selectedStudents", stds);

            model.addAttribute("error", "Une erreur est survenue, veuillez verifier les champs puis ressayer!");

            return "archive-form";
        }
    }

    /*
     * Single Archive
     */

    /**
     * Retrieve an archive
     *
     * @param id:                 Archive Identifier
     * @param model:              Model
     * @param redirectAttributes: Redirect Attributes
     * @return Views
     */
    @GetMapping(path = "/archives/{id}")
    public String getArchive(@PathVariable(name = "id", required = false) Long id, Model model, RedirectAttributes redirectAttributes) {
        if (id != null) {

            // Retrieve the archive from database
            Archive archive = this.archiveService.getArchive(id);

            if (archive != null) {
                // Increment views counter
                archive.setViews(archive.getViews() != null ? archive.getViews() + 1 : 1);
                archive = this.archiveService.saveArchive(archive);

                String publishDate = DateFormat.getDateInstance(
                        DateFormat.LONG, Locale.FRANCE).format(archive.getPublishDate());
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

    /**
     * Edit an archive
     *
     * @param id:                 Archive ID
     * @param model:              Model
     * @param redirectAttributes: Redirect Attributes
     * @return View
     */
    @PreAuthorize(value = "hasRole('ROLE_PROFESSOR')")
    @GetMapping(path = "/archives/{id}/edit")
    public String startArchiveEditing(@PathVariable(name = "id", required = false) Long id, Model model, RedirectAttributes redirectAttributes) {

        if (id != null) {
            // Retrieve the archive from database
            Archive archive = this.archiveService.getArchive(id);
            if (archive != null) {

                // Retrieve connected
                User user = this.securityHelper.getConnectedUser();

                // Retrieve professors and students
                model.addAttribute("user", user);
                model.addAttribute("professors", this.professorService.getProfessors());
                model.addAttribute("students", this.studentService.getStudents());
                model.addAttribute("archive", archive);


                // Retrieve selected professors and students
                Collection<String> professorsIds = new ArrayList<>();
                Collection<String> studentsIds = new ArrayList<>();

                archive.getProfessors().forEach(professor -> professorsIds.add(professor.getId().toString()));

                archive.getStudents().forEach(student -> studentsIds.add(student.getId().toString()));


                model.addAttribute("selectedProfessors", professorsIds);
                model.addAttribute("selectedStudents", studentsIds);


                // Reformat date
                String publishDate = DateFormat.getDateInstance(
                        DateFormat.LONG, Locale.FRANCE).format(archive.getPublishDate());
                model.addAttribute("archive", archive);
                model.addAttribute("publishDate", publishDate);
                return "archive-form";
            }
        }
        redirectAttributes.addFlashAttribute("error", "Aucun archive n'a été trouvé!");
        return "redirect:/";

    }

    /**
     * Delete a given archive
     *
     * @param id:                 Archive ID
     * @param redirectAttributes: Redirect attributes
     * @return View
     */
    @PostMapping(path = "/archives/delete")
    public String deleteArchivePost(@RequestParam(name = "id", required = false) Long id, RedirectAttributes redirectAttributes) {
        if (id != null) {
            this.archiveService.deleteArchive(id);
        }
        redirectAttributes.addFlashAttribute("success", "L'archive à été supprimé avec succés!");
        return "redirect:/profile";
    }

    /**
     * Users Get Endpoint
     *
     * @param userType: User type: Professor Or Student
     * @param search:   User Search
     * @param page:     Page
     * @param type:     Type
     * @param model:    Model
     * @return View
     */
    @GetMapping(path = "/users")
    public String usersGet(@RequestParam(name = "type", required = false) String userType, @RequestParam(value = "user-search", required = false) String search, @RequestParam(name = "page", defaultValue = "0") int page, @RequestParam(name = "type", required = false) String type, Model model) {
        if (userType != null && search != null) {
            if (type != null && type.equalsIgnoreCase("professors")) {
                model.addAttribute("users", this.professorService.getProfessors(search, page, SIZE));
                model.addAttribute("user_search", search);
                model.addAttribute("type", "professors");
                model.addAttribute("title", "Liste Des Professeurs");
            } else {
                model.addAttribute("users", this.studentService.getStudents(search, page, SIZE));
                model.addAttribute("title", "Liste Des Étudiants");
                model.addAttribute("user_search", search);
                model.addAttribute("type", "students");
            }
        } else {
            if (type != null && type.equalsIgnoreCase("professors")) {
                model.addAttribute("users", this.professorService.getProfessors(page, SIZE));
                model.addAttribute("title", "Liste Des Professeurs");
                model.addAttribute("type", "professors");
            } else {
                model.addAttribute("users", this.studentService.getStudents(page, SIZE));
                model.addAttribute("title", "Liste Des Étudiants");
                model.addAttribute("type", "students");
            }
        }
        // Return users view
        return "users";
    }

    /*
     * User controllers(GET request)
     */
    @GetMapping(path = "/users/{id}")
    public String userGet(@PathVariable(name = "id") Long id, Model model) {

        // Retrieve connected user
        User connectedUser = this.securityHelper.getConnectedUser();

        // Redirect user to his profile if the same profile
        if (connectedUser.getId().equals(id)) {
            return "redirect:/profile";
        }

        // Retrieve user from database
        User user = this.userService.getUser(id);
        Professor professor = this.professorService.getProfessor(id);
        Student student = this.studentService.getStudent(id);

        // Put data in the model
        model.addAttribute("otherUser", user);
        model.addAttribute("professor", professor);
        model.addAttribute("archivesCounter", this.archiveService.getArchives().stream().filter(archive -> archive.getPublisher().getId().equals(user.getId())).count());
        model.addAttribute("student", student);
        model.addAttribute("roles", this.rolesMap);

        return "user";
    }

}
