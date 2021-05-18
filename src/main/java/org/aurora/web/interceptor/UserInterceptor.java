/*
 * Copyright 2012-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.aurora.web.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.aurora.utils.UserContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Nullable;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 用户信息拦截器
 *
 * @author Evan Zhai
 * @since 1.0.0
 */
public class UserInterceptor implements HandlerInterceptor {

    /**
     * 服务间调用token用户信息,格式为json
     * {
     * "user_name":"必须有"
     * "自定义key:"value"
     * }
     */
    public static final String X_CLIENT_TOKEN_USER = "x-client-token-user";
    /**
     * 服务间调用的认证token
     */
    public static final String X_CLIENT_TOKEN = "x-client-token";
    private static final Logger log = LoggerFactory.getLogger(UserInterceptor.class);

    @Override
    @SuppressWarnings("unchecked")
    public boolean preHandle(HttpServletRequest request, @Nullable HttpServletResponse response,
                             @Nullable Object handler) throws Exception {
        //从网关获取并校验,通过校验就可信任x-client-token-user中的信息
        checkToken(request.getHeader(X_CLIENT_TOKEN));
        String userInfoString = StringUtils.defaultIfBlank(request.getHeader(X_CLIENT_TOKEN_USER), "{}");
        UserContextHolder.getInstance().setContext(new ObjectMapper().readValue(userInfoString, Map.class));
        return true;
    }

    private void checkToken(String token) {
        // TODO 从网关获取并校验,通过校验就可信任x-client-token-user中的信息
        log.debug("校验token:{}", token);
    }

    @Override
    public void afterCompletion(@Nullable HttpServletRequest request, @Nullable HttpServletResponse response,
                                @Nullable Object handler, @Nullable Exception ex) throws Exception {
        UserContextHolder.getInstance().clear();
    }
}
