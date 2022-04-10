package dev.langst.services;

import dev.langst.entities.Customer;

public interface CustomerService {

    Customer registerUser(Customer customer);

    Customer loginUser(String username, String password);

    boolean deleteUser(Customer customer);

    Customer updateInfo(Customer customer);

}
