package dev.langst.services;

import dev.langst.entities.Account;

public interface AccountService {

    Account openAccount(Account account);

    boolean closeAccount(Account account);

    Account adjustBalance(Account account, double difference);
}