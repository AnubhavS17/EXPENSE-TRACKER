package org.example.controller;

import org.example.entities.Expense;
import org.example.entities.UserInfo;
import org.example.repository.ExpenseRepository;
import org.example.repository.UserInfoRepository;
import org.example.request.ExpenseRequest;
import org.example.service.ExpenseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("expense/")
public class ExpenseController {

    private ExpenseRepository expenseRepository;
    private ExpenseService expenseService;
    private UserInfoRepository userInfoRepository;

    public ExpenseController(ExpenseRepository expenseRepository1,ExpenseService expenseService1,UserInfoRepository userInfoRepository1){

        this.expenseRepository=expenseRepository1;
        this.expenseService=expenseService1;
        this.userInfoRepository=userInfoRepository1;
    }

    @PostMapping("add/{username}")
    public ResponseEntity<Expense> addExpense(@PathVariable String username, @RequestBody Expense expense){
//        expenseRepository.save(expense);
//        Expense expense = new Expense();
//        expense.setAmount(request.getAmount());
//        expense.setName(request.getName());
//        expense.setCategory(request.getCategory());

        Expense savedExpense = expenseService.addExpense(username, expense);
        return new ResponseEntity<>(expense, HttpStatus.valueOf(200));
    }
    @GetMapping("get/{username}")
    public List<Expense> expense(@PathVariable String username){
        UserInfo user = userInfoRepository.findByUsername(username);
        return expenseRepository.findByUser(user);
//        List<Expense>list= StreamSupport
//                .stream(expenseRepository.findAll().spliterator(), false)
//                .toList();
//        return new ResponseEntity<>(list,HttpStatus.valueOf(200));
    }
//    @GetMapping("get/{name}")
//    public ResponseEntity<Expense> specificExpense(@PathVariable String name){
//        Expense expense=expenseRepository.findByName(name);
//        return new ResponseEntity<>(expense,HttpStatus.valueOf(200));
//    }
    @DeleteMapping("delete/{username}/{name}")
    public ResponseEntity<Map<String,String>> delete(@PathVariable("username") String username,@PathVariable("name")String name){
        try {
            String msg = expenseService.deleteExpense(username, name);
            return ResponseEntity.ok(Map.of("message", "Expense deleted successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("message",e.getMessage()));
        }
    }
    @PutMapping("update/{name}")
    public ResponseEntity<String> update(@RequestBody Expense expense,@PathVariable String name){
        Expense expense1=expenseRepository.findByName(name);
        if(expense1!=null){
            expense1.setName(expense.getName());
            expense1.setAmount(expense.getAmount());
            expense1.setCategory(expense.getCategory());
            expenseRepository.save(expense1);
            return new ResponseEntity<>("UPDATED SUCCESSFULLY",HttpStatus.valueOf(200));
        }
        else{
            return new ResponseEntity<>("NOT FOUND",HttpStatus.BAD_REQUEST);
        }
    }

}
