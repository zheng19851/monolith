package com.runssnail.monolith.session.utils;

import org.apache.commons.lang.StringUtils;

public class ConfigUtils {

    private static final String CONFIG_SEPARATORS = ",;";

    public static String[] splitConfig(String config) {
        return StringUtils.split(config, CONFIG_SEPARATORS);
    }



}
