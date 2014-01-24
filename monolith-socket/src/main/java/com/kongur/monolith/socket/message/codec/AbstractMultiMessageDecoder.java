package com.kongur.monolith.socket.message.codec;

import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CharsetDecoder;

import org.apache.commons.lang.StringUtils;

import com.kongur.monolith.lang.StringUtil;
import com.kongur.monolith.socket.message.UpstreamMessageSet;
import com.kongur.monolith.socket.message.header.UpstreamHeader;

/**
 * 有可能多条记录返回的协议解码器
 * 
 * @author zhengwei
 */
public abstract class AbstractMultiMessageDecoder<USO> extends AbstractMessageDecoder<UpstreamMessageSet<USO>> {

    @Override
    public UpstreamMessageSet<USO> createUpstreamMessage(UpstreamHeader header) {
        return new UpstreamMessageSet<USO>(header);
    }

    @Override
    protected void doDecode(UpstreamMessageSet<USO> usoSet, ByteBuffer bodyBuf, CharsetDecoder decoder,
                            DecodeResult<UpstreamMessageSet<USO>> result) throws CodecException {

        try {
            String dataStr = CodecUtils.getString(bodyBuf, false);

            if (StringUtil.isEmpty(dataStr)) {
                return;
            }

            String[] lines = dataStr.split(getNewBreak(), -1);

            for (String line : lines) {
                if (StringUtils.isBlank(line)) {
                    continue;
                }

                String[] fields = line.split(getSplitChar(), -1);

                USO uso = createOneUpStreamObject();

                doDecodeOne(uso, fields, result);

                if (!result.isSuccess()) {
                    return;
                }

                usoSet.addUpstreamMessage(uso);
            }
        } catch (Exception e) {
            throw new CodecException(e);
        }

    }

    protected abstract USO createOneUpStreamObject();

    /**
     * 子类自己实现具体的解码工作
     * 
     * @param buffer
     * @return
     */
    protected abstract void doDecodeOne(USO uso, String[] fields, DecodeResult<UpstreamMessageSet<USO>> result)
                                                                                                               throws CharacterCodingException;

}
