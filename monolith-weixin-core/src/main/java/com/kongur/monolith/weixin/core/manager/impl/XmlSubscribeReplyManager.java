package com.kongur.monolith.weixin.core.manager.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.kongur.monolith.weixin.core.domain.ItemDO;
import com.kongur.monolith.weixin.core.domain.SubscribeReplyDO;
import com.kongur.monolith.weixin.core.manager.SubscribeReplyManager;
import com.thoughtworks.xstream.XStream;

/**
 * 用xml实现的订阅回复管理服务
 * 
 * @author zhengwei
 * @date 2014年2月21日
 */
@Service("xmlSubscribeReplyManager")
public class XmlSubscribeReplyManager implements SubscribeReplyManager {

    private final Logger           log           = Logger.getLogger(getClass());

    /**
     * 路径
     */
    @Value("${weixin.subscribe.reply.conf}")
    private String                 confPath;

    private File                   file;

    /**
     * xml与对象之间转换用
     */
    private XStream                xstream;

    private ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    private SubscribeReplyDO       replyCache;

    private String                 fileEncoding  = "UTF-8";

    @PostConstruct
    public void init() throws IOException {
        this.file = new File(this.confPath);

        if (!this.file.exists()) {
            this.file.createNewFile();
        }

        if (xstream == null) {
            xstream = new XStream();
            xstream.alias("reply", SubscribeReplyDO.class);
            xstream.alias("item", ItemDO.class);
        }

        refresh();
    }

    /**
     * 刷新
     */
    public void refresh() {

        if (this.file.length() <= 0) {
            log.warn("there are no subscribe reply to refresh.");
            return;
        }

        // 将XML文件数据转成java对像
        SubscribeReplyDO reply = null;
        try {

            FileInputStream in = new FileInputStream(file);
            reply = (SubscribeReplyDO) xstream.fromXML(in);
        } catch (IOException e) {
            throw new RuntimeException("refresh subscribe reply error", e);
        }

        if (reply == null) {
            return;
        }

        WriteLock writeLock = readWriteLock.writeLock();
        writeLock.lock();

        try {

            this.replyCache = reply;

        } finally {
            writeLock.unlock();
        }

        if (log.isDebugEnabled()) {
            log.debug("refresh subscribe reply successfully, reply=" + this.replyCache);
        }

    }

    public XmlSubscribeReplyManager() {
    }

    @Override
    public String create(SubscribeReplyDO reply) {

        WriteLock writeLock = readWriteLock.writeLock();
        writeLock.lock();

        try {

            this.replyCache = reply;

            storeToXml(reply);

        } finally {
            writeLock.unlock();
        }

        return reply.getId();
    }

    private void storeToXml(SubscribeReplyDO reply) {

        OutputStreamWriter out = null;
        try {
            out = new OutputStreamWriter(new FileOutputStream(file), this.fileEncoding);
        } catch (Exception e) {
            throw new RuntimeException("can not find the subscribe reply conf file, filePath=" + this.confPath, e);
        }

        xstream.toXML(reply, out);
    }

    @Override
    public SubscribeReplyDO getSubscribeReply() {
        return this.replyCache;
    }

    @Override
    public boolean update(SubscribeReplyDO reply) {
        if (reply == null) {
            return false;
        }

        // 将之前的信息提交上来的话，就清理掉
        if (reply.isResource()) {
            reply.setContent(null);
        } else {
            reply.setResourceId(null);
        }

        WriteLock writeLock = readWriteLock.writeLock();
        writeLock.lock();

        try {

            this.replyCache = reply;

            storeToXml(reply);

        } finally {
            writeLock.unlock();
        }

        return true;
    }

    @Override
    public boolean remove() {

        WriteLock writeLock = readWriteLock.writeLock();
        writeLock.lock();

        try {

            this.replyCache = null;

            clearFile();

        } finally {
            writeLock.unlock();
        }

        return true;
    }

    private void clearFile() {
        RandomAccessFile raf = null;
        try {
            raf = new RandomAccessFile(this.file, "rw");
            raf.setLength(0);
            raf.close();
        } catch (Exception e) {

            throw new RuntimeException("clear xml error", e);
        }

    }

}
