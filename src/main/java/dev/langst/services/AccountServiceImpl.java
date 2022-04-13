package dev.langst.services;

import dev.langst.data.AccountDAO;
import dev.langst.entities.Account;
import dev.langst.utilities.List;

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

    @Override
    public List<Account> retrieveAccounts(int userId) {
        List<Account> accounts;

        accounts = accountDAO.getAccountsByUserId(userId);

        return accounts;
    }

    @Override
    public double deleteAllAccountsByUserId(int id) {
        double remainingAmount = 0.00;
        List<Account> accounts = accountDAO.getAccountsByUserId(id);

        for(int i = 0; i < accounts.size(); i++){
            remainingAmount += accounts.get(i).getBalance();
            if(!accountDAO.deleteAccountById(accounts.get(i).getAccountId())){
                return -1;
            }
        }

        return remainingAmount;
    }


}