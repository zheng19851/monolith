package com.kongur.monolith.weixin.service.system.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.kongur.monolith.weixin.dao.system.SystemUserDAO;
import com.kongur.monolith.weixin.domain.system.SystemUser;
import com.kongur.monolith.weixin.service.system.SystemUserService;

//@Service("systemUserService")
public class SystemUserServiceImpl implements SystemUserService {
    
    @Autowired
    private SystemUserDAO systemUserDAO;

    @Override
    public List<SystemUser> selectSystemUsers() {
        
        return systemUserDAO.selectSystemUsers();
    }

}
