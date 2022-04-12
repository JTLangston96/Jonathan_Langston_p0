package dev.langst.servicestests;

import dev.langst.entities.Customer;
import dev.langst.entities.CustomerDAOPostgres;
import dev.langst.services.CustomerService;
import dev.langst.services.CustomerServiceImpl;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CustomerServiceTests {

    CustomerService customerService = new CustomerServiceImpl(new CustomerDAOPostgres());
    static Customer customer = null;

    @Test
    @Order(1)
    void register_user_test(){

        int id = 0;
        String username = RandomStringUtils.randomAlphanumeric(8);
        String password = "pass";
        String firstName = "Zeus";
        String lastName = "Beard";

        customer = customerService.registerUser(new Customer(id, firstName, lastName, username, password));
        Assertions.assertNotEquals(0, customer.getId());
    }

    @Test
    @Order(2)
    void login_user_test(){

        Customer loggedInUser;

        loggedInUser = customerService.loginUser(customer.getUserName(), customer.getPassword());

        Assertions.assertEquals(customer.getUserName(), loggedInUser.getUserName());
        Assertions.assertEquals(customer.getPassword(), loggedInUser.getPassword());
    }

    @Test
    @Order(3)
    void update_user_test(){

        String newName = "Odin";

        customer.setFirstName(newName);
        customer = customerService.updateInfo(customer);

        Assertions.assertEquals(newName, customer.getFirstName());
    }
    @Test
    @Order(4)
    void unique_username_test(){
        String username = RandomStringUtils.randomAlphanumeric(8);

        Assertions.assertTrue(customerService.isUniqueUsername(username));
    }

    @Test
    @Order(5)
    void delete_user_test(){

        Assertions.assertTrue(customerService.deleteUser(customer));
    }
}
