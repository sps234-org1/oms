package com.jocata.oms.service.impl;

import com.jocata.oms.bean.AddressBean;
import com.jocata.oms.bean.PermissionBean;
import com.jocata.oms.bean.RoleBean;
import com.jocata.oms.bean.UserBean;
import com.jocata.oms.dao.PermissionDao;
import com.jocata.oms.dao.RoleDao;
import com.jocata.oms.dao.UserDao;
import com.jocata.oms.entity.AddressDetails;
import com.jocata.oms.entity.PermissionDetails;
import com.jocata.oms.entity.RoleDetails;
import com.jocata.oms.entity.UserDetails;
import com.jocata.oms.service.UserService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.*;

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
        userDetails.setIsActive(userBean.getIsActive());
        userDetails.setSmsEnabled(userBean.isSmsEnabled());
        userDetails.setPhone(userBean.getPhone());
        userDetails.setProfilePicture(userBean.getProfilePicture());
        userDetails.setOtpSecret(userBean.getOtpSecret());
        userDetails.setCreatedAt(new Date());

        List<RoleDetails> roleDetailsList = populateRole(userBean, userDetails);
        userDetails.setRoles(roleDetailsList);

        List<AddressDetails> addressDetailsList = populateAddress(userBean, userDetails);
        userDetails.setAddresses(addressDetailsList);

        UserBean userBeanDB = setDetails(userDao.save(userDetails));

        return userBeanDB;
    }

    List<AddressDetails> populateAddress(UserBean userBean, UserDetails userDetails) {

        List<AddressDetails> addressDetailsList = new ArrayList<>();
        List<AddressBean> addressBeans = userBean.getAddresses();

        if (addressBeans != null) {
            for (AddressBean address : addressBeans) {

                AddressDetails addressDetails = new AddressDetails();
                addressDetails.setAddress(address.getAddress());
                addressDetails.setCity(address.getCity());
                addressDetails.setState(address.getState());
                addressDetails.setCountry(address.getCountry());
                addressDetails.setZipCode(address.getZipCode());
                addressDetails.setCreatedAt(new Date());
                addressDetails.setUser(userDetails);
                addressDetailsList.add(addressDetails);
            }
        }

        return addressDetailsList;

    }

    List<RoleDetails> populateRole(UserBean userBean, UserDetails userDetails) {

        List<RoleDetails> roleDetailsList = new ArrayList<>();
        List<RoleBean> roleBeans = userBean.getRoles();

        for (RoleBean roleBean : roleBeans) {
            StringBuilder roleName = new StringBuilder();
            if (roleBean.getRoleName() != null) {
                roleName.append(roleBean.getRoleName().toUpperCase());
            } else {
                roleName.append("USER");
            }
            RoleDetails roleDetails = roleDao.findByName(roleName.toString());
            populatePermission(roleName.toString(), roleBean, roleDetails);
            roleDetails.getUsers().add(userDetails);
            roleDetails.setUsers(roleDetails.getUsers());
            roleDetailsList.add(roleDetails);
        }
        return roleDetailsList;
    }

    void populatePermission(String roleName, RoleBean roleBean, RoleDetails userRole) {

        if (roleName.equals("ADMIN")) {
            List<PermissionDetails> pd = permissionDao.findAll();
            userRole.setPermissions(pd);
        } else {

            List<PermissionDetails> permissionDetailsList = new ArrayList<>();
            if (roleBean.getPermissions() == null) {
                System.out.println("No permissions found for role: " + roleBean.getRoleName());
            } else {
                for (PermissionBean permissionBean : roleBean.getPermissions()) {

                    PermissionDetails permissionDetails = permissionDao.findByPermissionName(permissionBean.getPermissionName());
                    if (permissionDetails != null) {
                        permissionDetailsList.add(permissionDetails);
                    }
                }
            }
            userRole.setPermissions(permissionDetailsList);
        }
    }

    public UserBean getUser(Integer userId) {

        UserDetails userDetailsDb = userDao.findById(userId).orElse(null);
        if (userDetailsDb == null) {
            throw new IllegalArgumentException("User not found");
        }
        UserBean userBeanDB = setDetails(userDetailsDb);
        return userBeanDB;
    }

    public List<UserBean> getAllUsers() {
        List<UserDetails> userDetailsDb = userDao.findAll();
        if (userDetailsDb == null) {
            throw new IllegalArgumentException("User not found");
        }
        List<UserBean> userBeanDB = new ArrayList<>();
        for (UserDetails userDetails : userDetailsDb) {
            userBeanDB.add(setDetails(userDetails));
        }
        return userBeanDB;
    }

    public UserBean updateUser(UserBean userBean) {

        if (userBean == null) {
            throw new IllegalArgumentException("User details cannot be null");
        }
        if (userBean.getUserId() == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }

        UserBean userBeanDB = getUser(userBean.getUserId());
        if (userBeanDB == null) {
            throw new IllegalArgumentException("User can not be deleted");
        }
        return userBeanDB;
    }

    public UserBean deleteUser(Integer userId, boolean hardDelete) {
        UserBean userBeanDB = getUser(userId);
        if (hardDelete) {
            userDao.deleteById(userId);
            return null;
        }
        userBeanDB.setDeletedAt(new Date());
        userBeanDB.setIsActive(false);
        updateUser(userBeanDB);
        return null;
    }

    UserBean setDetails(UserDetails userDetailsDb) {

        UserBean userBeanDB = new UserBean();
        userBeanDB.setUserId(userDetailsDb.getUserId());
        userBeanDB.setFullName(userDetailsDb.getFullName());
        userBeanDB.setEmail(userDetailsDb.getEmail());
        userBeanDB.setPhone(userDetailsDb.getPhone());
        userBeanDB.setProfilePicture(userDetailsDb.getProfilePicture());
        userBeanDB.setOtpSecret(userDetailsDb.getOtpSecret());
        userBeanDB.setCreatedAt(userDetailsDb.getCreatedAt());
        userBeanDB.setPasswordHash(userDetailsDb.getPasswordHash());

        List<RoleBean> roleBeans = setRoleDetails(userDetailsDb.getRoles());
        userBeanDB.setRoles(roleBeans);

        List<AddressBean> addressBeansDB = setAddressDetails(userDetailsDb.getAddresses());
        userBeanDB.setAddresses(addressBeansDB);

        return userBeanDB;
    }

    List<RoleBean> setRoleDetails(List<RoleDetails> roleDetailsListDB) {

        if (roleDetailsListDB == null) {
            return null;
        }
        List<RoleBean> roleBeans = new ArrayList<>();
        for (RoleDetails roleDetails : roleDetailsListDB) {
            RoleBean roleBean = new RoleBean();
            roleBean.setRoleId(roleDetails.getRoleId());
            roleBean.setRoleName(roleDetails.getRoleName());
            roleBean.setPermissions(null);
            roleBeans.add(roleBean);

            List<PermissionBean> permissionBeans = setPermissionDetails(roleDetails.getPermissions());
            roleBean.setPermissions(permissionBeans);

        }
        return roleBeans;
    }

    List<PermissionBean> setPermissionDetails(List<PermissionDetails> permissionDetailsListDB) {

        if (permissionDetailsListDB == null) {
            return null;
        }
        List<PermissionBean> permissionBeans = new ArrayList<>();
        for (PermissionDetails permissionDetails : permissionDetailsListDB) {
            PermissionBean permissionBean = new PermissionBean();
            permissionBean.setPermissionId(permissionDetails.getPermissionId());
            permissionBean.setPermissionName(permissionDetails.getPermissionName());
            permissionBeans.add(permissionBean);
        }
        return permissionBeans;
    }

    List<AddressBean> setAddressDetails(List<AddressDetails> addressDetailsListDB) {

        if (addressDetailsListDB == null) {
            return null;
        }
        List<AddressBean> addressBeans = new ArrayList<>();
        for (AddressDetails addressDetails : addressDetailsListDB) {
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
        if (userDetailsDb == null) {
            throw new IllegalArgumentException("User not found");
        }
        UserBean userBeanDB = setDetails(userDetailsDb);
        return userBeanDB;
    }

    public List<UserBean> addUsersUsingExcel(MultipartFile file) {

        List<UserBean> userBeanList = getDataFromXLFile(file);
        if (userBeanList == null) {
            throw new IllegalArgumentException("No data found in the file");
        }
        for (UserBean userBean : userBeanList) {
            createUser(userBean);
        }

        List<UserDetails> userDetailsDb = new ArrayList<>();
        for (UserBean userBean : userBeanList) {
            userDetailsDb.add(userDao.findByEmail(userBean.getEmail()));
        }

        List<UserBean> userBeanListDB = new ArrayList<>();
        for (UserDetails userDetails : userDetailsDb) {
            userBeanListDB.add(setDetails(userDetails));
        }
        return userBeanListDB;
    }

    public List<UserBean> getDataFromXLFile(MultipartFile file) {

        List<UserBean> users = new ArrayList<>();
        try (InputStream fis = file.getInputStream()) {
            Workbook workbook;
            if (file.getOriginalFilename().endsWith(".xlsx")) {
                workbook = new XSSFWorkbook(fis);
            } else if (file.getOriginalFilename().endsWith(".xls")) {
                workbook = new HSSFWorkbook(fis);
            } else {
                throw new IllegalArgumentException("Invalid file format. Only .xls and .xlsx are supported.");
            }
            System.out.println("File Name: " + file.getOriginalFilename());
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();
            if (rowIterator.hasNext()) {
                rowIterator.next();
            }
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                UserBean user = populateUserBean(row);
                users.add(user);
            }
            workbook.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return users;
    }

    private String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                return String.valueOf((int) cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            case BLANK:
            default:
                return "";
        }
    }

    UserBean populateUserBean(Row row) {

        UserBean user = new UserBean();
        user.setFullName(getCellValueAsString(row.getCell(2)));
        user.setEmail(getCellValueAsString(row.getCell(3)));
        user.setPasswordHash(getCellValueAsString(row.getCell(4)));
        user.setPhone(getCellValueAsString(row.getCell(5)));
        user.setProfilePicture(getCellValueAsString(row.getCell(6)));
        user.setOtpSecret(getCellValueAsString(row.getCell(7)));
        user.setSmsEnabled(Boolean.parseBoolean(getCellValueAsString(row.getCell(8))));
        user.setIsActive(Boolean.parseBoolean(getCellValueAsString(row.getCell(9))));

        AddressBean address = new AddressBean();
        address.setAddress(getCellValueAsString(row.getCell(10)));
        address.setCity(getCellValueAsString(row.getCell(11)));
        address.setState(getCellValueAsString(row.getCell(12)));
        address.setCountry(getCellValueAsString(row.getCell(13)));
        address.setZipCode(getCellValueAsString(row.getCell(14)));
        user.setAddresses(Collections.singletonList(address));

        RoleBean role = new RoleBean();
        role.setRoleName(getCellValueAsString(row.getCell(15)));
        user.setRoles(Collections.singletonList(role));
        return user;
    }
}
