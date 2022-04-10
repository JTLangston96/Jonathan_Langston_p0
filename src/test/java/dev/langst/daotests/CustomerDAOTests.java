package dev.langst.daotests;

import dev.langst.data.CustomerDAO;
import dev.langst.entities.Customer;
import dev.langst.entities.CustomerDAOPostgres;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CustomerDAOTests {

    CustomerDAO customerDAO = new CustomerDAOPostgres();
    static Customer customer = null;

    @Test
    @Order(1)
    void create_customer_test(){
        //username must be unique
        //chance of failing due to duplicate username is extremely low
        String randomUserName = RandomStringUtils.randomAlphanumeric(8);

        Customer zeus = new Customer(0, "Zeus", "Beard", randomUserName, "thunderZap");
        Customer savedZeus = customerDAO.createCustomer(zeus);
        CustomerDAOTests.customer = savedZeus;
        Assertions.assertNotEquals(0, savedZeus.getId());
    }

    @Test
    @Order(2)
    void find_customer_test(){

        Customer result = customerDAO.getCustomerByUsername(customer.getUserName());
        Assertions.assertEquals(customer.getId(), result.getId());
    }

    @Test
    @Order(3)
    void update_customer_test(){

        customer.setLastName("Thunderbeard");
        Customer result = customerDAO.updateCustomer(customer);
        Assertions.assertEquals("Thunderbeard", result.getLastName());
    }

    @Test
    @Order(4)
    void delete_customer_test(){
        boolean result = new CustomerDAOPostgres().deleteCustomerById(customer.getId());
        Assertions.assertTrue(result);
    }
}
