package dev.langst.data;

import dev.langst.entities.Account;

public interface AccountDAO {

    Account createAccount(Account account);

    Account getAccountById(int accountId);

    Account updateAccount(Account account);

    boolean deleteAccountById(int accountId);
}
