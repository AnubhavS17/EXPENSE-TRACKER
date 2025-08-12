package org.example.service;

import org.example.entities.Expense;
import org.example.entities.UserInfo;
import org.example.repository.ExpenseRepository;
import org.example.repository.UserInfoRepository;
import org.example.request.ExpenseRequest;
import org.springframework.stereotype.Service;

@Service
public class ExpenseService {

    private UserInfoRepository userInfoRepository;
    private ExpenseRepository expenseRepository;

    public ExpenseService(UserInfoRepository userInfoRepository1,ExpenseRepository expenseRepository1){
        this.expenseRepository=expenseRepository1;
        this.userInfoRepository=userInfoRepository1;
    }

    public Expense addExpense(String username, Expense expense){
        UserInfo user = userInfoRepository.findByUsername(username);
        expense.setUser(user);
        return expenseRepository.save(expense);
    }
}
