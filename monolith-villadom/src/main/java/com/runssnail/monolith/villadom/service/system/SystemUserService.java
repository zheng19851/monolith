package com.runssnail.monolith.villadom.service.system;

import java.util.List;

import com.runssnail.monolith.villadom.domain.system.SystemUser;



public interface SystemUserService {

    List<SystemUser> selectSystemUsers();

}
