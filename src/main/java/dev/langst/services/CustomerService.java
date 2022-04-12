package dev.langst.services;

import dev.langst.entities.Account;
import dev.langst.entities.Customer;
import dev.langst.utilities.List;

public interface CustomerService {

    Customer registerUser(Customer customer);

    Customer loginUser(String username, String password);

    boolean deleteUser(Customer customer);

    Customer updateInfo(Customer customer);

    boolean isUniqueUsername(String username);

}
