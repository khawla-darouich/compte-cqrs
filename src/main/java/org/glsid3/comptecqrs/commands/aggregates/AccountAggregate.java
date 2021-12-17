package org.glsid3.comptecqrs.commands.aggregates;

import lombok.NoArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.glsid3.comptecqrs.commandapi.AccountStatus;
import org.glsid3.comptecqrs.commandapi.commands.CreateAccountCommand;
import org.glsid3.comptecqrs.commandapi.events.AccountActivatedEventEvent;
import org.glsid3.comptecqrs.commandapi.events.AccountCreatedEvent;

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
                createAccountCommand.getCurrency()
        ));
    }

    @EventSourcingHandler
    public void on(AccountCreatedEvent accountCreatedEvent){
        this.currency=accountCreatedEvent.getCurrency();
        this.balance=accountCreatedEvent.getInitialBalance();
        this.status=AccountStatus.CREATED;
        this.accountId=accountCreatedEvent.getId();
        AggregateLifecycle.apply(new AccountActivatedEventEvent(
                accountCreatedEvent.getId(),
                accountCreatedEvent.getInitialBalance(),
                accountCreatedEvent.getCurrency()
        ));
    }
}
