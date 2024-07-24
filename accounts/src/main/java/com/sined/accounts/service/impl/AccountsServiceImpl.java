package com.sined.accounts.service.impl;

import com.sined.accounts.constants.AccountsConstants;
import com.sined.accounts.dto.AccountsDto;
import com.sined.accounts.dto.CustomerDto;
import com.sined.accounts.entity.Accounts;
import com.sined.accounts.entity.Customer;
import com.sined.accounts.exception.CustomerAlreadyExistsException;
import com.sined.accounts.exception.ResourceNotFoundException;
import com.sined.accounts.mapper.AccountsMapper;
import com.sined.accounts.mapper.CustomerMapper;
import com.sined.accounts.repository.AccountsRepository;
import com.sined.accounts.repository.CustomerRepository;
import com.sined.accounts.service.IAccountsService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor
public class AccountsServiceImpl implements IAccountsService {

    AccountsRepository accountsRepository;
    CustomerRepository customerRepository;


    @Override
    public void createAccount(CustomerDto customerDto) {
        Optional<Customer> optionalCustomer = customerRepository.findByMobileNumber(customerDto.getMobileNumber());
        if (optionalCustomer.isPresent()) {
            throw new CustomerAlreadyExistsException
                    ("Customer with mobile number " + customerDto.getMobileNumber() + " alreadyExists!");
        }

        Customer customer = CustomerMapper.mapToCustomer(customerDto, new Customer());
        Customer savedCustomer = customerRepository.save(customer);
        Accounts accounts = createAccounts(savedCustomer);
        accountsRepository.save(accounts);
    }

    @Override
    public CustomerDto fetchAccountDetails(String mobileNumber) {
        Optional<Customer> optionalCustomer = customerRepository.findByMobileNumber(mobileNumber);
        if (optionalCustomer.isEmpty()) {
            throw new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber);
        }
        Optional<Accounts> optionalAccount =
                accountsRepository.findByCustomerId(optionalCustomer.get().getCustomerId());
        if (optionalAccount.isEmpty()) {
            throw new ResourceNotFoundException("Accounts", "customerId",
                    optionalCustomer.get().getCustomerId().toString());
        }
        CustomerDto customerDto = CustomerMapper.mapToCustomerDto(optionalCustomer.get(), new CustomerDto());
        customerDto.setAccountsDto(AccountsMapper.mapToAccountsDto(optionalAccount.get(), new AccountsDto()));
        return customerDto;
    }

    @Override
    public boolean updateAccount(CustomerDto customerDto) {
        boolean isUpdated = false;
        AccountsDto accountsDto = customerDto.getAccountsDto();
        if (accountsDto != null) {
            Accounts accounts = accountsRepository.findById(accountsDto.getAccountNumber()).orElseThrow(
                    () -> new ResourceNotFoundException("Accounts", "accountNumber",
                            accountsDto.getAccountNumber().toString())

            );
            AccountsMapper.mapToAccounts(accountsDto, accounts);
            accountsRepository.save(accounts);

            Long customerId = accounts.getCustomerId();
            Customer customer = customerRepository.findById(customerId).orElseThrow(
                    () -> new ResourceNotFoundException("Customer", "customerId", customerId.toString())
            );
            CustomerMapper.mapToCustomer(customerDto, customer);
            customerRepository.save(customer);
            isUpdated = true;
        }
        return isUpdated;
    }

    @Override
    @Transactional
    public boolean deleteAccount(String mobileNumber) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(() ->
                new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber)
        );
        customerRepository.deleteById(customer.getCustomerId());
        accountsRepository.deleteByCustomerId(customer.getCustomerId());
        return true;
    }

    private Accounts createAccounts(Customer customer) {
        Accounts newAccount = new Accounts();
        newAccount.setCustomerId(customer.getCustomerId());
        newAccount.setAccountNumber(1_000_000_000L + new Random().nextInt(900_000_000));
        newAccount.setAccountType(AccountsConstants.SAVINGS);
        newAccount.setBranchAddress(AccountsConstants.ADDRESS);
        return newAccount;
    }
}
