package com.kongur.monolith.weixin.dao.system.impl;

import java.util.List;

import com.kongur.monolith.dal.MonoSqlMapClientDaoSupport;
import com.kongur.monolith.weixin.dao.system.SystemUserDAO;
import com.kongur.monolith.weixin.domain.system.SystemUser;

//@Repository("systemUserDAO")
public class SystemUserDAOImpl extends MonoSqlMapClientDaoSupport implements SystemUserDAO {

    @Override
    public List<SystemUser> selectSystemUsers() {
        return getSqlMapClientTemplate().queryForList("SystemUserDAO.selectSystemUsers");
    }

}
