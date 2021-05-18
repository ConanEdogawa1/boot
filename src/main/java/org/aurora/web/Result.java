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

package org.aurora.web;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import org.aurora.exception.BaseException;
import org.aurora.exception.ErrorType;
import org.aurora.exception.SystemErrorType;

import java.time.Instant;
import java.time.ZonedDateTime;

/**
 * web结果出来类
 *
 * @author Evan Zhai
 * @since 1.0.0
 */
@Getter
public class Result<T> {

    public static final String SUCCESSFUL_CODE = "000000";
    public static final String SUCCESSFUL_MSG = "处理成功";
    /**
     * 请求结果生成时间戳
     */
    private final Instant time;
    /**
     * 处理结果code
     */
    private String code;
    /**
     * 处理结果描述信息
     */
    private String msg;
    /**
     * 处理结果数据信息
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    public Result() {
        this.time = ZonedDateTime.now().toInstant();
    }

    /**
     * @param errorType {@link ErrorType}
     */
    public Result(ErrorType errorType) {
        this.code = errorType.getCode();
        this.msg = errorType.getMsg();
        this.time = ZonedDateTime.now().toInstant();
    }

    /**
     * @param errorType {@link ErrorType}
     * @param data      需要返还的数据
     */
    public Result(ErrorType errorType, T data) {
        this(errorType);
        this.data = data;
    }

    /**
     * 内部使用，用于构造成功的结果
     *
     * @param code 系统预定义编码
     * @param msg  消息
     * @param data 需要返还的数据
     */
    private Result(String code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
        this.time = ZonedDateTime.now().toInstant();
    }

    /**
     * 快速创建成功结果并返回结果数据
     *
     * @param data 需要返还的数据
     * @return Result
     */
    public static Result<Object> success(Object data) {
        return new Result<>(SUCCESSFUL_CODE, SUCCESSFUL_MSG, data);
    }

    /**
     * 快速创建成功结果
     *
     * @return Result
     */
    public static Result<Object> success() {
        return success(null);
    }

    /**
     * 系统异常类没有返回数据
     *
     * @return Result
     */
    public static Result<Object> fail() {
        return new Result<>(SystemErrorType.SYSTEM_ERROR);
    }

    /**
     * 系统异常类没有返回数据
     *
     * @param baseException {@link BaseException}
     * @return Result
     */
    public static Result<Object> fail(BaseException baseException) {
        return fail(baseException, null);
    }

    /**
     * 系统异常类并返回结果数据
     *
     * @param data 需要返还的数据
     * @return Result
     */
    public static Result<Object> fail(BaseException baseException, Object data) {
        return new Result<>(baseException.getErrorType(), data);
    }

    /**
     * 系统异常类并返回结果数据
     *
     * @param errorType {@link ErrorType}
     * @param data      需要返还的数据
     * @return Result
     */
    public static Result<Object> fail(ErrorType errorType, Object data) {
        return new Result<>(errorType, data);
    }

    /**
     * 系统异常类并返回结果数据
     *
     * @param errorType {@link ErrorType}
     * @return Result
     */
    public static Result<Object> fail(ErrorType errorType) {
        return Result.fail(errorType, null);
    }

    /**
     * 系统异常类并返回结果数据
     *
     * @param data 需要返还的数据
     * @return Result
     */
    public static Result<Object> fail(Object data) {
        return new Result<>(SystemErrorType.SYSTEM_ERROR, data);
    }


    /**
     * 成功code=000000
     *
     * @return true/false
     */
    @JsonIgnore
    public boolean isSuccess() {
        return SUCCESSFUL_CODE.equals(this.code);
    }

    /**
     * 失败
     *
     * @return true/false
     */
    @JsonIgnore
    public boolean isFail() {
        return !isSuccess();
    }
}
