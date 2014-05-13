package com.kongur.monolith.weixin.core.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.velocity.VelocityEngineUtils;

import com.kongur.monolith.common.result.Result;
import com.kongur.monolith.weixin.core.domain.Menu;
import com.kongur.monolith.weixin.core.manager.MenuManager;
import com.kongur.monolith.weixin.core.service.RemoteMenuService;
import com.kongur.monolith.weixin.core.service.WeixinApiService;

/**
 * @author zhengwei
 * @date 2014年2月20日
 */
@Service("remoteMenuService")
public class RemoteMenuServiceImpl implements RemoteMenuService {

    private final Logger     log                   = Logger.getLogger(getClass());

    /**
     * 创建菜单api url
     */
    // @Value("${weixin.api.menu.create.url.pattern}")
    private String           createMenusUrlPattern = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=${access_token}";
    private String           createMenusTemplate   = "menu/create_menus.vm";

    /**
     * 删除菜单api url
     */
    private String           removeMenusUrlPattern = "https://api.weixin.qq.com/cgi-bin/menu/delete?access_token=${access_token}";

    /**
     * 查询菜单
     */
    private String           getMenusUrlPattern    = "https://api.weixin.qq.com/cgi-bin/menu/get?access_token=${access_token}";

    @Resource(name = "defaultWeixinApiService")
    private WeixinApiService weixinApiService;

    // @Autowired
    // private AccessTokenService accessTokenService;

    @Resource(name = "messageVelocityEngine")
    private VelocityEngine   velocityEngine;

    private ExecutorService  executorService;

    @Autowired
    private MenuManager      menuManager;

    @PostConstruct
    public void init() {
        if (this.executorService == null) {
            this.executorService = Executors.newSingleThreadExecutor();
        }
    }

    @Override
    public Result<Object> createMenus(List<Menu> menus) {
        if (log.isDebugEnabled()) {
            log.debug("invoke createMenus, menus=" + menus);
        }

        Result<Object> result = new Result<Object>();

        if (menus == null || menus.isEmpty()) {
            result.setError("1001", "菜单数据不能为空.");
            return result;
        }

        if (menus.size() > 3) {
            result.setError("1002", "一级菜单个数不能超过3个.");
            return result;
        }

        // 检查菜单是否有效
        for (Menu menu : menus) {
            if (!menu.isFunction()) { // 非功能菜单必须包含子菜单
                if (!menu.hasSubMenus()) {
                    result.setError("1003", "非功能菜单必须包含子菜单.");
                    return result;
                } else if (menu.subCount() > 5) {
                    result.setError("1004", "子菜单个数不能超过5个.");
                    return result;
                }

            }
        }

        String createMenuUrl = this.createMenusUrlPattern;// MessageFormat.format(createMenuUrlPattern,
                                                          // accessTokenService.getAccessToken());

        Map<String, Object> params = new HashMap<String, Object>(1);
        params.put("menus", menus);

        String postParams = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, createMenusTemplate, params);

        Result<JSONObject> apiResult = weixinApiService.doPost(createMenuUrl, postParams);

        if (!apiResult.isSuccess()) {
            result.setError(apiResult.getResultCode(), apiResult.getResultInfo());
            return result;
        }

        // 刷新本地菜单缓存
        // executorService.submit(new Runnable() {
        //
        // @Override
        // public void run() {
        //
        // try {
        // menuManager.refresh();
        // } catch (Exception e) {
        // log.error("refresh menu cache error", e);
        // }
        //
        // }
        // });

        if (log.isDebugEnabled()) {
            log.debug("invoke createMenus successfully, menus=" + menus);
        }

        result.setSuccess(true);
        return result;
    }

    @Override
    public Result<Object> removeMenus() {

        if (log.isDebugEnabled()) {
            log.debug("invoke removeMenus");
        }

        Result<Object> result = new Result<Object>();

        String removeMenuUrl = this.removeMenusUrlPattern;

        Result<JSONObject> apiResult = weixinApiService.doGet(removeMenuUrl);

        if (!apiResult.isSuccess()) {
            result.setError(apiResult.getResultCode(), apiResult.getResultInfo());
            return result;
        }

        // 刷新本地菜单缓存
        // executorService.submit(new Runnable() {
        //
        // @Override
        // public void run() {
        //
        // try {
        // menuManager.refresh();
        // } catch (Exception e) {
        // log.error("refresh menu cache error", e);
        // }
        //
        // }
        // });

        if (log.isDebugEnabled()) {
            log.debug("invoke removeMenus successfully");
        }

        result.setSuccess(true);
        return result;
    }

    @Override
    public Result<List<Menu>> getMenus() {
        // Result<List<Menu>> result = new Result<List<Menu>>();
        //
        // String getMenusUrl = this.getMenusUrlPattern;
        //
        // Result<JSONObject> apiResult = apiService.doGet(getMenusUrl);
        //
        // if (!apiResult.isSuccess()) {
        // result.setError(apiResult.getResultCode(), apiResult.getResultInfo());
        // return result;
        // }
        //
        // result.setSuccess(true);
        // return result;
        throw new UnsupportedOperationException("unsupport this operation of getMenus()");
    }

    @Override
    public Result<Object> refreshMenus() {
        Result<Object> result = new Result<Object>();

        menuManager.refresh();

        result.setSuccess(true);
        return result;
    }

    @Override
    public Result<Object> updateMenu(Menu menu) {
        throw new UnsupportedOperationException("unsupport this operation of updateMenu()");
    }

    @Override
    public Result<Object> removeMenu(String menuId) {
        throw new UnsupportedOperationException("unsupport this operation of removeMenu()");
    }

    @Override
    public Result<Object> createMenu(Menu menu) {
        throw new UnsupportedOperationException("unsupport this operation of createMenu()");
    }

    @Override
    public Result<Menu> getMenu(String menuId) {
        throw new UnsupportedOperationException("unsupport this operation of getMenu()");
    }

    public void setCreateMenuUrlPattern(String createMenuUrlPattern) {
        this.createMenusUrlPattern = createMenuUrlPattern;
    }

    public void setCreateMenusTemplate(String createMenusTemplate) {
        this.createMenusTemplate = createMenusTemplate;
    }

    // public void setAccessTokenService(AccessTokenService accessTokenService) {
    // this.accessTokenService = accessTokenService;
    // }

    public void setVelocityEngine(VelocityEngine velocityEngine) {
        this.velocityEngine = velocityEngine;
    }

    public void setCreateMenusUrlPattern(String createMenusUrlPattern) {
        this.createMenusUrlPattern = createMenusUrlPattern;
    }

    public void setRemoveMenusUrlPattern(String removeMenusUrlPattern) {
        this.removeMenusUrlPattern = removeMenusUrlPattern;
    }

    public void setGetMenusUrlPattern(String getMenusUrlPattern) {
        this.getMenusUrlPattern = getMenusUrlPattern;
    }

    public void setExecutorService(ExecutorService executorService) {
        this.executorService = executorService;
    }

    public void setMenuManager(MenuManager menuManager) {
        this.menuManager = menuManager;
    }

    public void setWeixinApiService(WeixinApiService weixinApiService) {
        this.weixinApiService = weixinApiService;
    }

}
