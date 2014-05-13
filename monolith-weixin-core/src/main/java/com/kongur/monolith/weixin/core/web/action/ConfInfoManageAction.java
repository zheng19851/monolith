package com.kongur.monolith.weixin.core.web.action;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kongur.monolith.common.result.Result;
import com.kongur.monolith.weixin.core.domain.MenusDO;
import com.kongur.monolith.weixin.core.domain.SubscribeReplyDO;
import com.kongur.monolith.weixin.core.manager.MenuManager;
import com.kongur.monolith.weixin.core.manager.SubscribeReplyManager;
import com.kongur.monolith.weixin.core.service.AccessTokenService;

/**
 * 管理当前微信平台公众帐号信息
 * 
 * @author zhengwei
 * @date 2014-2-18
 */
@Controller
public class ConfInfoManageAction {

    @Resource(name = "defaultAccessTokenService")
    private AccessTokenService    accessTokenService;

    @Autowired
    private MenuManager           menuManager;

    @Autowired
    private SubscribeReplyManager subscribeReplyManager;

    /**
     * 查看当前access token
     * 
     * @param model
     * @return
     */
    @RequestMapping("get_access_token.htm")
    public String viewAccessToken(Model model) {

        String accessToken = accessTokenService.getAccessToken();

        model.addAttribute("accessToken", accessToken);

        return "weixin/get_access_token";
    }

    /**
     * 刷新当前access token
     * 
     * @param model
     * @return
     */
    @RequestMapping("refresh_access_token.htm")
    public String refreshAccessToken(Model model) {

        Result<String> result = accessTokenService.refresh();

        model.addAttribute("result", result);

        return "weixin/refresh_access_token";
    }

    /**
     * 查看菜单
     * 
     * @param model
     * @return
     */
    @RequestMapping("get_menus.htm")
    public String getMenus(Model model) {

        MenusDO menus = menuManager.getMenus();

        model.addAttribute("menus", menus);

        return "weixin/get_menus";
    }

    /**
     * 查看订阅回复设置
     * 
     * @param model
     * @return
     */
    @RequestMapping("get_subscribe_reply.htm")
    public String getSubscribeReply(Model model) {

        SubscribeReplyDO subscribeReply = subscribeReplyManager.getSubscribeReply();

        model.addAttribute("reply", subscribeReply);

        return "weixin/get_subscribe_reply";
    }

    /**
     * 刷新菜单
     * 
     * @param model
     * @return
     */
    @RequestMapping("refresh_menus.htm")
    public String refreshMenus(Model model) {

        menuManager.refresh();

        return "success";
    }
    
    
    /**
     * 刷新菜单
     * 
     * @param model
     * @return
     */
    @RequestMapping("refresh_subscribe_reply.htm")
    public String refreshSubscribeReply(Model model) {

        subscribeReplyManager.refresh();

        return "success";
    }

}
