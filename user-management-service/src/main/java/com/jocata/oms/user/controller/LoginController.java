package com.jocata.oms.user.controller;

import com.jocata.oms.bean.UserBean;
import com.jocata.oms.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth/")
public class LoginController {

    @Autowired
    private UserService userService;

    @GetMapping("/user/get")
    UserBean getUser(@RequestParam String userId) {
        return userService.getUser(Integer.valueOf(userId));
    }

}
