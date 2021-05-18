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

package org.aurora.web.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.reflection.MetaObject;
import org.aurora.utils.UserContextHolder;

import java.time.ZonedDateTime;
import java.util.Date;

/**
 * mybatis plus 通用填充
 *
 * @author Evan Zhai
 * @since 1.0.0
 */
@Log4j2
public class PoMetaObjectHandler implements MetaObjectHandler {

    private static final String DEFAULT_USERNAME = "system";

    /**
     * 获取当前交易的用户，为空返回默认system
     *
     * @return user 如果没用获取用户，默认为 ‘system’
     */
    private String getCurrentUsername() {
        return StringUtils.defaultIfBlank(UserContextHolder.getInstance().getUsername(), DEFAULT_USERNAME);
    }

    @Override
    public void insertFill(MetaObject metaObject) {
        this.setFieldValByName("createdBy", getCurrentUsername(), metaObject);
        this.setFieldValByName("createdTime", Date.from(ZonedDateTime.now().toInstant()), metaObject);
        this.updateFill(metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("updatedBy", getCurrentUsername(), metaObject);
        this.setFieldValByName("updatedTime", Date.from(ZonedDateTime.now().toInstant()), metaObject);
    }
}
