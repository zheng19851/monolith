package com.kongur.monolith.im.dao.system;

import java.util.List;

import com.kongur.monolith.im.domain.system.SystemUser;


public interface SystemUserDAO {
    
    List<SystemUser> selectSystemUsers();

}
