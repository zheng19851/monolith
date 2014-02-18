package com.kongur.monolith.im.serivce;

import java.util.List;

import com.kongur.monolith.im.domain.ServiceResult;
import com.kongur.monolith.im.domain.User;



/**
 *用户管理服务
 *
 *@author zhengwei
 *
 *@date 2014-2-17	
 *
 */
public interface UserManagerService {
    
    /**
     * 获取关注者
     * 
     * @return
     */
    ServiceResult<List<User>> getUsers();

}

