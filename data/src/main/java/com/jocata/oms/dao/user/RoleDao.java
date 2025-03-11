package com.jocata.oms.dao.user;

import com.jocata.oms.entity.user.RoleDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RoleDao extends JpaRepository<RoleDetails, Integer> {

    @Query( "SELECT r FROM RoleDetails r WHERE r.roleName = ?1")
    RoleDetails findByName( String name );
}
