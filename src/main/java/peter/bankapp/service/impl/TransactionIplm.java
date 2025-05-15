package peter.bankapp.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import peter.bankapp.dto.TransactionDto;
import peter.bankapp.entity.Transaction;
import peter.bankapp.repository.TransactionRepository;
import peter.bankapp.service.TransactionService;

@Component
public class TransactionIplm implements TransactionService{
	@Autowired
	private TransactionRepository transactionRepository;
	@Override
	public void saveTransaction(TransactionDto transactionDto) {
		Transaction transaction = Transaction.builder()
				.transactionType(transactionDto.getTransactionType())
				.accountNumber(transactionDto.getAccountNumber())
				.amount(transactionDto.getAmount())
				.status("SUCCESS")
				.build();
		transactionRepository.save(transaction);
		System.out.println("Transaction saved success");
	}

}
