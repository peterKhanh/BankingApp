package peter.bankapp.service;

import peter.bankapp.dto.TransactionDto;

public interface TransactionService {
	void saveTransaction(TransactionDto transactionDto);
}
