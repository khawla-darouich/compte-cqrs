package org.glsid3.comptecqrs.commandapi.events;

import lombok.Getter;

public class AccountActivatedEventEvent extends BaseEvent<String>{
    @Getter
    private double initialBalance;
    @Getter
    private String currency;
    public AccountActivatedEventEvent(String id, double initialBalance, String currency) {
        super(id);
        this.initialBalance = initialBalance;
        this.currency = currency;
    }
}
