package com.sined.accounts.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Schema(
        name = "Customer",
        description = "Customer data"
)
@Data
public class CustomerDto {

    @Schema(
            description = "Name of customer", example = "Sined"
    )
    @NotEmpty(message = "Name cannot be empty!")
    @Size(min = 2, max = 30, message = "Name must be withing 2 - 30 length!")
    private String name;


    @Schema(
            description = "Email of customer", example = "sined@example.com"
    )
    @NotEmpty(message = "Email cannot be empty!")
    @Email(message = "Email format not valid!")
    private String email;


    @Schema(
            description = "Mobile of customer", example = "0723456772"
    )
    @Pattern(regexp = "^\\d{10}$", message = "Mobile Number format not valid!")
    private String mobileNumber;


    @Schema(
            description = "Accounts of customer"
    )
    @JsonProperty("accountsDetails")
    private AccountsDto accountsDto;
}
