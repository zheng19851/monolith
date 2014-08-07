package com.kongur.monolith.common.ip;

import com.kongur.monolith.common.result.Result;

/**
 * ip地域定位服务
 * 
 * @author zhengwei
 */
public interface IpRegionService {

    /**
     * 根据ip获取位置信息
     * 
     * @param ip
     * @return
     */
    Result<IpRegionDO> getIpRegion(String ip);

}
