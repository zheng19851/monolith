package com.kongur.monolith.dal.test.dao.impl;

import java.util.HashMap;
import java.util.Map;

import org.mybatis.spring.support.SqlSessionDaoSupport;

import com.kongur.monolith.dal.test.dao.UserDAO;
import com.kongur.monolith.dal.test.domain.UserDO;


public class UserDAOImpl extends SqlSessionDaoSupport implements UserDAO {

    @Override
    public UserDO selectById(Integer id) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", id);
        return getSqlSession().selectOne("USER.selectById", params);
    }

}
