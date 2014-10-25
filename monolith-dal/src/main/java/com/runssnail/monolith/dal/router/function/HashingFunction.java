package com.runssnail.monolith.dal.router.function;

/**
 * 
 * @author zhengwei
 *
 */
public class HashingFunction {

    public HashingFunction() {

    }

    public int apply(Object input, int mod) {
        int hashCode = input.hashCode();

        return Math.abs(hashCode % mod);
    }

}
