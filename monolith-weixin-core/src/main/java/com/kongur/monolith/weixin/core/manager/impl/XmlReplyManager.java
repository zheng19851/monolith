package com.kongur.monolith.weixin.core.manager.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Assert;

import com.kongur.monolith.common.UUIDGenerator;
import com.kongur.monolith.lang.StringUtil;
import com.kongur.monolith.weixin.core.domain.ItemDO;
import com.kongur.monolith.weixin.core.domain.ReplyDO;
import com.kongur.monolith.weixin.core.domain.ReplysDO;
import com.kongur.monolith.weixin.core.manager.ReplyManager;
import com.thoughtworks.xstream.XStream;

/**
 * 用XML实现的回复管理
 * 
 * @author zhengwei
 * @date 2014年2月20日
 */
// @Service("xmlReplyManager")
public class XmlReplyManager implements ReplyManager {

    private final Logger                      log           = Logger.getLogger(getClass());

    /**
     * 路径
     */
    @Value("${weixin.replys.conf}")
    private String                            confPath;

    private File                              file;

    /**
     * 文件存放路径
     */
    // @Value("${weixin.replys.conf}")
    // private Resource conf;

    /**
     * 所有回复记录缓存 key=replyId
     */
    private Map<String/* replyId */, ReplyDO> replysCache   = null;

    private ReentrantReadWriteLock            readWriteLock = new ReentrantReadWriteLock();

    /**
     * xml与对象之间转换用
     */
    private XStream                           xstream;

    private String                            fileEncoding  = "UTF-8";

    /**
     * 初始化
     * 
     * @throws IOException
     */
    @PostConstruct
    public void init() throws IOException {
        Assert.notNull(this.confPath, "the xml conf file of replys can not be null.");

        // this.confPath = this.conf.getFile().getAbsolutePath();

        this.file = new File(this.confPath);

        if (!this.file.exists()) {
            this.file.createNewFile();
        }

        if (xstream == null) {
            xstream = new XStream();
            xstream.alias("replys", ReplysDO.class);
            xstream.alias("reply", ReplyDO.class);
            xstream.alias("item", ItemDO.class);

            xstream.addImplicitCollection(ReplysDO.class, "replys");
        }

        refresh();

    }

    private void refresh() {

        // 将XML文件数据转成java对像
        ReplysDO replys = null;
        try {
            replys = (ReplysDO) xstream.fromXML(new FileInputStream(file));
        } catch (IOException e) {
            throw new RuntimeException("refresh replys error", e);
        }

        WriteLock writeLock = readWriteLock.writeLock();
        writeLock.lock();

        try {
            this.replysCache = new HashMap<String, ReplyDO>(replys.count());

            for (ReplyDO reply : replys.getReplys()) {
                this.replysCache.put(reply.getId(), reply);
            }
        } finally {
            writeLock.unlock();
        }

        if (log.isDebugEnabled()) {
            log.debug("refresh replys successfully, replys=" + this.replysCache);
        }

    }

    @Override
    public ReplysDO getReplys() {
        ReplysDO replys = new ReplysDO();

        ReadLock readLock = readWriteLock.readLock();

        readLock.lock();

        try {

            replys.setReplys(getReplyList());
        } finally {
            readLock.unlock();
        }

        return replys;
    }

    private List<ReplyDO> getReplyList() {

        List<ReplyDO> list = new ArrayList<ReplyDO>(this.replysCache.size());

        for (Entry<String, ReplyDO> entry : this.replysCache.entrySet()) {
            list.add(entry.getValue());
        }

        return list;
    }

    @Override
    public ReplyDO getReply(String replyId) {

        ReadLock readLock = readWriteLock.readLock();
        readLock.lock();
        ReplyDO result = null;
        try {
            result = this.replysCache.get(replyId);
        } finally {
            readLock.unlock();
        }

        return result;
    }

    @Override
    public boolean remove(String replyId) {
        WriteLock writeLock = readWriteLock.writeLock();

        writeLock.lock();

        boolean success = false;

        try {
            this.replysCache.remove(replyId);

            success = storeToXml();

        } finally {
            writeLock.unlock();
        }

        return success;
    }

    private boolean storeToXml() {
        List<ReplyDO> replyList = getReplyList();

        ReplysDO replys = new ReplysDO(replyList);

        try {
            
            OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(file), this.fileEncoding);
            xstream.toXML(replys, out);
        } catch (Exception e) {
            throw new RuntimeException("can not find the conf file of replys, file=" + this.confPath, e);
        }

        return true;

    }

    @Override
    public boolean update(ReplyDO repaly) {

        WriteLock writeLock = readWriteLock.writeLock();

        writeLock.lock();
        boolean success = false;

        try {
            this.replysCache.put(repaly.getId(), repaly);

            success = storeToXml();

        } finally {
            writeLock.unlock();
        }

        return success;
    }

    @Override
    public String addReply(ReplyDO repaly) {

        if (repaly == null) {
            return null;
        }

        String id = repaly.getId();
        if (StringUtil.isBlank(id)) {
            id = UUIDGenerator.generate();
            repaly.setId(id);
        }

        WriteLock writeLock = readWriteLock.writeLock();
        writeLock.lock();

        try {
            this.replysCache.put(id, repaly);

            storeToXml();

        } finally {
            writeLock.unlock();
        }

        return id;
    }

}
