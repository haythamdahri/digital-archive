package org.digital.archive.controllers;

import org.digital.archive.entities.Professor;
import org.digital.archive.entities.RoleType;
import org.digital.archive.entities.Student;
import org.digital.archive.entities.User;
import org.digital.archive.services.ProfessorService;
import org.digital.archive.services.StudentService;
import org.digital.archive.services.UserService;
import org.digital.archive.utils.ArchiveHelper;
import org.digital.archive.utils.SecurityConstants;
import org.digital.archive.utils.SecurityHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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
    private Map<RoleType, String> rolesMap;

    @Value("${file.upload-dir}")
    private String path;

    /*
     * Home page controller (GET REQUESTS)
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView home() {
        ModelAndView modelAndView = new ModelAndView("index");
        modelAndView.addObject("name", "HAYTHAM DAHRI");
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
    public String profileGet(Model model, RedirectAttributes redirectAttributes) {
        User user = this.securityHelper.getConnectedUser();

        // Retrieve the specialized classes from the parent one
        Professor professor = this.professorService.getProfessor(user.getId());
        Student student = this.studentService.getStudent(user.getId());

        model.addAttribute("user", user);
        model.addAttribute("userModal", user);
        model.addAttribute("professor", professor);
        model.addAttribute("student", student);
        model.addAttribute("roles", this.rolesMap);
        return "profile";
    }

    /*
     * Profile page controller (POST REQUESTS)
     */
    @RequestMapping(value = "/profile", method = RequestMethod.POST)
    public String postProfile(@Valid @ModelAttribute("userModal") User user, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {

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

        // Save the user using the student or professor object
        if (student != null) {
            student.setLastName(user.getLastName());
            student.setFirstName(user.getFirstName());
            student.setUsername(user.getUsername());
            student.setEmail(user.getEmail());
            student.setBirthDate(user.getBirthDate());
            student = this.studentService.saveStudent(student);
            user = student;
        } else {
            professor.setLastName(user.getLastName());
            professor.setFirstName(user.getFirstName());
            professor.setUsername(user.getUsername());
            professor.setEmail(user.getEmail());
            professor.setBirthDate(user.getBirthDate());
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



}
