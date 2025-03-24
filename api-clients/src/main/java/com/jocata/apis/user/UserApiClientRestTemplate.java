package com.jocata.apis.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jocata.oms.bean.UserBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@Service
public class UserApiClientRestTemplate {

    private final RestTemplate restTemplate;

    public UserApiClientRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private static final Logger logger = LoggerFactory.getLogger(UserApiClientRestTemplate.class);


    //    @CircuitBreaker(name = "userService", fallbackMethod = "fallbackGetUser")
    public UserBean getUserUsingRestTemplate(Integer userId) {

        String URL = "http://localhost:8081/user-mgmt-service/api/v1/user/get?userId=" + userId;
        try {

            ResponseEntity<String> rawRes = restTemplate.getForEntity(URL, String.class);
            logger.info("Raw response : " + rawRes.getBody());

            ObjectMapper mapper = new ObjectMapper();
            mapper.findAndRegisterModules();

            UserBean userBean = mapper.readValue(rawRes.getBody(), UserBean.class);

            if (userBean != null) {
                return userBean;
            } else {
                throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "User not found for userId: " + userId);
            }

        } catch (ResourceAccessException e) {
            logger.error("Ignored exception: {}", e.getMessage());
            throw e;
        } catch (HttpClientErrorException e) {
            logger.error("User not found: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error while fetching user details: {}", e.getMessage());
            throw new RuntimeException("Error while fetching user details for userId: " + userId, e);
        }
    }

    public UserBean fallbackGetUser(Throwable throwable) {
        logger.info("Fallback method triggered due to : " + throwable.getMessage());
        UserBean dummyUser = new UserBean();
        dummyUser.setUserId(-1);
        dummyUser.setFullName("The User Service is currently down. Try later.");
        return dummyUser;
    }

}
