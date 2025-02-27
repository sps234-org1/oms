package com.jocata.oms.controller;

import com.jocata.oms.bean.UserBean;
import com.jocata.oms.entity.UserDetails;
import com.jocata.oms.request.GenericRequestPayload;
import com.jocata.oms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping( "/api/v1/user" )
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping( "/create" )
    UserDetails createUser(@RequestBody GenericRequestPayload genericRequestPayload ) {
        return userService.createUser( (UserBean) genericRequestPayload.getData() );
    }

    @GetMapping( "/get" )
    UserDetails getUser(@RequestParam Integer userId) {
        return userService.getUser(userId);
    }

    @PutMapping( "/update" )
    UserDetails updateUser(@RequestBody UserDetails userDetails) {
        return userService.updateUser(userDetails);
    }

    @DeleteMapping( "/delete" )
    UserDetails deleteUser(@RequestParam Integer userId){
        return userService.deleteUser(userId);
    }

}
