package com.github.guninigor75.social_media.exception_handler;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;

/**
 * Обертка с удобной формы восприятия исключения на фронте
 */
@Data
@Schema(description = "Обертка для ошибок")
public class ErrorResponse {

    /** Сообщение исключения*/
    @Schema(description = "сообщение")
    private String message;

    public ErrorResponse(String message) {
        this.message = message;
    }

}
