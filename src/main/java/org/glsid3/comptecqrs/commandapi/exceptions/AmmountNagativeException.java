package org.glsid3.comptecqrs.commandapi.exceptions;

public class AmmountNagativeException extends Throwable {
    public AmmountNagativeException(String amount_should_not_be_negative) {
        super(amount_should_not_be_negative);
    }
}
