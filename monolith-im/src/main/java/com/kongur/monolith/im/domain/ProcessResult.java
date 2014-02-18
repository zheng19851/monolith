package com.kongur.monolith.im.domain;

import com.kongur.monolith.common.result.Result;

/**
 * 消息处理结果
 * 
 * @author zhengwei
 * @date 2014-2-14
 */
public class ProcessResult extends Result {

    /**
     * 
     */
    private static final long serialVersionUID = 4383547911770751385L;

    
    /**
     * 消息处理返回的数据
     */
    private String            data;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

}
