package com.kongur.monolith.dal.test.dao;

import com.kongur.monolith.dal.test.domain.UserDO;


public interface UserDAO {
    
    UserDO selectById(Integer id);

}
