package peter.bankapp.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import peter.bankapp.entity.Transaction;
import peter.bankapp.repository.TransactionRepository;
@Service
public class BankStatement {
	@Autowired
	private TransactionRepository transactionRepository;
		
	public List<Transaction> getTransactionByAccount(String accountNumber){
		//return transactionRepository.findByAccountNumber(accountNumber);
		return transactionRepository.findAll();
	}
}
