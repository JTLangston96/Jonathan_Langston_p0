package dev.langst.services;

import dev.langst.data.AccountDAO;
import dev.langst.entities.Account;

public class AccountServiceImpl implements AccountService{

    private AccountDAO accountDAO;

    public AccountServiceImpl(AccountDAO accountDAO){
        this.accountDAO = accountDAO;
    }

    @Override
    public Account openAccount(Account account) {
        return accountDAO.createAccount(account);
    }

    @Override
    public boolean closeAccount(Account account) {
        return accountDAO.deleteAccountById(account.getAccountId());
    }

    @Override
    public Account adjustBalance(Account account, double difference) {
        if((account.getBalance() + difference) > 0) {
            account.setBalance(account.getBalance() + difference);
            return accountDAO.updateAccount(account);
        }
        else{
            System.out.println("You may not overdraft your account.");
            return account;
        }
    }
}