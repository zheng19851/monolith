package com.runssnail.monolith.momo.support;

import com.runssnail.monolith.momo.support.hessian.HessianExporterMetadataParser;

/**
 * ExporterMetadataParser 简单创建工厂
 * 
 * @author zhengwei
 */
public class ExporterMetadataParserFactory {

    public static ExporterMetadataParser getExporterMetadataParser(String protocol) {
        ExporterMetadataParser parser = null;
        if (EnumProtocol.isHessian(protocol)) {
            parser = HessianExporterMetadataParser.getInstance();
        } else if (EnumProtocol.isXfire(protocol) || EnumProtocol.isHttpInvoker(protocol)) {
            parser = DefaultExporterMetadataParser.getInstance();
        } else {
            throw new RuntimeException("can not find 'ExporterMetadataParser', protocol=" + protocol);
        }

        return parser;
    }
}
