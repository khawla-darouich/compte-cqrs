package org.glsid3.comptecqrs.queries.repositories;

import org.glsid3.comptecqrs.queries.entities.Account;
import org.glsid3.comptecqrs.queries.entities.Operation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OperationRepository extends JpaRepository<Operation,Long> {
}
