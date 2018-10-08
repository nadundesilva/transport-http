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
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;

import java.nio.charset.Charset;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * Header adjustment handler to adjust the tracing headers for Ballerina Observability.
 */
public class HeaderAdjustmentInboundHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        HttpHeaders headers = null;
        if (msg instanceof HttpRequest) {
            HttpRequest httpRequest = (HttpRequest) msg;
            headers = httpRequest.headers();
        } else if (msg instanceof HttpResponse) {
            HttpResponse httpResponse = (HttpResponse) msg;
            headers = httpResponse.headers();
        }

        if (headers != null) {
            String[] tracingHeaderKeys = new String[]{
                    Constants.ZIPKIN_B3_TRACE_ID_HEADER,
                    Constants.ZIPKIN_B3_SPAN_ID_HEADER,
                    Constants.ZIPKIN_B3_PARENT_SPAN_ID,
                    Constants.ZIPKIN_B3_SAMPLED_HEADER,
                    Constants.ZIPKIN_B3_FLAGS_HEADER
            };
            Map<String, String> tracingHeadersMap = new HashMap<>();
            for (String tracingHeaderKey : tracingHeaderKeys) {
                String headerValue = headers.get(tracingHeaderKey);
                if (headerValue != null) {
                    tracingHeadersMap.put(tracingHeaderKey, headerValue);
                }
            }

            String tracingHeaderMapString = tracingHeadersMap.toString();
            String ballerinaTracingHeader = tracingHeaderMapString.substring(1, tracingHeaderMapString.length() - 1);
            headers.add(
                    Constants.BALLERINA_TRACING_HEADER,
                    new String(
                            Base64.getEncoder().encode(ballerinaTracingHeader.getBytes(Charset.defaultCharset())),
                            Charset.defaultCharset()
                    )
            );
        }
        ctx.fireChannelRead(msg);
    }
}
