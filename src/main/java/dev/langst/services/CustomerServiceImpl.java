package dev.langst.services;

import dev.langst.data.AccountDAO;
import dev.langst.data.CustomerDAO;
import dev.langst.entities.Account;
import dev.langst.entities.Customer;
import dev.langst.utilities.List;
import dev.langst.utilities.LogLevel;
import dev.langst.utilities.Logger;

public class CustomerServiceImpl implements CustomerService {

    private CustomerDAO customerDAO;
    private AccountDAO accountDAO;

    public CustomerServiceImpl(CustomerDAO customerDAO){
        this.customerDAO = customerDAO;
        this.accountDAO = accountDAO;
    }

    @Override
    public Customer registerUser(Customer customer) {

        Logger.log("The user with the ID: " + customer.getId() + "has been created.", LogLevel.INFO);
        return customerDAO.createCustomer(customer);
    }

    @Override
    public Customer loginUser(String username, String password) {
        Customer user = customerDAO.getCustomerByUsername(username);
        if(user == null){
            return user;
        }
        if(user.getPassword().equals(password)){
            return user;
        }

        return null;
    }

    @Override
    public boolean deleteUser(Customer customer) {

        Logger.log("The user with the ID: " + customer.getId() + "has been deleted.", LogLevel.INFO);
        return customerDAO.deleteCustomerById(customer.getId());
    }

    @Override
    public Customer updateInfo(Customer customer) {
        return customerDAO.updateCustomer(customer);
    }

    @Override
    public boolean isUniqueUsername(String username) {

        if(customerDAO.getCustomerByUsername(username) == null){
            return true;
        }

        return false;
    }
}
