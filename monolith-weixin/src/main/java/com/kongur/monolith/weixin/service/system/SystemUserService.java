package com.kongur.monolith.weixin.service.system;

import java.util.List;

import com.kongur.monolith.weixin.domain.system.SystemUser;



public interface SystemUserService {

    List<SystemUser> selectSystemUsers();

}
