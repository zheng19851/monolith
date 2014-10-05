package com.runssnail.monolith.dal.test.dao;

import com.runssnail.monolith.dal.test.domain.UserDO;


public interface UserDAO {
    
    UserDO selectById(Integer id);

}
