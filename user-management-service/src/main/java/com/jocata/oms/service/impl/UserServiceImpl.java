package com.jocata.oms.service.impl;

import com.jocata.oms.bean.AddressBean;
import com.jocata.oms.bean.PermissionBean;
import com.jocata.oms.bean.RoleBean;
import com.jocata.oms.bean.UserBean;
import com.jocata.oms.dao.PermissionDao;
import com.jocata.oms.dao.RoleDao;
import com.jocata.oms.dao.UserDao;
import com.jocata.oms.entity.*;
import com.jocata.oms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.management.relation.Role;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private PermissionDao permissionDao;


    public UserBean createUser(UserBean userBean) {

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

        List<RoleDetails> roleDetailsList = new ArrayList<>();
        List<RoleBean> roleBeans = userBean.getRoles();

        for( RoleBean roleBean : roleBeans ) {

            RoleDetails userRole ;
            if ( roleBean.getRoleName() == null ) {
                userRole = roleDao.findByName("USER");
            }
            else {
                userRole = roleDao.findByName( roleBean.getRoleName().toUpperCase() );
            }

            List<PermissionDetails> permissionDetailsList = new ArrayList<>();
            for ( PermissionBean permissionBean : roleBean.getPermissions() ) {
                PermissionDetails permissionDetails = permissionDao.findByPermissionName( permissionBean.getPermissionName() );
                if( permissionDetails != null ) {
                    permissionDetailsList.add( permissionDetails );
                }
            }
            userRole.setPermissions( permissionDetailsList );
            userRole.getUsers().add( userDetails );
            userRole.setUsers( userRole.getUsers() );
            roleDetailsList.add(userRole);
        }
        userDetails.setRoles( roleDetailsList );

        List<AddressDetails> addressDetailsList = populateAddress( userBean, userDetails );
        userDetails.setAddresses(addressDetailsList);

        UserBean userBeanDB = setDetails( userDao.save(userDetails));

        return userBeanDB;
    }

    List<AddressDetails> populateAddress( UserBean userBean, UserDetails userDetails ) {

        List<AddressDetails> addressDetailsList = new ArrayList<>();
        List<AddressBean> addressBeans = userBean.getAddresses();

        if ( addressBeans != null ) {
            for (AddressBean address : addressBeans ) {

                AddressDetails addressDetails = new AddressDetails();
                addressDetails.setAddress(address.getAddress());
                addressDetails.setCity(address.getCity());
                addressDetails.setState(address.getState());
                addressDetails.setCountry(address.getCountry());
                addressDetails.setZipCode(address.getZipCode());
                addressDetails.setCreatedAt( new Date() );
                addressDetails.setUser(userDetails);
                addressDetailsList.add(addressDetails);
            }
        }

        return addressDetailsList;

    }

    List<RoleDetails> populateDB( List<RoleBean> roleBeans ) {

        return null;
    }

    public UserBean getUser( Integer userId) {

        UserDetails userDetailsDb = userDao.findById(userId).orElse(null);
        if ( userDetailsDb == null ) {
            throw new IllegalArgumentException("User not found");
        }
        UserBean userBeanDB = setDetails( userDetailsDb );
        return userBeanDB;
    }

    public List<UserBean> getAllUsers(){
        List<UserDetails> userDetailsDb = userDao.findAll();
        if ( userDetailsDb == null ) {
            throw new IllegalArgumentException("User not found");
        }
        List<UserBean> userBeanDB = new ArrayList<>();
        for(UserDetails userDetails : userDetailsDb){
            userBeanDB.add(setDetails(userDetails));
        }
        return userBeanDB;
    }

    public UserBean updateUser(UserBean userBean) {

        if ( userBean == null ) {
            throw new IllegalArgumentException("User details cannot be null");
        }
        if ( userBean.getUserId() == null ) {
            throw new IllegalArgumentException("User ID cannot be null");
        }

        UserBean userBeanDB = getUser( userBean.getUserId() );
        if( userBeanDB == null ) {
            throw new IllegalArgumentException("User can not be deleted");
        }
        return userBeanDB;
    }

    public UserBean deleteUser( Integer userId, boolean hardDelete) {
        UserBean userBeanDB = getUser( userId );
        if ( hardDelete ) {
            userDao.deleteById(userId);
            return null;
        }
        userBeanDB.setDeletedAt( new Date() );
        userBeanDB.setIsActive(false);
        updateUser( userBeanDB );
        return null;
    }

    UserBean setDetails( UserDetails userDetailsDb ) {

        UserBean userBeanDB = new UserBean();
        userBeanDB.setUserId(userDetailsDb.getUserId());
        userBeanDB.setFullName(userDetailsDb.getFullName());
        userBeanDB.setEmail(userDetailsDb.getEmail());
        userBeanDB.setPhone(userDetailsDb.getPhone());
        userBeanDB.setProfilePicture(userDetailsDb.getProfilePicture());
        userBeanDB.setOtpSecret(userDetailsDb.getOtpSecret());
        userBeanDB.setCreatedAt(userDetailsDb.getCreatedAt());

        List<RoleBean> roleBeans = setRoleDetails( userDetailsDb.getRoles() );
        userBeanDB.setRoles(roleBeans);

        List<AddressBean> addressBeansDB = setAddressDetails( userDetailsDb.getAddresses() );
        userBeanDB.setAddresses(addressBeansDB);

        return userBeanDB;
    }

    List<RoleBean> setRoleDetails( List<RoleDetails> roleDetailsListDB ) {

        if ( roleDetailsListDB == null ) {
            return null;
        }
        List<RoleBean> roleBeans = new ArrayList<>();
        for ( RoleDetails roleDetails : roleDetailsListDB ) {
            RoleBean roleBean = new RoleBean();
            roleBean.setRoleId(roleDetails.getRoleId());
            roleBean.setRoleName( roleDetails.getRoleName());
            roleBean.setPermissions( null );
            roleBeans.add(roleBean);

            List<PermissionBean> permissionBeans = setPermissionDetails( roleDetails.getPermissions() );
            roleBean.setPermissions( permissionBeans );

        }
        return roleBeans;
    }

    List<PermissionBean> setPermissionDetails( List<PermissionDetails> permissionDetailsListDB ) {

        if ( permissionDetailsListDB == null ) {
            return null;
        }
        List<PermissionBean> permissionBeans = new ArrayList<>();
        for ( PermissionDetails permissionDetails : permissionDetailsListDB ) {
            PermissionBean permissionBean = new PermissionBean();
            permissionBean.setPermissionId(permissionDetails.getPermissionId());
            permissionBean.setPermissionName( permissionDetails.getPermissionName());
            permissionBeans.add(permissionBean);
        }
        return permissionBeans;
    }

    List<AddressBean> setAddressDetails( List<AddressDetails> addressDetailsListDB ) {

        if ( addressDetailsListDB == null ) {
            return null;
        }
        List<AddressBean> addressBeans = new ArrayList<>();
        for ( AddressDetails addressDetails : addressDetailsListDB ) {
            AddressBean addressBean = new AddressBean();
            addressBean.setAddressId(addressDetails.getAddressId());
            addressBean.setAddress(addressDetails.getAddress());
            addressBean.setCity(addressDetails.getCity());
            addressBean.setState(addressDetails.getState());
            addressBean.setCountry(addressDetails.getCountry());
            addressBean.setZipCode(addressDetails.getZipCode());
            addressBean.setCreatedAt(addressDetails.getCreatedAt());
            addressBeans.add(addressBean);
        }
        return addressBeans;
    }

    public UserBean getUserByEmail(String email) {
        UserDetails userDetailsDb = userDao.findByEmail(email);
        if ( userDetailsDb == null ) {
            throw new IllegalArgumentException("User not found");
        }
        UserBean userBeanDB = setDetails( userDetailsDb );
        return userBeanDB;
    }

}
