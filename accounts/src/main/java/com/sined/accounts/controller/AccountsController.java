package com.sined.accounts.controller;

import com.sined.accounts.constants.AccountsConstants;
import com.sined.accounts.dto.AccountsDto;
import com.sined.accounts.dto.AccountsInfoDto;
import com.sined.accounts.dto.CustomerDto;
import com.sined.accounts.dto.ErrorResponseDto;
import com.sined.accounts.dto.ResponseDto;
import com.sined.accounts.service.IAccountsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(
        name = "REST API for CRUD operations on accounts",
        description = "CREATE, READ, UPDATE, DELETE accounts using this API"
)
@RestController
@RequestMapping(path = "/api/accounts", produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
public class AccountsController {

    private final IAccountsService accountsService;
    @Value("${build.version}")
    private String buildVersion;
    @Autowired
    private Environment environment;

    @Autowired
    private AccountsInfoDto accountsInfoDto;

    public AccountsController(IAccountsService iAccountsService) {
        this.accountsService = iAccountsService;
    }

    @Operation(
            summary = "CREATE account",
            description = "Method that allows you to CREATE ACCOUNT"
    )
    @ApiResponse(
            responseCode = AccountsConstants.STATUS_201,
            description = AccountsConstants.MESSAGE_201
    )
    @PostMapping
    public ResponseEntity<ResponseDto> createAccount(@Valid @RequestBody CustomerDto customerDto) {
        accountsService.createAccount(customerDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto(AccountsConstants.STATUS_201, AccountsConstants.MESSAGE_201));
    }


    @Operation(
            summary = "Fetch account",
            description = "Method that allows you to Fetch ACCOUNT"
    )
    @ApiResponse(
            responseCode = AccountsConstants.STATUS_200,
            description = AccountsConstants.MESSAGE_200
    )
    @GetMapping(path = "/{mobileNumber}")
    public ResponseEntity<CustomerDto> fetchAccountDetails(
            @Pattern(regexp = "^\\d{10}$", message = "Mobile Number format not valid!")
            @PathVariable String mobileNumber) {
        CustomerDto customerDto = accountsService.fetchAccountDetails(mobileNumber);
        return new ResponseEntity<>(customerDto, HttpStatus.OK);
    }


    @Operation(
            summary = "Update account",
            description = "Method that allows you to Update ACCOUNT"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = AccountsConstants.STATUS_200,
                    description = AccountsConstants.MESSAGE_200
            ),
            @ApiResponse(
                    responseCode = AccountsConstants.STATUS_400,
                    description = AccountsConstants.MESSAGE_400,
                    content = @Content(
                            schema = @Schema(
                                    implementation = ErrorResponseDto.class
                            )
                    )
            )
    }
    )
    @PutMapping
    public ResponseEntity<ResponseDto> updateAccount(@Valid @RequestBody CustomerDto customerDto) {
        boolean isUpdated = accountsService.updateAccount(customerDto);
        if (isUpdated) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseDto(AccountsConstants.STATUS_200, AccountsConstants.MESSAGE_200));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseDto(AccountsConstants.STATUS_400, AccountsConstants.MESSAGE_400));
        }
    }


    @Operation(
            summary = "DELETE account",
            description = "Method that allows you to DELETE ACCOUNT"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = AccountsConstants.STATUS_200,
                    description = AccountsConstants.MESSAGE_200
            ),
            @ApiResponse(
                    responseCode = AccountsConstants.STATUS_500,
                    description = AccountsConstants.MESSAGE_500,
                    content = @Content(
                            schema = @Schema(
                                    implementation = ErrorResponseDto.class
                            )
                    )
            )
    }
    )
    @DeleteMapping(path = "/{mobileNumber}")
    public ResponseEntity<ResponseDto> deleteAccount(
            @Pattern(regexp = "^\\d{10}$", message = "Mobile Number format not valid!")
            @PathVariable String mobileNumber) {
        boolean isDeleted = accountsService.deleteAccount(mobileNumber);
        if (isDeleted) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseDto(AccountsConstants.STATUS_200, AccountsConstants.MESSAGE_200));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseDto(AccountsConstants.STATUS_500, AccountsConstants.MESSAGE_500));
        }
    }


    @Operation(
            summary = "Fetch build version",
            description = "Method that allows you to fetch build version of the app"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = AccountsConstants.STATUS_200,
                    description = AccountsConstants.MESSAGE_200
            ),
            @ApiResponse(
                    responseCode = AccountsConstants.STATUS_500,
                    description = AccountsConstants.MESSAGE_500,
                    content = @Content(
                            schema = @Schema(
                                    implementation = ErrorResponseDto.class
                            )
                    )
            )
    }
    )
    @GetMapping(path = "/version")
    public ResponseEntity<ResponseDto> fetchBuildVersion() {
        return ResponseEntity.ok(new ResponseDto(AccountsConstants.STATUS_200, buildVersion));
    }

    @Operation(
            summary = "Fetch env variables",
            description = "Method that allows you to fetch environment variables"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = AccountsConstants.STATUS_200,
                    description = AccountsConstants.MESSAGE_200
            ),
            @ApiResponse(
                    responseCode = AccountsConstants.STATUS_500,
                    description = AccountsConstants.MESSAGE_500,
                    content = @Content(
                            schema = @Schema(
                                    implementation = ErrorResponseDto.class
                            )
                    )
            )
    }
    )
    @GetMapping(path = "/env")
    public ResponseEntity<ResponseDto> fetchEnvironmentVariables() {
        return ResponseEntity
                .ok(new ResponseDto(AccountsConstants.STATUS_200, environment.getProperty("MAVEN_HOME")));
    }

    @GetMapping(path = "/accounts-info")
    public ResponseEntity<ResponseDto> fetchAccountsContactInfo() {
        return ResponseEntity
                .ok(new ResponseDto(AccountsConstants.STATUS_200, accountsInfoDto.toString()));
    }
}
