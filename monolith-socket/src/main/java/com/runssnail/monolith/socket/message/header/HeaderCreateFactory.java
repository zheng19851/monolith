package com.runssnail.monolith.socket.message.header;



/**
 * 报文头创建工厂
 * 
 * @author zhengwei
 * @version 2013-10-15 下午2:11:26
 */
public interface HeaderCreateFactory {

    /**
     * 创建UpstreamHeader
     * 
     * @return
     */
    UpstreamHeader createUpstreamHeader();

    /**
     * 创建DownstreamHeader
     * 
     * @return
     */
    DownstreamHeader createDownstreamHeader();

}
