package com.runssnail.monolith.socket.netty.test;

import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;

import com.runssnail.monolith.socket.message.UpstreamMessage;
import com.runssnail.monolith.socket.protocol.AbstractProtocolParser;


/**
 *
 *@author zhengwei
 *
 *@date 2014-1-26	
 *
 */
public class DefaultProtocolParser extends AbstractProtocolParser {

    @Override
    protected UpstreamMessage doParse(ByteBuffer buffer) throws CharacterCodingException {
        return null;
    }

}

