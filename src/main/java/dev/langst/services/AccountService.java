package dev.langst.services;

import dev.langst.entities.Account;
import dev.langst.utilities.List;

public interface AccountService {

    Account openAccount(Account account);

    boolean closeAccount(Account account);

    Account adjustBalance(Account account, double difference);

    List<Account> retrieveAccounts(int userId);
}