package com.mygolfclub.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import static com.mygolfclub.utils.constants.ConstantProvider.*;

@Controller
public class AccessController {

    @GetMapping
    public String redirectHome() {
        return "redirect:" + HOME;
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return PREFIX_DIR_USERS + "login";
    }

    @GetMapping("/access-denied")
    public String showAccessDeniedPage() {
        return PREFIX_DIR_USERS + "access-denied";
    }
}
