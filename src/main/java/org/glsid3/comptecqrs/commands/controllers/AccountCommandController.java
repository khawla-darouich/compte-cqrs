package org.glsid3.comptecqrs.commands.controllers;

import lombok.AllArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.glsid3.comptecqrs.commandapi.DTO.CreateAccountRequestDTO;
import org.glsid3.comptecqrs.commandapi.DTO.CreditAccountRequestDTO;
import org.glsid3.comptecqrs.commandapi.DTO.DebitAccountRequestDTO;
import org.glsid3.comptecqrs.commandapi.commands.CreateAccountCommand;
import org.glsid3.comptecqrs.commandapi.commands.CreditAccountCommand;
import org.glsid3.comptecqrs.commandapi.commands.DebitAccountCommand;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

@RestController
@RequestMapping("/commands/account")
@AllArgsConstructor
public class AccountCommandController {

    private CommandGateway commandGateway;
    private EventStore eventStore;
    @PostMapping("/create")
    public CompletableFuture<String> createAccount(@RequestBody CreateAccountRequestDTO request){
       CompletableFuture<String> commandResponse = commandGateway.send(new CreateAccountCommand(
                UUID.randomUUID().toString(),
                request.getInitialBalance(),
                request.getCurrency()
        ));

        return commandResponse;
    }

    @PutMapping("/credit")
    public CompletableFuture<String> creditAccount(@RequestBody CreditAccountRequestDTO request){
        CompletableFuture<String> commandResponse = commandGateway.send(new CreditAccountCommand(
                UUID.randomUUID().toString(),
                request.getAmount(),
                request.getCurrency()
        ));

        return commandResponse;
    }

    @PutMapping("/debit")
    public CompletableFuture<String> debitAccount(@RequestBody DebitAccountRequestDTO request){
        CompletableFuture<String> commandResponse = commandGateway.send(new DebitAccountCommand(
                UUID.randomUUID().toString(),
                request.getAmount(),
                request.getCurrency()
        ));

        return commandResponse;
    }

    @GetMapping("/eventStore/{accounId}")
    public Stream eventStore(@RequestParam  String accountId){
        return eventStore.readEvents(accountId).asStream();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> exceptionHandler(Exception e){
        ResponseEntity<String> entity=new ResponseEntity<>(
                e.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
        return entity;
    }
}
