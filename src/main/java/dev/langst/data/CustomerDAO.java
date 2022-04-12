package dev.langst.data;

import dev.langst.entities.Customer;

public interface CustomerDAO {

    Customer createCustomer(Customer customer);

    Customer getCustomerByUsername(String username);

    Customer updateCustomer(Customer customer);

    boolean deleteCustomerById(int id);
}
