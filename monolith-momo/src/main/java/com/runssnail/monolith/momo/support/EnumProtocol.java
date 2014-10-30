package com.runssnail.monolith.momo.support;

/**
 * –≠“È
 * 
 * @author zhengwei
 */
public enum EnumProtocol {

    HESSIAN("hessian"), XFIRE("xfire");

    private String name;

    private EnumProtocol(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static boolean isHessian(String protocol) {
        return HESSIAN.getName().equalsIgnoreCase(protocol);
    }

}
