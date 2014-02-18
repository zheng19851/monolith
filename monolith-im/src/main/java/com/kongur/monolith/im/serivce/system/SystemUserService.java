package com.kongur.monolith.im.serivce.system;

import java.util.List;

import com.kongur.monolith.im.domain.system.SystemUser;



public interface SystemUserService {

    List<SystemUser> selectSystemUsers();

}
