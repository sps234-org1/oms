package com.jocata.oms.service.impl;

import com.jocata.oms.bean.UserBean;
import com.jocata.oms.dao.UserDao;
import com.jocata.oms.entity.UserDetails;
import com.jocata.oms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    public UserDetails createUser(UserBean userBean) {

        UserDetails userDetails = new UserDetails();
        userDetails.setFullName(userBean.getFullName());
        userDetails.setEmail(userBean.getEmail());
        userDetails.setPasswordHash(userBean.getPasswordHash());
        userDetails.setActive(userBean.isActive());


        return userDao.save( userDetails );
    }

    public UserDetails getUser( Integer userId) {
        return userDao.findById( userId ).orElse( null );
    }

    public UserDetails updateUser(UserDetails userDetails) {
        return userDao.save(userDetails);
    }

    public UserDetails deleteUser( Integer userId) {
        UserDetails userDetails = userDao.findById( userId ).orElse( null );
        if( userDetails != null ) {
            userDao.delete( userDetails );
        }
        return userDetails;
    }

}
