package dev.langst.data;

import dev.langst.entities.Account;
import dev.langst.utilities.List;

public interface AccountDAO {

    Account createAccount(Account account);

    Account getAccountById(int accountId);

    List<Account> getAccountsByUserId(int userId);

    Account updateAccount(Account account);

    boolean deleteAccountById(int accountId);
}
