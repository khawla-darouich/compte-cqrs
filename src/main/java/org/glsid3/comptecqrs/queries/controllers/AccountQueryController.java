package org.glsid3.comptecqrs.queries.controllers;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.messaging.responsetypes.ResponseType;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.glsid3.comptecqrs.commandapi.queries.GetAccountByIdQuery;
import org.glsid3.comptecqrs.commandapi.queries.GetAllAcountQuery;
import org.glsid3.comptecqrs.queries.entities.Account;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/query/accounts")
@AllArgsConstructor
@Slf4j
public class AccountQueryController {
    private QueryGateway queryGateway;

    @GetMapping("AllAccounts")
    public List<Account> accountList(){
      List<Account> response=queryGateway.query(new GetAllAcountQuery(), ResponseTypes.multipleInstancesOf(Account.class)).join();
      return response;
    }


    @GetMapping("/byId/{id}")
    public Account getAcount(@PathVariable String accountId){
        Account response=queryGateway.query(new GetAccountByIdQuery(accountId), ResponseTypes.instanceOf(Account.class)).join();
        return response;
    }

}
