package com.mygolfclub.exception.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.util.Assert;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorResponse {
    private LocalDateTime timeStamp;
    private int code;
    private String status;
    private String message;
    private String path;

    public static ErrorResponseBuilder builder() {
        return new ErrorResponseBuilder();
    }

    public static class ErrorResponseBuilder {
        private LocalDateTime timeStamp;
        private int code;
        private String status;
        private String message;
        private String path;

        public ErrorResponseBuilder timeStamp(LocalDateTime timeStamp) {
            Assert.notNull(timeStamp, "timeStamp cannot be null.");
            this.timeStamp = timeStamp;
            return this;
        }

        public ErrorResponseBuilder code(int code) {
            this.code = code;
            return this;
        }

        public ErrorResponseBuilder status(String status) {
            Assert.notNull(status, "status cannot be null.");
            this.status = status;
            return this;
        }

        public ErrorResponseBuilder message(String message) {
            Assert.notNull(message, "message cannot be null.");
            this.message = message;
            return this;
        }

        public ErrorResponseBuilder path(String path) {
            Assert.notNull(path, "path cannot be null.");
            this.path = path;
            return this;
        }

        public ErrorResponse build() {
            return new ErrorResponse(
                    this.timeStamp,
                    this.code,
                    this.status,
                    this.message,
                    this.path
            );
        }
    }
}
