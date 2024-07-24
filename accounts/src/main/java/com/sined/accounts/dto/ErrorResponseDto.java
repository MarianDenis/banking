package com.sined.accounts.dto;


import com.sined.accounts.constants.AccountsConstants;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Schema(
        title = "ErrorResponse",
        description = "Error response"
)
@Data
@AllArgsConstructor
public class ErrorResponseDto {

    @Schema(
            description = "Path that caused the error", example = "api/account"
    )
    private String apiPath;


    @Schema(
            description = "Status code", example = AccountsConstants.STATUS_400
    )
    private HttpStatus statusCode;


    @Schema(
            description = "Error message code", example = AccountsConstants.MESSAGE_400
    )
    private String errorMsg;


    @Schema(
            description = "error time", example = "21.03.2024T21:00:00"
    )
    private LocalDateTime errorTime;
}
