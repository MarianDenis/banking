package com.sined.accounts.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;

@Schema(
        name = "Accounts",
        description = "Accounts information"
)
@Data
public class AccountsDto {

    @Schema(
            description = "Account number", example = "1234567891"
    )
    @NotEmpty(message = "Account Number must not be empty!")
    @Pattern(regexp = "^\\d{10}$", message = "Account Number must be 10 digits!")
    private Long accountNumber;


    @Schema(
            description = "Account Type", example = "Savings"
    )
    @NotEmpty(message = "Account Type must not be empty!")
    private String accountType;



    @Schema(
            description = "Branch type", example = "New York Harlem Street 21"
    )
    @NotEmpty(message = "Branch Address must not be empty!")
    private String branchAddress;

}
