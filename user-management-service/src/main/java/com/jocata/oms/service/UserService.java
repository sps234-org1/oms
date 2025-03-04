package com.jocata.oms.service;

import com.jocata.oms.bean.UserBean;

import java.util.List;

public interface UserService {

    UserBean createUser( UserBean userBean );

    UserBean getUser(Integer userId);

    UserBean updateUser(UserBean userBean);

    UserBean deleteUser(Integer userId,  boolean hardDelete );

    UserBean getUserByEmail( String email );

    List<UserBean> getAllUsers();
}
