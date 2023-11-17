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

import static com.mygolfclub.utils.constants.ConstantsProvider.*;

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
        return "add-user";
    }

    @PostMapping("/save")
    public String saveUser(@Valid @ModelAttribute("user") UserModel userModel,
                           BindingResult result,
                           Model model) {
        if (userService.findByUserName(userModel.getUsername()) != null) {
            model.addAttribute("alreadyTaken", "User already exists.");
        }
        if (!result.hasErrors()) {
            userService.save(userModel);
            model.addAttribute("hasBeenAdded", "User has been added.");
            return "add-user-success";
        }
        return "add-user";
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }
}
