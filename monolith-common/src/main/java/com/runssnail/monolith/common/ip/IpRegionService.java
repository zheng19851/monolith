package com.runssnail.monolith.common.ip;

import com.runssnail.monolith.common.result.Result;

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
