package com.kongur.monolith.im.serivce.system.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.kongur.monolith.im.dao.system.SystemUserDAO;
import com.kongur.monolith.im.domain.system.SystemUser;
import com.kongur.monolith.im.serivce.system.SystemUserService;

//@Service("systemUserService")
public class SystemUserServiceImpl implements SystemUserService {
    
    @Autowired
    private SystemUserDAO systemUserDAO;

    @Override
    public List<SystemUser> selectSystemUsers() {
        
        return systemUserDAO.selectSystemUsers();
    }

}
