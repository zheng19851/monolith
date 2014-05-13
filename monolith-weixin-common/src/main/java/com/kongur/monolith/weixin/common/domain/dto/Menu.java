package com.kongur.monolith.weixin.common.domain.dto;

import java.util.List;

import com.kongur.monolith.common.DomainBase;
import com.kongur.monolith.lang.StringUtil;
import com.kongur.monolith.weixin.common.domain.enums.EnumMenuType;

/**
 * 菜单对象，内部接口调用用到
 * 
 * @author zhengwei
 * @date 2014年2月25日
 */
public class Menu extends DomainBase {

    /**
     * 
     */
    private static final long serialVersionUID = -9131049689494154312L;

    /**
     * 名称
     */
    private String            name;

    /**
     * 类型
     */
    private String            type;

    /**
     * 事件key
     */
    private String            eventKey;

    /**
     * url
     */
    private String            url;

    /**
     * 子菜单
     */
    private List<Menu>        subMenus;

    /**
     * 是否功能菜单
     * 
     * @return
     */
    public boolean isFunction() {
        return !StringUtil.isBlank(this.type);
    }

    /**
     * view链接类型的菜单
     * 
     * @return
     */
    public boolean isView() {
        return EnumMenuType.isView(this.type);
    }

    /**
     * 是否click类型
     * 
     * @return
     */
    public boolean isClick() {
        return EnumMenuType.isClick(this.type);
    }

    public boolean hasSubMenus() {
        return subCount() > 0;
    }

    /**
     * 子菜单数量
     * 
     * @return
     */
    public int subCount() {
        return subMenus != null ? this.subMenus.size() : 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEventKey() {
        return eventKey;
    }

    public void setEventKey(String eventKey) {
        this.eventKey = eventKey;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<Menu> getSubMenus() {
        return subMenus;
    }

    public void setSubMenus(List<Menu> subMenus) {
        this.subMenus = subMenus;
    }

}
