package peter.bankapp.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import peter.bankapp.entity.Transaction;
import peter.bankapp.service.impl.BankStatement;

@RestController
@RequestMapping("/api/transaction")
public class TransactionController {
    @Autowired
	BankStatement bankStatement;
	
    @GetMapping("/list")
    public List<Transaction> getTransaction(@RequestBody String accountNumber) {
        return bankStatement.getTransactionByAccount(accountNumber);
    }
}


// @RequestBody EnquiryRequest request