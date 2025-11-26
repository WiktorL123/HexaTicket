package com.pjatk.web.exception;

import lombok.*;

import java.util.Map;


@RequiredArgsConstructor
@Getter
@Setter
public class ErrorResponse {
    private final Map<String, String> body;
}
