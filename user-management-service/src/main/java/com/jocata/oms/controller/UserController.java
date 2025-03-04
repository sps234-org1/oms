package com.jocata.oms.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jocata.oms.bean.UserBean;
import com.jocata.oms.entity.UserDetails;
import com.jocata.oms.request.GenericRequestPayload;
import com.jocata.oms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @PostMapping("/create")
    UserBean createUser(@RequestBody GenericRequestPayload genericRequestPayload) {
        UserBean userBean = objectMapper.convertValue(genericRequestPayload.getData(), UserBean.class);
        return userService.createUser( userBean );
    }

    @GetMapping("/user/get")
    UserBean getUser(@RequestParam Integer userId) {
        return userService.getUser(userId);
    }

    @PutMapping("/update")
    UserBean updateUser(@RequestBody UserBean userBean) {
        return userService.updateUser( userBean);
    }

    @DeleteMapping("/delete")
    UserBean deleteUser(@RequestParam Integer userId, @RequestParam boolean hardDelete ) {
        return userService.deleteUser(userId, hardDelete );
    }

    @GetMapping("/getByEmail")
    UserBean getUserByEmail(@RequestParam String email) {
        return userService.getUserByEmail( email );
    }

    @GetMapping("/admin/getAll")
    List<UserBean> getAllUsers() {
        return userService.getAllUsers();
    }

}
