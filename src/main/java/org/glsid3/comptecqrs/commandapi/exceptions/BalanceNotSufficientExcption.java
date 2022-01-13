package org.glsid3.comptecqrs.commandapi.exceptions;

public class BalanceNotSufficientExcption extends Exception {
    public BalanceNotSufficientExcption(String balance_not_sufficient) {
        super(balance_not_sufficient);
    }
}
