package org.glsid3.comptecqrs.commands.aggregates;

import lombok.NoArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.glsid3.comptecqrs.commandapi.enums.AccountStatus;
import org.glsid3.comptecqrs.commandapi.commands.CreateAccountCommand;
import org.glsid3.comptecqrs.commandapi.commands.CreditAccountCommand;
import org.glsid3.comptecqrs.commandapi.commands.DebitAccountCommand;
import org.glsid3.comptecqrs.commandapi.events.AccountActivatedEvent;
import org.glsid3.comptecqrs.commandapi.events.AccountCreatedEvent;
import org.glsid3.comptecqrs.commandapi.events.AccountCreditedEvent;
import org.glsid3.comptecqrs.commandapi.events.AccountDebitedEvent;
import org.glsid3.comptecqrs.commandapi.exceptions.AmmountNagativeException;
import org.glsid3.comptecqrs.commandapi.exceptions.BalanceNotSufficientExcption;

@Aggregate
@NoArgsConstructor
public class AccountAggregate {
    @AggregateIdentifier
    private String accountId;
    private double balance;
    private String currency;
    private AccountStatus status;

    @CommandHandler
    public AccountAggregate(CreateAccountCommand createAccountCommand) {
        if(createAccountCommand.getInitialBalance()<0)
            throw new RuntimeException("Impossible de creer un compte avec un solde negative");
        AggregateLifecycle.apply(new AccountCreatedEvent(
                createAccountCommand.getId(),
                createAccountCommand.getInitialBalance(),
                createAccountCommand.getCurrency(),
                AccountStatus.CREATED));
    }

    @EventSourcingHandler
    public void on(AccountCreatedEvent accountCreatedEvent){
        this.currency=accountCreatedEvent.getCurrency();
        this.balance=accountCreatedEvent.getInitialBalance();
        this.status=AccountStatus.CREATED;
        this.accountId=accountCreatedEvent.getId();
        AggregateLifecycle.apply(new AccountActivatedEvent(
                accountCreatedEvent.getId(),
                accountCreatedEvent.getStatus()
        ));
    }

    @EventSourcingHandler
    public void on(AccountActivatedEvent accountActivatedEventEvent)
    {
        //this.status=accountActivatedEventEvent.get
        this.status=AccountStatus.ACTIVATED;
    }

    @CommandHandler
    public void handle(CreditAccountCommand command) throws AmmountNagativeException {
        if(command.getAmount()<0) throw  new AmmountNagativeException("Amount should not be negative");
        AggregateLifecycle.apply(new AccountCreditedEvent(
                command.getId(),
                command.getAmount(),
                command.getCurrency()
        ));
    }

    @EventSourcingHandler
    public void on(AccountCreditedEvent event){
        this.balance+=event.getAmount();
    }

    @CommandHandler
    public void handle(DebitAccountCommand command) throws BalanceNotSufficientExcption {
        if(command.getAmount()<0) throw  new RuntimeException("Amount should not be negative");
        if(this.balance<command.getAmount()) throw new BalanceNotSufficientExcption("Balance not sufficient");
        AggregateLifecycle.apply(new AccountDebitedEvent(
                command.getId(),
                command.getAmount(),
                command.getCurrency()
        ));
    }

    @EventSourcingHandler
    public void on(AccountDebitedEvent event){
        this.balance+=event.getAmount();
    }
}
