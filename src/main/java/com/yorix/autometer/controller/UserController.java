package com.yorix.autometer.controller;

import com.yorix.autometer.model.User;
import com.yorix.autometer.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

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

    @PostMapping
    public ModelAndView create(@Valid User user, BindingResult bindingResult, @RequestParam("passwordConfirm") String passwordConfirm) {
        ModelAndView modelAndView = new ModelAndView("user-list");
        modelAndView.addObject("users", userService.readAll());
        modelAndView.addObject("budget", userService.getBudget());
        modelAndView.addObject("balance", userService.getBalance());
        if (bindingResult.hasErrors()) {
            modelAndView.addAllObjects(ControllerUtils.getErrors(bindingResult));
        }
        if (userService.getUser(user.getUsername()) != null) {
            modelAndView.addObject("userExistsError", "Пользователь с таким именем уже существует.");
        }
        if (!passwordConfirm.equals(user.getPassword())) {
            modelAndView.addObject("passConfirmError", "Пароли не совпадают.");
        }
        if (modelAndView.getModel().size() < 4) {
            userService.create(user);
        }
        return modelAndView;
    }
}
