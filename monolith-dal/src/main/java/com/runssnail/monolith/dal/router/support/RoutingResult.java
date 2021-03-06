/**
 * Copyright 1999-2011 Alibaba Group Licensed under the Apache License, Version 2.0 (the "License"); you may not use
 * this file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and limitations under the
 * License.
 */
package com.runssnail.monolith.dal.router.support;

import java.util.ArrayList;
import java.util.List;

/**
 * 路由结果
 * 
 * @author zhengwei
 */
public class RoutingResult extends com.alibaba.cobar.client.router.support.RoutingResult {

    /**
     * 表名后缀
     */
    private String tableSuffix;

    public String getTableSuffix() {
        return tableSuffix;
    }

    public void setTableSuffix(String tableSuffix) {
        this.tableSuffix = tableSuffix;
    }

    public void addResourceIdentities(List<String> resourceIdentities) {
        if (super.getResourceIdentities() == null) {
            setResourceIdentities(new ArrayList<String>());
        }

        getResourceIdentities().addAll(resourceIdentities);

    }

}
