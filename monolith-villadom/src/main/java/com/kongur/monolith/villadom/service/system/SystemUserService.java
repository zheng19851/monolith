package com.kongur.monolith.villadom.service.system;

import java.util.List;

import com.kongur.monolith.villadom.domain.system.SystemUser;



public interface SystemUserService {

    List<SystemUser> selectSystemUsers();

}
