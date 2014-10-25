package com.runssnail.monolith.villadom.dao.system.impl;

import java.util.List;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import org.springframework.stereotype.Repository;

import com.runssnail.monolith.villadom.dao.system.SystemUserDAO;
import com.runssnail.monolith.villadom.domain.system.SystemUser;

@Repository("systemUserDAO")
public class SystemUserDAOImpl extends SqlMapClientDaoSupport implements SystemUserDAO {

    @Override
    public List<SystemUser> selectSystemUsers() {
        return getSqlMapClientTemplate().queryForList("SystemUserDAO.selectSystemUsers");
    }

}
