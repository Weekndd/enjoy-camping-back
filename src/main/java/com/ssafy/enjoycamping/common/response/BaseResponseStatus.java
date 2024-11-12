package com.ssafy.enjoycamping.common.response;


import org.springframework.http.HttpStatus;

public enum BaseResponseStatus {
    SUCCESS(true, HttpStatus.OK.value(), "요청에 성공하였습니다."),
    NOT_FOUND(false, HttpStatus.NOT_FOUND.value(), "존재하지 않는 페이지입니다."),
    BODY_PARSE_ERROR(false, HttpStatus.BAD_REQUEST.value(), "본문 데이터를 읽는데 실패하였습니다. 본문을 올바르게 작성했는지 확인해주세요."),
    INVALID_PARAMETER(false, HttpStatus.BAD_REQUEST.value(), "잘못된 파라미터가 전달되었습니다."),
    INPUT_PARSE_ERROR(false, HttpStatus.BAD_REQUEST.value(), "입력된 데이터가 잘못되었습니다. 입력값을 확인해주세요."),
    RESPONSE_ERROR(false, HttpStatus.NOT_FOUND.value(), "값을 불러오는데 실패하였습니다."),
    EMPTY_JWT(false, HttpStatus.UNAUTHORIZED.value(), "JWT를 입력해주세요."),
    NOT_AUTHORIZED(false, HttpStatus.UNAUTHORIZED.value(), "로그인이 필요합니다."),
    INVALID_JWT(false, HttpStatus.UNAUTHORIZED.value(), "유효하지 않은 JWT입니다."),
    INVALID_USER_JWT(false, HttpStatus.FORBIDDEN.value(), "권한이 없는 유저의 접근입니다."),
    FAILED_TO_VALIDATION(false, HttpStatus.BAD_REQUEST.value(), "값 검증에 실패하였습니다."),
    ALREADY_EXIST_EMAIL(false, HttpStatus.BAD_REQUEST.value(), "이미 존재하는 이메일입니다."),
    NOT_EXIST_EMAIL(false, HttpStatus.BAD_REQUEST.value(), "존재하지 않는 이메일입니다."),
    NOT_CORRECT_PASSWORD(false, HttpStatus.BAD_REQUEST.value(), "일치하지 않는 비밀번호입니다."),
    NOT_EXIST_USER(false, HttpStatus.NOT_FOUND.value(), "존재하지 않는 유저입니다."),
    WITHDRAW_USER(false, HttpStatus.BAD_REQUEST.value(), "탈퇴한 유저입니다."),
    NOT_EXIST_CONTENTTYPE(false, HttpStatus.NOT_FOUND.value(), "존재하지 않는 컨텐츠 타입입니다."),
    NOT_EXIST_ATTRACTION(false, HttpStatus.NOT_FOUND.value(), "존재하지 않는 관광지입니다."),
    NOT_EXIST_CAMPING(false, HttpStatus.NOT_FOUND.value(), "존재하지 않는 캠핑장입니다."),
    SIDO_GUGUN_PARSE_ERROR(false, HttpStatus.BAD_REQUEST.value(),"시도 혹은 구군이 잘못되었습니다. 입력값을 확인해주세요"),
    /**
     * Review
     */
    NOT_EXIST_REVIEW(false,HttpStatus.NOT_FOUND.value(),"존재하지 않는 리뷰입니다."),
    

    /**
     * 자유롭게 에러코드 추가
     */

    DATABASE_ERROR(false, HttpStatus.INTERNAL_SERVER_ERROR.value(), "데이터베이스 연결에 실패하였습니다."),
    SERVER_ERROR(false, HttpStatus.INTERNAL_SERVER_ERROR.value(), "서버와의 연결에 실패하였습니다."),
    ENCRYPTION_ERROR(false, HttpStatus.INTERNAL_SERVER_ERROR.value(), "암호화에 실패하였습니다."),
    DECRYPTION_ERROR(false, HttpStatus.INTERNAL_SERVER_ERROR.value(), "복호화에 실패하였습니다."),
    UNEXPECTED_ERROR(false, HttpStatus.INTERNAL_SERVER_ERROR.value(), "예상치 못한 에러가 발생했습니다.")
    ;
    

    private final boolean isSuccess;
    private final int code;
    private final String message;

    private BaseResponseStatus(boolean isSuccess, int code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }

    public boolean isSuccess() {
        return this.isSuccess;
    }

    public int getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }
}
