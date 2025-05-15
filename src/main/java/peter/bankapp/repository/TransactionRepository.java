package peter.bankapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import peter.bankapp.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, String> {

}
