package com.jocata.oms.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jocata.oms.bean.UserBean;
import com.jocata.oms.entity.UserDetails;
import com.jocata.oms.request.GenericRequestPayload;
import com.jocata.oms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @PostMapping("/create")
    UserDetails createUser(@RequestBody GenericRequestPayload genericRequestPayload) {
        UserBean userBean = objectMapper.convertValue(genericRequestPayload.getData(), UserBean.class);
        return userService.createUser( userBean );
    }

    @GetMapping("/get")
    UserDetails getUser(@RequestParam Integer userId) {
        return userService.getUser(userId);
    }

    @PutMapping("/update")
    UserDetails updateUser(@RequestBody UserDetails userDetails) {
        return userService.updateUser(userDetails);
    }

    @DeleteMapping("/delete")
    UserDetails deleteUser(@RequestParam Integer userId) {
        return userService.deleteUser(userId);
    }

}
