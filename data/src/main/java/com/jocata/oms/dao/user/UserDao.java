package com.jocata.oms.dao.user;

import com.jocata.oms.entity.user.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserDao extends JpaRepository<UserDetails, Integer> {

    @Query( "SELECT u FROM UserDetails u WHERE u.email = ?1")
    UserDetails findByEmail(String email);

}
