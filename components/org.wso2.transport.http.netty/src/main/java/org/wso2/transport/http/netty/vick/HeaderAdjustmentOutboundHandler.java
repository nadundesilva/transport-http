/*
 * Copyright (c) 2018, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.transport.http.netty.vick;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;

import java.nio.charset.Charset;
import java.util.Base64;

/**
 * Header adjustment handler to adjust the tracing headers for Ballerina Observability.
 */
public class HeaderAdjustmentOutboundHandler extends ChannelOutboundHandlerAdapter {

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        HttpHeaders headers = null;
        if (msg instanceof HttpRequest) {
            HttpRequest httpRequest = (HttpRequest) msg;
            headers = httpRequest.headers();
        } else if (msg instanceof HttpResponse) {
            HttpResponse httpResponse = (HttpResponse) msg;
            headers = httpResponse.headers();
        }

        if (headers != null) {
            String ballerinaTracingHeader = headers.get(Constants.BALLERINA_TRACING_HEADER);

            if (ballerinaTracingHeader != null) {
                String ballerinaTracingContext = new String(
                        Base64.getDecoder().decode(ballerinaTracingHeader),
                        Charset.defaultCharset()
                );

                for (String keyValueHeader : ballerinaTracingContext.split(",")) {
                    String[] splitKeyValueHeader = keyValueHeader.split("=");
                    headers.add(splitKeyValueHeader[0], splitKeyValueHeader[1]);
                }
            }
        }
        ctx.write(msg, promise);
    }
}
