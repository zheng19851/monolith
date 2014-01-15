package com.kongur.monolith.villadom.dao.system.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.kongur.monolith.dal.MonoSqlMapClientDaoSupport;
import com.kongur.monolith.villadom.dao.system.SystemUserDAO;
import com.kongur.monolith.villadom.domain.system.SystemUser;

@Repository("systemUserDAO")
public class SystemUserDAOImpl extends MonoSqlMapClientDaoSupport implements SystemUserDAO {

    @Override
    public List<SystemUser> selectSystemUsers() {
        return getSqlMapClientTemplate().queryForList("SystemUserDAO.selectSystemUsers");
    }

}
