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

package org.aurora.web.exception;

import org.aurora.exception.BaseException;
import org.aurora.exception.SystemErrorType;
import org.aurora.web.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartException;

import java.util.Objects;

/**
 * web exception
 *
 * @author Evan Zhai
 * @since 1.0.0
 */
public class DefaultGlobalExceptionHandlerAdvice {

    private static final Logger log = LoggerFactory.getLogger(DefaultGlobalExceptionHandlerAdvice.class);

    @ExceptionHandler(value = {MissingServletRequestParameterException.class})
    public Result<Object> missingServletRequestParameterException(MissingServletRequestParameterException ex) {
        log.error("missing servlet request parameter exception:{}", ex.getMessage());
        return Result.fail(SystemErrorType.ARGUMENT_NOT_VALID);
    }

    @ExceptionHandler(value = {MultipartException.class})
    public Result<Object> uploadFileLimitException(MultipartException ex) {
        log.error("upload file size limit:{}", ex.getMessage());
        return Result.fail(SystemErrorType.UPLOAD_FILE_SIZE_LIMIT);
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public Result<Object> serviceException(MethodArgumentNotValidException ex) {
        log.error("service exception:{}", ex.getMessage());
        return Result.fail(SystemErrorType.ARGUMENT_NOT_VALID,
                Objects.requireNonNull(ex.getBindingResult().getFieldError()).getDefaultMessage());
    }

    @ExceptionHandler(value = {DuplicateKeyException.class})
    public Result<Object> duplicateKeyException(DuplicateKeyException ex) {
        log.error("primary key duplication exception:{}", ex.getMessage());
        return Result.fail(SystemErrorType.DUPLICATE_PRIMARY_KEY);
    }

    @ExceptionHandler(value = {BaseException.class})
    public Result<Object> baseException(BaseException ex) {
        log.error("base exception:{}", ex.getMessage());
        return Result.fail(ex.getErrorType());
    }

    @ExceptionHandler(value = {Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<Object> exception() {
        return Result.fail();
    }

    @ExceptionHandler(value = {Throwable.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<Object> throwable() {
        return Result.fail();
    }
}
