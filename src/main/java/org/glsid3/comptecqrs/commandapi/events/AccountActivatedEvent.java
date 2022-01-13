package org.glsid3.comptecqrs.commandapi.events;

import lombok.Getter;
import org.glsid3.comptecqrs.commandapi.enums.AccountStatus;

public class AccountActivatedEvent extends BaseEvent<String>{

    @Getter private AccountStatus status;
    public AccountActivatedEvent(String id, AccountStatus status) {
        super(id);
        this.status = status;
    }
}
