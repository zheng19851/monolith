package com.kongur.monolith.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.kongur.monolith.dal.MonoSqlMapClientDaoSupport;
import com.kongur.monolith.dao.SystemUserDAO;
import com.kongur.monolith.domain.SystemUser;

@Repository("systemUserDAO")
public class SystemUserDAOImpl extends MonoSqlMapClientDaoSupport implements SystemUserDAO {

    @Override
    public List<SystemUser> selectSystemUsers() {
        return getSqlMapClientTemplate().queryForList("SystemUserDAO.selectSystemUsers");
    }

}
