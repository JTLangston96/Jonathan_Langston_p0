package dev.langst.servicestests;

import dev.langst.entities.Account;
import dev.langst.entities.AccountDAOPostgres;
import dev.langst.entities.Customer;
import dev.langst.entities.CustomerDAOPostgres;
import dev.langst.services.AccountService;
import dev.langst.services.AccountServiceImpl;
import dev.langst.services.CustomerService;
import dev.langst.services.CustomerServiceImpl;
import dev.langst.utilities.List;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AccountServiceTests {

    AccountService accountService = new AccountServiceImpl(new AccountDAOPostgres());
    CustomerService customerService = new CustomerServiceImpl(new CustomerDAOPostgres());
    static Account account;
    static  Customer customer;

    @Test
    @Order(1)
    void test_open_account(){

        //Accounts cannot be created without first being linked to a user
        String randomUserName = RandomStringUtils.randomAlphanumeric(8);
        customer = new Customer(0, "Zeus", "Beard", randomUserName , "pass");
        customer = customerService.registerUser(customer);

        account = new Account(0, customer.getId(), "Checking", 25.23);
        account = accountService.openAccount(account);
        Assertions.assertNotEquals(0, account.getAccountId());
    }

    @Test
    @Order(2)
    void test_update_account_balance(){
        double deposit = 10.23;
        double oldBalance = account.getBalance();
        accountService.adjustBalance(account, deposit);
        Assertions.assertEquals(oldBalance + deposit, account.getBalance());
    }

    @Test
    @Order(3)
    void test_get_accounts_userId(){
        List<Account> accounts = accountService.retrieveAccounts(customer.getId());

        for(int i = 0; i < accounts.size(); i++){
            Assertions.assertEquals(customer.getId(), accounts.get(i).getUserId());
        }
    }

    @Test
    @Order(4)
    void test_close_account(){

        boolean result = accountService.closeAccount(account);
        Assertions.assertTrue(result);
    }
}
