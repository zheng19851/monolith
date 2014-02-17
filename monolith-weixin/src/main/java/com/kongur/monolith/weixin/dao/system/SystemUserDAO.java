package com.kongur.monolith.weixin.dao.system;

import java.util.List;

import com.kongur.monolith.weixin.domain.system.SystemUser;


public interface SystemUserDAO {
    
    List<SystemUser> selectSystemUsers();

}
