package com.jocata.oms.service.impl;

import com.jocata.oms.bean.UserBean;
import com.jocata.oms.dao.UserDao;
import com.jocata.oms.entity.AddressDetails;
import com.jocata.oms.entity.UserDetails;
import com.jocata.oms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    public UserDetails createUser(UserBean userBean) {

        UserDetails userDetails = new UserDetails();
        userDetails.setFullName(userBean.getFullName());
        userDetails.setEmail(userBean.getEmail());
        userDetails.setPasswordHash(userBean.getPasswordHash());
        userDetails.setIsActive( userBean.getIsActive() );
        userDetails.setSmsEnabled(userBean.isSmsEnabled());
        userDetails.setPhone(userBean.getPhone());
        userDetails.setProfilePicture(userBean.getProfilePicture());
        userDetails.setOtpSecret(userBean.getOtpSecret());
        userDetails.setCreatedAt( new Date() );

        List<AddressDetails> addressDetailsList = new ArrayList<>();
        for (AddressDetails address : userBean.getAddresses()) {
            AddressDetails newAddress = new AddressDetails();
            newAddress.setAddress(address.getAddress());
            newAddress.setZipCode(address.getZipCode());
            newAddress.setCity(address.getCity());
            newAddress.setState(address.getState());
            newAddress.setCountry( address.getCountry() );
            newAddress.setUser(userDetails);
            newAddress.setCreatedAt( new Date() );
            addressDetailsList.add(newAddress);
        }
        userDetails.setAddresses(addressDetailsList);
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
