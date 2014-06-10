package com.kongur.monolith.weixin.core.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.kongur.monolith.common.DomainBase;
import com.kongur.monolith.lang.StringUtil;
import com.kongur.monolith.weixin.common.domain.enums.EnumMenuType;

/**
 * 菜单对象
 * 
 * @author zhengwei
 * @date 2014-2-19
 */
public class MenuDO extends DomainBase {

    /**
     * 
     */
    private static final long serialVersionUID = 2098017289149628095L;

    /**
     * 菜单ID
     */
    private String            id;

    /**
     * 父菜单ID
     */
    private String            parentId;

    /**
     * 菜单名称
     */
    private String            name;

    /**
     * 是否功能菜单
     */
    private boolean           function         = false;

    /**
     * 菜单等级
     */
    private int               level;

    /**
     * 菜单类型
     */
    private String            type;

    /**
     * 菜单url 当type=view时用到
     */
    private String            url;

    /**
     * 事件key，当type=click时用到
     */
    private String            eventKey;

    /**
     * 回复消息ID
     */
    private String            replyId;

    private ReplyDO           reply;

    /**
     * 子菜单
     */
    private List<MenuDO>      subMenus;

    /**
     * 保存上一次状态
     */
    private int               oldStatus        = -1;

    /**
     * 菜单状态, 1=刚创建 6=已推送
     */
    private int               status           = STATUS_CREATE;

    /**
     * 新创建
     */
    public static final int   STATUS_CREATE    = 1;

    /**
     * 已推送
     */
    public static final int   STATUS_PUSHED    = 6;

    private Date              gmtCreate;

    private Date              gmtModify;

    /**
     * 是否被锁定, 0:没锁 1：被锁
     */
    private int               locked           = 0;

    public boolean hasErrors() {
        return this.reply.hasErrors();
    }

    public int getLocked() {
        return locked;
    }

    public void setLocked(int locked) {
        this.locked = locked;
    }

    /**
     * 是否被锁
     * 
     * @return
     */
    public boolean isLocked() {
        return 1 == this.locked;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Date getGmtModify() {
        return gmtModify;
    }

    public void setGmtModify(Date gmtModify) {
        this.gmtModify = gmtModify;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.oldStatus = this.status; // 先保存
        this.status = status;
        this.lock();
    }

    public void lock() {
        this.locked = 1;
    }

    public void unlock() {
        this.locked = 0;
    }

    /**
     * 回滚状态
     */
    public void reset() {
        int temp = this.oldStatus;
        this.oldStatus = this.status;

        this.status = temp;

        unlock();
    }

    public ReplyDO getReply() {
        return reply;
    }

    public void setReply(ReplyDO reply) {
        this.reply = reply;
    }

    /**
     * 子菜单数量
     * 
     * @return
     */
    public int subCount() {
        return subMenus != null ? this.subMenus.size() : 0;
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

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getReplyId() {
        return replyId;
    }

    public void setReplyId(String replyId) {
        this.replyId = replyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isFunction() {
        return function;
    }

    public void setFunction(boolean function) {
        this.function = function;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getEventKey() {
        return eventKey;
    }

    public void setEventKey(String eventKey) {
        this.eventKey = eventKey;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<MenuDO> getSubMenus() {
        return subMenus;
    }

    public void setSubMenus(List<MenuDO> subMenus) {
        this.subMenus = subMenus;
    }

    public int getOldStatus() {
        return oldStatus;
    }

    public void setOldStatus(int oldStatus) {
        this.oldStatus = oldStatus;
    }

    /**
     * 状态是否变更
     * 
     * @return
     */
    // public boolean hasUpdated() {
    // return this.oldStatus != -1;
    // }

    /**
     * 复制基本信息
     * 
     * @param newMenu
     */
    public void copyFrom(MenuDO newMenu) {

        this.name = newMenu.name;
        this.type = newMenu.type;
        this.function = newMenu.function;
        this.eventKey = newMenu.eventKey;
        this.replyId = newMenu.replyId;
        this.reply = newMenu.reply;
        this.url = newMenu.url;
        this.gmtModify = newMenu.gmtModify;
    }

    /**
     * 是否一级菜单
     * 
     * @return
     */
    public boolean isLevelOne() {
        return StringUtil.isBlank(this.parentId);
    }

    public void addSubMenu(MenuDO newMenu) {
        if (this.subMenus == null) {
            this.subMenus = new ArrayList<MenuDO>();
        }

        this.subMenus.add(newMenu);

    }

    public boolean hasSubMenus() {
        return subCount() > 0;
    }

    /**
     * 是否已推送
     * 
     * @return
     */
    public boolean isPushed() {
        return STATUS_PUSHED == this.status;
    }

    /**
     * 是否文本回复类型
     * 
     * @return
     */
    public boolean isTextReply() {
        return this.reply != null && this.reply.isText();
    }

    /**
     * 图文回复消息
     * 
     * @return
     */
    public boolean isNewsReply() {
        return this.reply != null && this.reply.isNews();
    }

    /**
     * 是否资源回复类型
     * 
     * @return
     */
    public boolean isResourceReply() {
        return this.reply != null && this.reply.isResource();
    }

    /**
     * 是否包含父菜单
     * 
     * @return
     */
    public boolean hasParent() {
        return !StringUtil.isBlank(this.parentId);
    }

}
