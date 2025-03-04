package com.jocata.oms.dao;

import com.jocata.oms.entity.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface UserDao extends JpaRepository<UserDetails, Integer> {

    @Query( "SELECT u FROM UserDetails u WHERE u.email = ?1")
    UserDetails findByEmail(String email);

}
