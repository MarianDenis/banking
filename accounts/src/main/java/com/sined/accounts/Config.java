package com.sined.accounts;

import com.sined.accounts.dto.AccountsInfoDto;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

    @Bean
    public CommandLineRunner commandLineRunner(AccountsInfoDto accountsInfoDto) {
        return args -> {
            System.out.println(accountsInfoDto);
        };
    }
}
