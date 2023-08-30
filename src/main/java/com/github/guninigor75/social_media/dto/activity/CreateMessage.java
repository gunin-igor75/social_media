package com.github.guninigor75.social_media.dto.activity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Schema(description = "Request for MessagesController")
public class CreateMessage {

    @Schema(description = "content", example = "content message")
    @NotBlank(message = "Content must be not empty")
    @Size(min = 3, max = 255, message = "Length name must be 3 - 255 symbol")
    private String content;

    public CreateMessage() {
    }
}
