package com.kongur.monolith.im.serivce.message;

import com.kongur.monolith.im.domain.message.Message;

/**
 * 消息验证服务，可验证是否重发
 * 
 * @author zhengwei
 * @date 2014-2-14
 */
public interface MessageValidator<M extends Message> {

    boolean validate(M msg);

}
