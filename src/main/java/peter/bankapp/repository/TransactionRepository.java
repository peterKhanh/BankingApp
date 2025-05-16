package peter.bankapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import peter.bankapp.entity.Transaction;
import peter.bankapp.entity.User;

public interface TransactionRepository extends JpaRepository<Transaction, String> {

}
