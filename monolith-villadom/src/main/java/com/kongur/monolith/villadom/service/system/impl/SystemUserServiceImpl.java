package com.kongur.monolith.villadom.service.system.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kongur.monolith.villadom.dao.system.SystemUserDAO;
import com.kongur.monolith.villadom.domain.system.SystemUser;
import com.kongur.monolith.villadom.service.system.SystemUserService;

@Service("systemUserService")
public class SystemUserServiceImpl implements SystemUserService {
    
    @Autowired
    private SystemUserDAO systemUserDAO;

    @Override
    public List<SystemUser> selectSystemUsers() {
        
        return systemUserDAO.selectSystemUsers();
    }

}
