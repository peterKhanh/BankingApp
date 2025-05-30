package peter.bankapp.service;

import peter.bankapp.dto.BankResponse;
import peter.bankapp.dto.CreditDebitRequest;
import peter.bankapp.dto.EnquiryRequest;
import peter.bankapp.dto.TransferRequest;
import peter.bankapp.dto.UserRequest;

public interface UserService {
	BankResponse createAccount(UserRequest userRequest) ;
    BankResponse balanceEnquiry(EnquiryRequest request);
    String nameEnquiry(EnquiryRequest request);
    BankResponse creditAccount(CreditDebitRequest request);
    BankResponse debitAccount(CreditDebitRequest request);
    BankResponse transfer(TransferRequest request);
    }
