package com.kongur.monolith.weixin.core.domain;

/**
 * 回复类型
 * 
 * @author zhengwei
 * @date 2014年2月26日
 */
public interface Reply {

    /**
     * 是否主动发送
     * 
     * @return
     */
    boolean isActive();

    /**
     * 是否文本类型
     * 
     * @return
     */
    boolean isText();

    /**
     * 类型
     * 
     * @return
     */
    String getType();

    /**
     * 资源类型
     * 
     * @return
     */
    boolean isResource();

}
