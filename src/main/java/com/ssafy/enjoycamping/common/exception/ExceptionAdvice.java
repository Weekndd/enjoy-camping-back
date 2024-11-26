package com.ssafy.enjoycamping.common.exception;

import com.ssafy.enjoycamping.common.response.BaseResponse;
import com.ssafy.enjoycamping.common.response.BaseResponseStatus;

import io.jsonwebtoken.ExpiredJwtException;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import static com.ssafy.enjoycamping.common.response.BaseResponseStatus.FAILED_TO_VALIDATION;

@Slf4j
@RestControllerAdvice
public class ExceptionAdvice {
    @ExceptionHandler(BadRequestException.class)
    @ApiResponse(responseCode = "400", description = "잘못된 값이 삽입되었거나, 올바르지 않은 요청", content = @Content)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public BaseResponse<BaseResponseStatus> BadRequestExceptionHandle(BadRequestException exception) {
        log.warn("BadRequestExceptionHandle has occurred. %s %s %s".formatted(exception.getMessage(), exception.getCause(), exception.getStackTrace()[0]));
        return new BaseResponse<>(exception.getStatus());
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class, IllegalArgumentException.class})
    @ApiResponse(responseCode = "400", description = "잘못된 요청 파라미터 타입", content = @Content)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public BaseResponse<BaseResponseStatus> handleTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        log.warn("MethodArgumentTypeMismatchException has occurred. Parameter: {}, Message: {}", ex.getName(), ex.getMessage());
        return new BaseResponse<>(BaseResponseStatus.INVALID_PARAMETER);
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ApiResponse(responseCode = "401", description = "인증되지 않음", content = @Content)
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public BaseResponse<BaseResponseStatus> UnauthorizedExceptionHandle(UnauthorizedException exception) {
        log.warn("UnauthorizedException has occurred. %s %s %s".formatted(exception.getMessage(), exception.getCause(), exception.getStackTrace()[0]));
        return new BaseResponse<>(exception.getStatus());
    }

    @ExceptionHandler(ForbiddenException.class)
    @ApiResponse(responseCode = "403", description = "권한 없는 리소스에 대한 접근", content = @Content)
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    public BaseResponse<BaseResponseStatus> ForbiddenExceptionHandle(ForbiddenException exception) {
        log.warn("ForbiddenException has occurred. %s %s %s".formatted(exception.getMessage(), exception.getCause(), exception.getStackTrace()[0]));
        return new BaseResponse<>(exception.getStatus());
    }

    @ExceptionHandler(NotFoundException.class)
    @ApiResponse(responseCode = "404", description = "존재하지 않는 리소스", content = @Content)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public BaseResponse<BaseResponseStatus> NotFoundExceptionExceptionHandle(NotFoundException exception) {
        log.warn("NotFoundException has occurred. %s %s %s".formatted(exception.getMessage(), exception.getCause(), exception.getStackTrace()[0]));
        return new BaseResponse<>(exception.getStatus());
    }


    @ExceptionHandler(InternalServerErrorException.class)
    @ApiResponse(responseCode = "500", description = "서버 에러", content = @Content)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public BaseResponse<BaseResponseStatus> InternalServerErrorException(InternalServerErrorException exception) {
        log.error("InternalServerErrorException has occurred. %s %s %s".formatted(exception.getMessage(), exception.getCause(), exception.getStackTrace()[0]));
        return new BaseResponse<>(exception.getStatus());
    }

    @ExceptionHandler(Exception.class)
    @ApiResponse(responseCode = "500", description = "서버 에러", content = @Content)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public BaseResponse<BaseResponseStatus> ExceptionHandle(Exception exception) {
        exception.printStackTrace();
        log.error("Exception has occurred. %s %s %s".formatted(exception.getMessage(), exception.getCause(), exception.getStackTrace()[0]));
        return new BaseResponse<>(BaseResponseStatus.UNEXPECTED_ERROR);
    }

    @ExceptionHandler(HttpClientErrorException.class)
    @ApiResponse(responseCode = "500", description = "서버 에러", content = @Content)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<?> ExceptionHandle(HttpClientErrorException exception) {
        log.error("HttpClientErrorException has occurred. %s %s %s".formatted(exception.getMessage(), exception.getCause(), exception.getStackTrace()[0]));
        return ResponseEntity.status(exception.getStatusCode())
                .body(new BaseResponse<>(BaseResponseStatus.UNEXPECTED_ERROR, exception.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ApiResponse(responseCode = "400", description = "유효성 검사 실패", content = @Content)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public BaseResponse<String> methodValidException(MethodArgumentNotValidException e, HttpServletRequest request) {
        log.warn("[{}] Validation Error Trace: {}", request.getRequestURI(), e.getStackTrace()[0]);

        BindingResult bindingResult = e.getBindingResult();

        StringBuilder errorBuilder = new StringBuilder();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            errorBuilder.append(fieldError.getDefaultMessage());
            errorBuilder.append(" ");
        }

        return new BaseResponse<>(FAILED_TO_VALIDATION, errorBuilder.toString().stripTrailing());
    }
    
    @ExceptionHandler(ExpiredJwtException.class)
    @ApiResponse(responseCode = "403", description = "토큰 기간 만료", content = @Content)
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    public BaseResponse<BaseResponseStatus> expiredJwtException() {
    	log.error("토큰 시간이 만료되었습니다.");
    	return new BaseResponse<>(BaseResponseStatus.EXPIRED_JWT);
    }
}
