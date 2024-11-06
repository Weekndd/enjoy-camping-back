package com.ssafy.enjoycamping.test.controller;

import com.ssafy.enjoycamping.common.response.BaseResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/app/test")
@Tag(name = "1. TEST")
@Slf4j
public class TestController {

    @GetMapping
    public BaseResponse<Date> test() {
        Date date = new Date();
        return new BaseResponse<>(date);
    }
}
