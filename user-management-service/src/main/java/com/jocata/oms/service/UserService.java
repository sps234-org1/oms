package com.jocata.oms.service;

import com.jocata.oms.entity.UserDetails;

public interface UserService {

    UserDetails createUser(UserDetails userDetails);

    UserDetails getUser(Integer userId);

    UserDetails updateUser(UserDetails userDetails);

    UserDetails deleteUser(Integer userId);
}
