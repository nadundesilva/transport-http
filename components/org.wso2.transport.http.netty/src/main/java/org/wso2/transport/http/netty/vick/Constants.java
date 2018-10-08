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

/**
 * Ballerina Observability handlers related constants.
 */
public class Constants {

    public static final String BALLERINA_TRACING_HEADER = "x-b7a-trace";
    public static final String ZIPKIN_B3_TRACE_ID_HEADER = "x-b3-traceid";
    public static final String ZIPKIN_B3_SPAN_ID_HEADER = "x-b3-spanid";
    public static final String ZIPKIN_B3_PARENT_SPAN_ID = "x-b3-parentspanid";
    public static final String ZIPKIN_B3_SAMPLED_HEADER = "x-b3-sampled";
    public static final String ZIPKIN_B3_FLAGS_HEADER = "x-b3-flags";
}
