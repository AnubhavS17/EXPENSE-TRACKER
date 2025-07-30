package org.example.repository;

import org.example.entities.Expense;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpenseRepository extends CrudRepository<Expense,Long> {
    Expense findByName(String name);
}
