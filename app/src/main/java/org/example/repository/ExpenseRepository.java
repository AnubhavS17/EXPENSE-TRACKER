package org.example.repository;

import org.example.entities.Expense;
import org.example.entities.UserInfo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpenseRepository extends CrudRepository<Expense,Long> {
    Expense findByName(String name);
    List<Expense> findByUser(UserInfo userInfo);
}
