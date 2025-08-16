package org.example.repository;

import org.example.entities.Expense;
import org.example.entities.UserInfo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExpenseRepository extends CrudRepository<Expense,Long> {
    Expense findByName(String name);
    List<Expense> findByUser(UserInfo userInfo);
    Optional<Expense> findByNameAndUser_UserId(String name, String userId);
//    void deleteByNameAndUserInfo_UserId(String name, String userId);
}
