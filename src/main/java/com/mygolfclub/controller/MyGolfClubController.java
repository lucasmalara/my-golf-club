package com.mygolfclub.controller;

import com.mygolfclub.entity.member.GolfClubMember;
import com.mygolfclub.service.member.GolfClubMemberService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import static com.mygolfclub.utils.constants.ConstantProvider.*;

@Controller
@RequestMapping(HOME)
public class MyGolfClubController {

    private final GolfClubMemberService memberService;

    @Autowired
    public MyGolfClubController(GolfClubMemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping
    public String showHomePage() {
        return PREFIX_DIR + "home";
    }

    @GetMapping("/members/list")
    public String showMembers(Model model) {
        model.addAttribute("activeOnly", false);
        model.addAttribute("members", memberService.findAll());
        return PREFIX_DIR + "members-list";
    }

    @GetMapping("/members/list/active")
    public String showActiveMembers(Model model) {
        model.addAttribute("activeOnly", true);
        model.addAttribute("members", memberService.findAllByActivity(true));
        return PREFIX_DIR + "members-list";
    }

    @GetMapping("/members/add")
    public String addMember(Model model) {
        model.addAttribute("member", new GolfClubMember());
        return PREFIX_DIR + SAVE_MEMBER_FILE;
    }

    @GetMapping("/members/update")
    public String updateMember(@RequestParam("memberId") int id,
                               Model model) {
        model.addAttribute("member", memberService.findById(id));
        return PREFIX_DIR + SAVE_MEMBER_FILE;
    }

    @PostMapping("/members/save")
    public String saveMember(@Valid @ModelAttribute("member") GolfClubMember model,
                             BindingResult result) {
        if (result.hasErrors())
            return PREFIX_DIR + SAVE_MEMBER_FILE;

        memberService.save(model);
        return "redirect:" + HOME + "/members/list";
    }

    @GetMapping("/members/delete")
    public String deleteMember(@RequestParam("memberId") int id) {
        memberService.deleteById(id);
        return "redirect:" + HOME + "/members/list";
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }
}
