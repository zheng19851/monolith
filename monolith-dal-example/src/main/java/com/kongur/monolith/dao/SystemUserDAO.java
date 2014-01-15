package com.kongur.monolith.dao;

import java.util.List;

import com.kongur.monolith.domain.SystemUser;


public interface SystemUserDAO {
    
    List<SystemUser> selectSystemUsers();

}
