package com.jocata.oms.controller;

import com.jocata.oms.entity.UserDetails;
import com.jocata.oms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping( "/api/v1/user" )
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping( "/create" )
    UserDetails createUser(UserDetails userDetails) {
        return userService.createUser(userDetails);
    }

    @GetMapping( "/get/{userId}" )
    UserDetails getUser(Integer userId) {
        return userService.getUser(userId);
    }

    @PutMapping( "/update" )
    UserDetails updateUser(UserDetails userDetails) {
        return userService.updateUser(userDetails);
    }

    @DeleteMapping( "/delete/{userId}" )
    UserDetails deleteUser(Integer userId){
        return userService.deleteUser(userId);
    }

}
