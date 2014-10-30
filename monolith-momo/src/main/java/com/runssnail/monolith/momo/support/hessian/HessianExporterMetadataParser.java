package com.runssnail.monolith.momo.support.hessian;

import com.runssnail.monolith.momo.support.DefaultExporterMetadata;
import com.runssnail.monolith.momo.support.DefaultExporterMetadataParser;
import com.runssnail.monolith.momo.support.ExporterMetadataParser;

/**
 * hessian exporter 元数据解析器
 * 
 * @author zhengwei
 */
public class HessianExporterMetadataParser extends DefaultExporterMetadataParser {

    private static final HessianExporterMetadataParser INSTANCE = new HessianExporterMetadataParser();

    @Override
    protected DefaultExporterMetadata createExporterMetadata() {
        return new HessianExporterMetadata();
    }

    public static ExporterMetadataParser getInstance() {
        return INSTANCE;
    }

}
