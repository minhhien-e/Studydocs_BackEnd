package com.studydocs.review.domain.exception;

/**
 * Exception khi người dùng không có quyền thực hiện hành động.
 */
public class UnauthorizedException extends BaseException {
    public UnauthorizedException() {
        super("R403", "Bạn không có quyền thực hiện hành động này", 403);
    }
}
