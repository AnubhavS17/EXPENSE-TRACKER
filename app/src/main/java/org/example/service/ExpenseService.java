package org.example.service;

import org.example.entities.Expense;
import org.example.entities.UserInfo;
import org.example.repository.ExpenseRepository;
import org.example.repository.UserInfoRepository;
import org.example.request.ExpenseRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

    public String deleteExpense(String username,String name){
        UserInfo userInfo=userInfoRepository.findByUsername(username);
        if(userInfo==null){
            throw new RuntimeException("User not found!");
        }
        Optional<Expense> expenseOpt = expenseRepository.findByNameAndUser_UserId(name, userInfo.getUserId());
        if (expenseOpt.isPresent()) {
            expenseRepository.delete(expenseOpt.get());
            return "Expense deleted successfully!";
        } else {
            throw new RuntimeException("Expense not found for this user!");
        }
    }
}
