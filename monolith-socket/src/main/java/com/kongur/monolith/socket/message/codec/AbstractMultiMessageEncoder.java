package com.kongur.monolith.socket.message.codec;

import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CharsetEncoder;
import java.util.List;

import com.kongur.monolith.socket.Constants;
import com.kongur.monolith.socket.message.DownstreamMessage;
import com.kongur.monolith.socket.message.DownstreamMessageSet;

/**
 * 当报文体为多条数据时使用
 * 
 * @author zhengwei
 * @param <DM>
 */
public abstract class AbstractMultiMessageEncoder<DM extends DownstreamMessage> extends AbstractMessageEncoder<DownstreamMessageSet<DM>> {

    @Override
    protected ByteBuffer doEncode(DownstreamMessageSet<DM> dsoSet, CharsetEncoder encoder)
                                                                                          throws CharacterCodingException {

        List<DM> downstreamMessageList = dsoSet.getDownstreamMessageList();
        if (downstreamMessageList.isEmpty()) {
            return null;
        }

        ByteBuffer buffer = allocateBuffer();

        for (int i = 0, len = downstreamMessageList.size(); i < len; i++) {
            DM dso = downstreamMessageList.get(i);
            ByteBuffer entryBuf = allocateBuffer();

            if (entryBuf.hasRemaining()) {
                encodeOne(dso, entryBuf, encoder);
                entryBuf.flip();
                buffer.put(entryBuf);
            }
            if (i < len - 1) {
                buffer.put(Constants.DEFAULT_NEW_BREAK_BYTE);
            }
        }

        buffer.flip();
        return buffer;
    }

    /**
     * 编码单条记录
     * 
     * @param dso
     * @param entryBuf
     * @param encoder
     */
    protected abstract void encodeOne(DM dso, ByteBuffer entryBuf, CharsetEncoder encoder);

}
