package com.abenezer.itom.repository;

import com.abenezer.itom.model.systemUserRecords.SystemUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<SystemUser, Integer>{

}