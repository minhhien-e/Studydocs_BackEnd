package studydoc.response;

public record ApiResponse<T>(int statusCode, Integer errorCode, T data) {
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(200, null, data);
    }

    public static <T> ApiResponse<T> success(int statusCode, T data) {
        return new ApiResponse<>(statusCode, null, data);

    }

    public static <T> ApiResponse<T> error(int statusCode, Integer errorCode) {
        return new ApiResponse<>(statusCode, errorCode, null);
    }
}