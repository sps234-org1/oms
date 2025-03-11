package com.jocata.oms.dao;

import com.jocata.oms.entity.PermissionDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PermissionDao extends JpaRepository<PermissionDetails, Integer > {

    @Query( "SELECT p FROM PermissionDetails p WHERE p.permissionName = ?1")
    PermissionDetails findByPermissionName( String permissionName );

}
