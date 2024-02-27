package com.codingbottle.docs.error;

import com.codingbottle.model.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/error")
public class ErrorController {
    @GetMapping
    public ResponseEntity<ErrorResponse> error() {
        return ResponseEntity.badRequest().body(ErrorResponse.of("오류 코드", "오류 이유"));
    }
}
