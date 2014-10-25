package com.runssnail.monolith.villadom.dao.system;

import java.util.List;

import com.runssnail.monolith.villadom.domain.system.SystemUser;


public interface SystemUserDAO {
    
    List<SystemUser> selectSystemUsers();

}
