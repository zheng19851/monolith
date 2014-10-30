package com.runssnail.monolith.momo.support;

import com.runssnail.monolith.momo.support.hessian.HessianServiceMetadataParser;

public class ServiceMetadataParserFactory {

    public static ServiceMetadataParser getServiceMetadataParser(String protocol) {
        ServiceMetadataParser parser = null;
        if (EnumProtocol.isHessian(protocol)) {
            parser = HessianServiceMetadataParser.getInstance();
        } else if (EnumProtocol.isXfire(protocol) || EnumProtocol.isHttpInvoker(protocol)) {
            parser = DefaultServiceMetadataParser.getInstance();
        } else {
            throw new RuntimeException("can not find 'ServiceMetadataParser', protocol=" + protocol);
        }

        return parser;
    }

}
