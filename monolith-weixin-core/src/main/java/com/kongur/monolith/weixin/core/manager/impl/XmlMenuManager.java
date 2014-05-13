package com.kongur.monolith.weixin.core.manager.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.kongur.monolith.lang.StringUtil;
import com.kongur.monolith.weixin.core.domain.ItemDO;
import com.kongur.monolith.weixin.core.domain.MenuDO;
import com.kongur.monolith.weixin.core.domain.MenusDO;
import com.kongur.monolith.weixin.core.domain.ReplyDO;
import com.kongur.monolith.weixin.core.manager.MenuManager;
import com.thoughtworks.xstream.XStream;

/**
 * 默认用XML实现
 * 
 * @author zhengwei
 */
@Service("xmlMenuManager")
public class XmlMenuManager implements MenuManager {

    private final Logger                        log           = Logger.getLogger(getClass());

    /**
     * 路径
     */
    @Value("${weixin.menus.conf}")
    private String                              confPath;

    private File                                file;

    /**
     * 所有菜单缓存
     */
    private MenusDO                             menusCache    = new MenusDO(null);

    private Map<String /* eventKey */, ReplyDO> replysCache   = new HashMap<String, ReplyDO>();

    private Map<String /* menuId */, MenuDO>    menusMap      = new HashMap<String, MenuDO>();

    /**
     * xml与对象之间转换用
     */
    private XStream                             xstream;

    private ReentrantReadWriteLock              readWriteLock = new ReentrantReadWriteLock();

    /**
     * 初始化
     * 
     * @throws IOException
     */
    @PostConstruct
    public void init() throws IOException {
        Assert.notNull(this.confPath, "the xml conf of menus file can not be null.");

        this.file = new File(this.confPath);

        if (!this.file.exists()) {
            this.file.createNewFile();
        }

        if (xstream == null) {
            xstream = new XStream();
            xstream.alias("menus", MenusDO.class);
            xstream.alias("menu", MenuDO.class);
            xstream.alias("reply", ReplyDO.class);
            xstream.alias("item", ItemDO.class);
            xstream.addImplicitCollection(MenusDO.class, "menus");
        }

        refresh();

    }

    public void refresh() {

        if (this.file.length() <= 0) {
            log.warn("there are no menus need to refresh.");
            return;
        }

        // 将XML文件数据转成java对像
        MenusDO menus = null;
        try {
            menus = (MenusDO) xstream.fromXML(new FileInputStream(this.file));
        } catch (IOException e) {
            throw new RuntimeException("refresh menus error", e);
        }

        if (menus == null) {
            menus = new MenusDO(null);
        }

        WriteLock writeLock = readWriteLock.writeLock();

        writeLock.lock();

        try {
            // 缓存回复数据
            buildCacheData(menus);
        } finally {
            writeLock.unlock();
        }

        if (log.isDebugEnabled()) {
            log.debug("refresh menus successfully, menus=" + menus);
        }
    }

    /**
     * 组装回复缓存数据
     * 
     * @param menus
     * @return
     */
    private void buildCacheData(MenusDO menus) {

        if (menus.getMenus() == null) {
            menus.setMenus(new ArrayList<MenuDO>());
        }

        this.menusCache = menus;

        // 缓存回复数据
        if (!menus.isEmpty()) {
            Map<String, ReplyDO> replysMap = new HashMap<String, ReplyDO>();

            Map<String, MenuDO> menusMap = new HashMap<String, MenuDO>();

            for (MenuDO menu : menus.getMenus()) {
                if (menu.isClick()) {
                    // ReplyDO reply = replyManager.getReply(menu.getReplyId());
                    ReplyDO reply = menu.getReply();
                    if (reply != null) {
                        replysMap.put(menu.getEventKey(), reply);
                        menu.setReply(reply); // 设置回复
                    }
                }

                if (menu.hasSubMenus()) {
                    for (MenuDO subMenu : menu.getSubMenus()) {
                        if (subMenu.isClick()) {
                            // ReplyDO reply = replyManager.getReply(subMenu.getReplyId());
                            ReplyDO reply = subMenu.getReply();
                            if (reply != null) {
                                replysMap.put(subMenu.getEventKey(), reply);
                                subMenu.setReply(reply);// 设置回复
                            }
                        }

                        menusMap.put(subMenu.getId(), subMenu);
                    }
                }

                menusMap.put(menu.getId(), menu);

            }

            this.menusMap = menusMap;

            this.replysCache = replysMap;

        }

    }

    @Override
    public MenusDO getMenus() {
        MenusDO menus = null;
        ReadLock readLock = readWriteLock.readLock();
        readLock.lock();
        try {

            menus = this.menusCache;

        } finally {
            readLock.unlock();
        }

        return menus;
    }

    @Override
    public ReplyDO getReplyByEventKey(String eventKey) {

        ReplyDO reply = null;

        ReadLock readLock = readWriteLock.readLock();

        readLock.lock();

        try {
            reply = this.replysCache.get(eventKey);
        } finally {
            readLock.unlock();
        }

        return reply;
    }

    public String getConfPath() {
        return confPath;
    }

    public void setConfPath(String confPath) {
        this.confPath = confPath;
    }

    public static void main(String[] args) throws Exception {
        XStream xstream = new XStream();
        // xstream.alias("menus", MenusDO.class);
        // xstream.alias("menu", MenuDO.class);
        //
        // xstream.addImplicitCollection(MenusDO.class, "menus");

        xstream = new XStream();
        xstream.alias("menus", MenusDO.class);
        xstream.alias("menu", MenuDO.class);
        xstream.alias("reply", ReplyDO.class);
        xstream.alias("item", ItemDO.class);
        xstream.addImplicitCollection(MenusDO.class, "menus");

        // String conf = "D:/git_repo/monolith/monolith-im/src/main/resources/menus.xml";
        String conf = "D:/doc/skyjoo/forall/trunk/weixin/testsrc/menus.xml";

        File f = new File(conf);

        FileInputStream in = new FileInputStream(f);

        Object obj = xstream.fromXML(in);

        System.out.println(obj);

    }

    @Override
    public MenuDO getMenu(String menuId) {
        MenuDO menu = null;

        if (StringUtil.isBlank(menuId)) {
            return null;
        }

        ReadLock readLock = readWriteLock.readLock();
        readLock.lock();

        try {

            menu = this.menusMap.get(menuId);

        } finally {
            readLock.unlock();
        }

        return menu;

    }

    @Override
    public boolean containsEventKey(String eventKey) {

        ReadLock readLock = readWriteLock.readLock();
        readLock.lock();
        try {
            return this.replysCache.containsKey(eventKey);
        } finally {
            readLock.unlock();
        }
    }

}
