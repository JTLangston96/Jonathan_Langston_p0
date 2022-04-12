package dev.langst.daotests;

import dev.langst.data.AccountDAO;
import dev.langst.data.CustomerDAO;
import dev.langst.entities.Account;
import dev.langst.entities.AccountDAOPostgres;
import dev.langst.entities.Customer;
import dev.langst.entities.CustomerDAOPostgres;
import dev.langst.utilities.List;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AccountDAOTests {

    AccountDAO accountDAO = new AccountDAOPostgres();
    CustomerDAO customerDAO = new CustomerDAOPostgres();
    static Account account;
    static Customer customer;



    @Test
    @Order(1)
    void create_account_test(){

        //Accounts cannot be created without first being linked to a user
        String randomUserName = RandomStringUtils.randomAlphanumeric(8);
        customer = new Customer(0, "Zeus", "Beard", randomUserName , "pass");
        customer = customerDAO.createCustomer(customer);

        account = new Account(0, customer.getId(), "Checking", 25.23);
        account = accountDAO.createAccount(account);
        Assertions.assertNotEquals(0, account.getAccountId());
    }

    @Test
    @Order(2)
    void get_account_test(){

        int targetId = account.getAccountId();
        Account fetchedAccount = accountDAO.getAccountById(targetId);

        Assertions.assertEquals(targetId, fetchedAccount.getAccountId());
    }

    @Test
    @Order(3)
    void get_account_userId_test(){
        int userId = customer.getId();

        List<Account> accounts = accountDAO.getAccountsByUserId(userId);

        for(int i = 0; i < accounts.size(); i++){
            Assertions.assertEquals(userId, accounts.get(i).getUserId());
        }

    }

    @Test
    @Order(4)
    void delete_account_test(){

        boolean result = accountDAO.deleteAccountById(account.getAccountId());
        customerDAO.deleteCustomerById(customer.getId());

        Assertions.assertTrue(result);
    }
}
