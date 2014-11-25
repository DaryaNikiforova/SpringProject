package ru.tsystems.tsproject.sbb.controllers;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.tsystems.tsproject.sbb.services.UserService;
import ru.tsystems.tsproject.sbb.services.exceptions.UserAlreadyExistException;
import ru.tsystems.tsproject.sbb.transferObjects.UserTO;

import javax.inject.Inject;
import javax.validation.Valid;

/**
 * Created by apple on 17.11.14.
 */
@Controller
@RequestMapping("/user")
public class UserController {
    @Inject private UserService userService;

    @Inject
    @Qualifier("authenticationManager")
    private AuthenticationManager authManager;

    @RequestMapping(method = RequestMethod.GET, value = "login")
    public String login(Model model) {
        return "user/login";
    }

    @RequestMapping(method = RequestMethod.GET, value = "registration")
    public String register(Model model) {
        model.addAttribute("page", "registration");
        model.addAttribute("user", new UserTO());
        return "user/registration";
    }

    @RequestMapping(method = RequestMethod.POST, value = "registration")
    public String postRegister(Model model, @ModelAttribute("user") @Valid UserTO user, BindingResult result) {
        if (!result.hasErrors()) {
            String password = user.getPassword();
            try {
                userService.addUser(user);
            } catch (UserAlreadyExistException e) {
                e.printStackTrace();
            }
            Authentication authRequest = new UsernamePasswordAuthenticationToken(user.getLogin(), password);
            Authentication authResult = authManager.authenticate(authRequest);
            SecurityContextHolder.getContext().setAuthentication(authResult);
            return "redirect:/main/search";
        } else {
            model.addAttribute("errors", result.getAllErrors());
        }
        return "user/registration";
    }


}
