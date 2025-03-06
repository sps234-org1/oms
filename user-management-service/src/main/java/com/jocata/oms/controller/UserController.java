package com.jocata.oms.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jocata.oms.bean.UserBean;
import com.jocata.oms.request.GenericRequestPayload;
import com.jocata.oms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @PostMapping("/user/create")
    UserBean createUser(@RequestBody GenericRequestPayload genericRequestPayload) {
        UserBean userBean = objectMapper.convertValue(genericRequestPayload.getData(), UserBean.class);
        return userService.createUser( userBean );
    }

    @GetMapping("/user/get")
    UserBean getUser(@RequestParam Integer userId) {
        return userService.getUser(userId);
    }

    @PutMapping("/user/update")
    UserBean updateUser(@RequestBody UserBean userBean) {
        return userService.updateUser( userBean);
    }

    @DeleteMapping("/user/delete")
    UserBean deleteUser(@RequestParam Integer userId, @RequestParam boolean hardDelete ) {
        return userService.deleteUser(userId, hardDelete );
    }

    @GetMapping("/user/getByEmail")
    UserBean getUserByEmail(@RequestParam String email) {
        return userService.getUserByEmail( email );
    }

    @GetMapping("/admin/getAll")
    List<UserBean> getAllUsers() {
        return userService.getAllUsers();
    }


    @PostMapping( "/admin/add/users/xls/upload" )
    List<UserBean> addUsersUsingExcel( @RequestParam("file") MultipartFile file ) {
        return userService.addUsersUsingExcel(file);
    }


    @PostMapping( "/admin/add/users/pdf/upload" )
    String addUsersUsingPdf( @RequestParam("file") MultipartFile file ) {
        return "";
    }



}
