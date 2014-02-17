package com.kongur.monolith.weixin.web.action.system;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kongur.monolith.weixin.domain.system.SystemUser;
import com.kongur.monolith.weixin.service.system.SystemUserService;

//@Controller
//@RequestMapping("system")
public class SystemUserAction {

    @Autowired
    private SystemUserService systemUserService;

    @RequestMapping("list")
    public String list(Model model) {

        List<SystemUser> systemUserList = systemUserService.selectSystemUsers();
        model.addAttribute("systemUserList", systemUserList);
        return "system/list";
    }
}
