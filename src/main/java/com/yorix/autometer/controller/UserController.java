package com.yorix.autometer.controller;

import com.yorix.autometer.model.Role;
import com.yorix.autometer.model.User;
import com.yorix.autometer.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Map;

@Controller
@RequestMapping("/user/")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ModelAndView userList() {
        ModelAndView modelAndView = new ModelAndView("user-list");
        modelAndView.addObject("users", userService.readAll());
        modelAndView.addObject("budget", userService.getBudget());
        modelAndView.addObject("balance", userService.getBalance());
        return modelAndView;
    }

    @GetMapping("{username}/")
    public ModelAndView get(@PathVariable("username") User user) {
        ModelAndView modelAndView = new ModelAndView("user");
        modelAndView.addObject("user", user);
        modelAndView.addObject("roles", Role.values());
        modelAndView.addObject("budget", userService.getBudget());
        modelAndView.addObject("balance", userService.getBalance());
        return modelAndView;
    }

    @PostMapping
    public ModelAndView create(@Valid User user, BindingResult bindingResult, @RequestParam("passwordConfirm") String passwordConfirm) {
        ModelAndView modelAndView = new ModelAndView("user-list");
        if (bindingResult.hasErrors()) {
            modelAndView.addAllObjects(ControllerUtils.getErrors(bindingResult));
        }
        if (userService.getUser(user.getUsername()) != null) {
            modelAndView.addObject("userExistsError", "Пользователь с таким именем уже существует.");
        }
        if (!passwordConfirm.equals(user.getPassword())) {
            modelAndView.addObject("passConfirmError", "Пароли не совпадают.");
        }
        if (modelAndView.getModel().size() == 0) {
            userService.create(user);
        }
        modelAndView.addObject("users", userService.readAll());
        modelAndView.addObject("budget", userService.getBudget());
        modelAndView.addObject("balance", userService.getBalance());
        return modelAndView;
    }

    @PutMapping("{username}/")
    public String update(
            @AuthenticationPrincipal User activeUser,
            @PathVariable("username") User user,
            @RequestParam Map<String, String> form,
            Model model
    ) {
        String password = form.get("password");
        String passwordConfirm = form.get("passwordConfirm");
        if (!activeUser.getRoles().contains(Role.ADMIN) &&
                (user.getRoles().contains(Role.ADMIN) || user.getRoles().contains(Role.POWER) && !activeUser.equals(user))) {
            model.addAttribute("accessError", "Не достаточно полномочий.");
        }
        if (!password.equals(passwordConfirm)) {
            model.addAttribute("passConfirmError", "Пароли не совпадают.");
        }
        if (model.asMap().size() == 0) {
            userService.update(user, form);
            return "redirect:../";
        }
        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());
        model.addAttribute("budget", userService.getBudget());
        model.addAttribute("balance", userService.getBalance());
        return "user";
    }

    @DeleteMapping("{username}/")
    public String delete(
            @PathVariable("username") User user,
            @AuthenticationPrincipal User activeUser,
            Model model
    ) {
        if (!activeUser.getRoles().contains(Role.ADMIN) &&
                (user.getRoles().contains(Role.ADMIN) || user.getRoles().contains(Role.POWER))) {
            model.addAttribute("accessError", "Не достаточно полномочий.");
            model.addAttribute("user", user);
            model.addAttribute("roles", Role.values());
            model.addAttribute("budget", userService.getBudget());
            model.addAttribute("balance", userService.getBalance());
            return "user";
        }
        userService.delete(user);
        return "redirect:../";
    }
}