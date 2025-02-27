package com.jocata.oms.um;


import com.jocata.oms.dao.UserDao;
import com.jocata.oms.entity.UserDetails;
import org.junit.jupiter.api.Assertions;

import org.junit.jupiter.api.BeforeAll;

import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)

public class UserDaoTest {

    @Autowired
    private UserDao userDao;

//    @BeforeAll
//
//    void setup() {
//
//        userDao = new UserDaoImpl();
//
//    }

    @Test
    void testSaveUser() {

        UserDetails user = new UserDetails();

        user.setFullName("Abc ");

        user.setEmail("abc@gmail.com");

        user.setPasswordHash("235y29osdkfalR@#$");

        user.setActive(true);

        userDao.save(user);

        UserDetails retrievedUser = userDao.findById( 2 ).orElse( null );

        Assertions.assertNotNull(retrievedUser);

        Assertions.assertEquals("John Doe", retrievedUser.getFullName());

    }

//    @Test
//
//    void testGetAllUsers() {
//
//        List<UserDetails> users = userDao.getAllUsers();
//
//        Assertions.assertFalse(users.isEmpty());
//
//    }

}
