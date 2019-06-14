package org.digital.archive.controllers;

import org.digital.archive.entities.Professor;
import org.digital.archive.entities.Student;
import org.digital.archive.entities.User;
import org.digital.archive.services.ProfessorService;
import org.digital.archive.services.StudentService;
import org.digital.archive.services.UserService;
import org.digital.archive.utils.SecurityHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/")
public class MainController {

    @Autowired
    private SecurityHelper securityHelper;

    @Autowired
    private StudentService studentService;

    @Autowired
    private ProfessorService professorService;

    @Autowired
    private UserService userService;

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
     * Login page controller (GET REQUESTS)
     */
    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public String profileGet(Model model, RedirectAttributes redirectAttributes) {
        User user = this.securityHelper.getConnectedUser();

        Professor professor = this.professorService.getProfessor(user.getEmail());
        Student student = this.studentService.getStudent(user.getEmail());

        System.out.println(path);

        model.addAttribute("user", user);
        model.addAttribute("professor", professor);
        model.addAttribute("student", student);
        return "profile";
    }


}
