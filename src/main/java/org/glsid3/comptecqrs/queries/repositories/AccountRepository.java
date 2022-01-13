package org.glsid3.comptecqrs.queries.repositories;

import org.glsid3.comptecqrs.queries.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account,String> {
}
