package org.example.controller;

import org.example.entities.Expense;
import org.example.repository.ExpenseRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("expense/")
public class ExpenseController {

    private ExpenseRepository expenseRepository;

    public ExpenseController(ExpenseRepository expenseRepository1){
        this.expenseRepository=expenseRepository1;
    }

    @PostMapping("add")
    public ResponseEntity<Expense> expense(@RequestBody Expense expense){
        expenseRepository.save(expense);
        return new ResponseEntity<>(expense, HttpStatus.valueOf(200));
    }
    @GetMapping("get")
    public ResponseEntity<List<Expense>> expense(){
        List<Expense>list= StreamSupport
                .stream(expenseRepository.findAll().spliterator(), false)
                .toList();
        return new ResponseEntity<>(list,HttpStatus.valueOf(200));
    }
    @GetMapping("get/{name}")
    public ResponseEntity<Expense> specificExpense(@PathVariable String name){
        Expense expense=expenseRepository.findByName(name);
        return new ResponseEntity<>(expense,HttpStatus.valueOf(200));
    }
    @DeleteMapping("delete/{name}")
    public ResponseEntity<String> delete(@PathVariable String name){
        Expense expense=expenseRepository.findByName(name);
        expenseRepository.delete(expense);
        return new ResponseEntity<>("DELETED SUCCESSFULLY",HttpStatus.valueOf(200));
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
