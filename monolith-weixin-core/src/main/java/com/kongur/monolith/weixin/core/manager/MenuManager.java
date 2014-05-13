package com.kongur.monolith.weixin.core.manager;

import com.kongur.monolith.weixin.core.domain.MenuDO;
import com.kongur.monolith.weixin.core.domain.MenusDO;
import com.kongur.monolith.weixin.core.domain.ReplyDO;

/**
 * 菜单管理服务
 * 
 * @author zhengwei
 * @date 2014-2-19
 */
public interface MenuManager {

    void refresh();
    
    
    /**
     * 获取所有菜单
     * 
     * @return
     */
    MenusDO getMenus();

    /**
     * 菜单详情
     * 
     * @param menuId 菜单Id
     * @return
     */
    MenuDO getMenu(String menuId);

   

    /**
     * 根据事件KEY获取回复
     * 
     * @param eventKey
     * @return
     */
    ReplyDO getReplyByEventKey(String eventKey);

    /**
     * 是否包含当前eventKey
     * 
     * @param eventKey
     * @return
     */
    boolean containsEventKey(String eventKey);

}
