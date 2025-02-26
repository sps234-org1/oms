package com.jocata.oms.dao;

import com.jocata.oms.entity.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao extends JpaRepository<UserDetails, Integer> {


}
