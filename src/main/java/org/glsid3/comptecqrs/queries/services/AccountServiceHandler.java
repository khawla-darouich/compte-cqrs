package org.glsid3.comptecqrs.queries.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.glsid3.comptecqrs.commandapi.enums.AccountStatus;
import org.glsid3.comptecqrs.commandapi.enums.OperationType;
import org.glsid3.comptecqrs.commandapi.events.AccountActivatedEvent;
import org.glsid3.comptecqrs.commandapi.events.AccountCreatedEvent;
import org.glsid3.comptecqrs.commandapi.events.AccountCreditedEvent;
import org.glsid3.comptecqrs.commandapi.events.AccountDebitedEvent;
import org.glsid3.comptecqrs.commandapi.queries.GetAccountByIdQuery;
import org.glsid3.comptecqrs.commandapi.queries.GetAllAcountQuery;
import org.glsid3.comptecqrs.queries.entities.Account;
import org.glsid3.comptecqrs.queries.entities.Operation;
import org.glsid3.comptecqrs.queries.repositories.AccountRepository;
import org.glsid3.comptecqrs.queries.repositories.OperationRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class AccountServiceHandler {
    private AccountRepository accountRepository;
    private OperationRepository operationRepository;
    @EventHandler
    public void on(AccountCreatedEvent event){
        log.info("**********************");
        log.info("AccountCreatedEvent received");
        accountRepository.save(new Account(
                event.getId(),
                event.getInitialBalance(),
                event.getStatus(),
                event.getCurrency(),
                null

        ));

    }
    public void on(AccountActivatedEvent event){
        log.info("**********************");
        log.info("AccountActivatedEvent received");
        Account account=accountRepository.findById(event.getId()).get();
        account.setStatus(AccountStatus.ACTIVATED);
        accountRepository.save(account);

    }
    public void on(AccountDebitedEvent event){
        log.info("**********************");
        log.info("AccountDebitedEvent received");
        Account account=accountRepository.findById(event.getId()).get();
        Operation operation=new Operation();
        operation.setAmount(event.getAmount());
        operation.setDate(new Date());
        operation.setType(OperationType.DEBIT);
        operation.setAccount(account);
        operationRepository.save(operation);
        account.setBalance(account.getBalance()-event.getAmount());
        accountRepository.save(account);

    }
    public void on(AccountCreditedEvent event){
        log.info("**********************");
        log.info("AccountCreditedEvent received");
        Account account=accountRepository.findById(event.getId()).get();
        Operation operation=new Operation();
        operation.setAmount(event.getAmount());
        operation.setDate(new Date());
        operation.setType(OperationType.CREDIT);
        operation.setAccount(account);
        operationRepository.save(operation);
        account.setBalance(account.getBalance()+event.getAmount());
        accountRepository.save(account);

    }

    @QueryHandler
    public List<Account> on(GetAllAcountQuery query){
        return accountRepository.findAll();
    }

    @QueryHandler
    public Account on(GetAccountByIdQuery query){
        return accountRepository.findById(query.getId()).get();
    }
}
