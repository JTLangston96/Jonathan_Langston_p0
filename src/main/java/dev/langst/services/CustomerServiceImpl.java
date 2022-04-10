package dev.langst.services;

import dev.langst.data.CustomerDAO;
import dev.langst.entities.Customer;

public class CustomerServiceImpl implements CustomerService {

    private CustomerDAO customerDAO;

    public CustomerServiceImpl(CustomerDAO customerDAO){
        this.customerDAO = customerDAO;
    }

    @Override
    public Customer registerUser(Customer customer) {
        return customerDAO.createCustomer(customer);
    }

    @Override
    public Customer loginUser(String username, String password) {
        Customer user = customerDAO.getCustomerByUsername(username);

        if(user.getPassword().equals(password)){
            return user;
        }

        return null;
    }

    @Override
    public boolean deleteUser(Customer customer) {
        return customerDAO.deleteCustomerById(customer.getId());
    }

    @Override
    public Customer updateInfo(Customer customer) {
        return customerDAO.updateCustomer(customer);
    }
}
