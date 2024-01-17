package com.mygolfclub.controller;

import com.mygolfclub.model.user.UserModel;
import com.mygolfclub.service.user.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import static com.mygolfclub.utils.constants.ConstantProvider.*;

@Controller
@RequestMapping(HOME + "/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/add")
    public String addUser(Model model) {
        model.addAttribute("user", new UserModel());
        return PREFIX_DIR_USERS + "add-user";
    }

    @PostMapping("/save")
    public String saveUser(@Valid @ModelAttribute("user") UserModel userModel,
                           BindingResult result,
                           Model model) {
        boolean userExists = userService.findByUserName(userModel.getUsername()) != null;
        if (userExists)
            model.addAttribute("alreadyTaken", "User already exists.");

        if (!result.hasErrors() && !userExists) {
            userService.save(userModel);
            model.addAttribute("hasBeenAdded", "User has been added.");
            return PREFIX_DIR_USERS + "add-user-success";
        }

        return PREFIX_DIR_USERS + "add-user";
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }
}
