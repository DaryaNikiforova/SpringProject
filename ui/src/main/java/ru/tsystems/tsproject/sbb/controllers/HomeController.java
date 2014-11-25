package ru.tsystems.tsproject.sbb.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by apple on 13.11.14.
 */
@Controller
@RequestMapping("/index")
public class HomeController {
    @RequestMapping
    public String Index(Model model) {
        model.addAttribute("page", "index");
        return "home/index";
    }
}
