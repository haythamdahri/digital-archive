package org.digital.archive.controllers;

import org.digital.archive.utils.SecurityHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/")
public class MainController {

    @Autowired
    private SecurityHelper securityHelper;

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
    public String profileGet(RedirectAttributes redirectAttributes) {
        // Redirect the user if already connected
        redirectAttributes.addFlashAttribute("error", "Vous êtes déja connecté");
        return "redirect:/";
    }


}
