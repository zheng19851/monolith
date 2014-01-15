package com.kongur.monolith.villadom.dao.system;

import java.util.List;

import com.kongur.monolith.villadom.domain.system.SystemUser;


public interface SystemUserDAO {
    
    List<SystemUser> selectSystemUsers();

}
