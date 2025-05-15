package peter.bankapp.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import peter.bankapp.dto.AccountInfo;
import peter.bankapp.dto.BankResponse;
import peter.bankapp.dto.CreditDebitRequest;
import peter.bankapp.dto.EmailDetails;
import peter.bankapp.dto.EnquiryRequest;
import peter.bankapp.dto.TransactionDto;
import peter.bankapp.dto.TransferRequest;
import peter.bankapp.dto.UserRequest;
import peter.bankapp.entity.User;
import peter.bankapp.repository.UserRepository;
import peter.bankapp.service.EmailService;
import peter.bankapp.service.TransactionService;
import peter.bankapp.service.UserService;
import peter.bankapp.ultil.AccountUtils;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	UserRepository userRepository;
	@Autowired
	EmailService emailService;
	@Autowired
	TransactionService transactionService;

	@Override
	public BankResponse createAccount(UserRequest userRequest) {
		/**
		 * Creating an account - saving a new user into the db check if user already has
		 * an account
		 */
		if (userRepository.existsByEmail(userRequest.getEmail())) {
			return BankResponse.builder()
					.responseCode(AccountUtils.ACCOUNT_EXISTS_CODE)
					.responseMessage(AccountUtils.ACCOUNT_EXISTS_MESSAGE)
					.accountInfo(null)
					.build();
		}
		User newUser = User.builder()
				.firstName(userRequest.getFirstName())
				.lastName(userRequest.getLastName())
				.otherName(userRequest.getOtherName())
				.gender(userRequest.getGender())
				.address(userRequest.getAddress())
				.stateOfOrigin(userRequest.getStateOfOrigin())
				.accountNumber(AccountUtils.generateAccountNumber())
				.accountBalance(BigDecimal.ZERO)
				.email(userRequest.getEmail())
				.phoneNumber(userRequest.getPhoneNumber())
				.alternativePhoneNumber(userRequest.getAlternativePhoneNumber())
				.status("ACTIVE")
				.build();

		User savedUser = userRepository.save(newUser);

        //Send email Alert
		/*
		EmailDetails emailDetails = EmailDetails.builder()
				 .recipient(savedUser.getEmail())
	                .subject("ACCOUNT CREATION")
	                .messageBody("Congratulations! Your Account Has been Successfully Created.\nYour Account Details: \n" +
	                        "Account Name: " + savedUser.getFirstName() + " " + savedUser.getLastName() + " " + savedUser.getOtherName() + "\nAccount Number: " + savedUser.getAccountNumber())
	                .build();
	        emailService.sendEmailAlert(emailDetails);
		*/
		
		return BankResponse.builder()
				.responseCode(AccountUtils.ACCOUNT_CREATION_SUCCESS)
				.responseMessage(AccountUtils.ACCOUNT_CREATION_MESSAGE)
				.accountInfo(AccountInfo.builder()
						.accountBalance(savedUser.getAccountBalance())
						.accountNumber(savedUser.getAccountNumber())
						.accountName(savedUser.getFirstName() + ""
								+ savedUser.getLastName() + " " + savedUser.getOtherName())
						.build())
				.build();

	}

	@Override
	public BankResponse balanceEnquiry(EnquiryRequest request) {
		boolean isAccountExist = userRepository.existsByAccountNumber(request.getAccountNumber()) ;
		   if (!isAccountExist){
	            return BankResponse.builder()
	                    .responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
	                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
	                    .accountInfo(null)
	                    .build();
	        }
	        User foundUser = userRepository.findByAccountNumber(request.getAccountNumber());
	        return BankResponse.builder()
	                .responseCode(AccountUtils.ACCOUNT_FOUND_CODE)
	                .responseMessage(AccountUtils.ACCOUNT_FOUND_SUCCESS)
	                .accountInfo(AccountInfo.builder()
	                        .accountBalance(foundUser.getAccountBalance())
	                        .accountNumber(request.getAccountNumber())
	                        .accountName(foundUser.getFirstName() + " " + foundUser.getLastName() + " " + foundUser.getOtherName())
	                        .build())
	                .build();
	    }
	

	@Override
	public String nameEnquiry(EnquiryRequest request) {
		 boolean isAccountExist = userRepository.existsByAccountNumber(request.getAccountNumber());
	        if (!isAccountExist){
	            return AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE;
	        }
	        User foundUser = userRepository.findByAccountNumber(request.getAccountNumber());
	        return foundUser.getFirstName() + " " + foundUser.getLastName() + " " + foundUser.getOtherName();
	    }

	@Override
	public BankResponse creditAccount(CreditDebitRequest request) {
		boolean isAccountExist = userRepository.existsByAccountNumber(request.getAccountNumber()) ;
		   if (!isAccountExist){
	            return BankResponse.builder()
	                    .responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
	                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
	                    .accountInfo(null)
	                    .build();
	        }
		   User userToCredit = userRepository.findByAccountNumber(request.getAccountNumber());
	       userToCredit.setAccountBalance(userToCredit.getAccountBalance().add(request.getAmount()));
	       userRepository.save(userToCredit);
	       
	       
	       // Save Transaction
	       TransactionDto transactionDto = TransactionDto.builder()
	    		   .accountNumber(userToCredit.getAccountNumber())
	    		   .transactionType("CREDIT")
	    		   .amount(request.getAmount())
	    		   .build();
	       transactionService.saveTransaction(transactionDto);
		   //
	       
	       return BankResponse.builder()
	                .responseCode(AccountUtils.ACCOUNT_CREDITED_SUCCESS)
	                .responseMessage(AccountUtils.ACCOUNT_CREDITED_SUCCESS_MESSAGE)
	                .accountInfo(AccountInfo.builder()
	                        .accountName(userToCredit.getFirstName() + " " + userToCredit.getLastName() + " " + userToCredit.getOtherName())
	                        .accountBalance(userToCredit.getAccountBalance())
	                        .accountNumber(request.getAccountNumber())
	                        .build())
	                .build();
	}

	@Override
	public BankResponse debitAccount(CreditDebitRequest request) {
		   //check if the account exists
        //check if the amount you intend to withdraw is not more than the current account balance
        boolean isAccountExist = userRepository.existsByAccountNumber(request.getAccountNumber());
        if (!isAccountExist){
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
                    .accountInfo(null)
                    .build();
        }

        User userToDebit = userRepository.findByAccountNumber(request.getAccountNumber());
        BigInteger availableBalance =userToDebit.getAccountBalance().toBigInteger();
        BigInteger debitAmount = request.getAmount().toBigInteger();
        if ( availableBalance.intValue() < debitAmount.intValue()){
            return BankResponse.builder()
                    .responseCode(AccountUtils.INSUFFICIENT_BALANCE_CODE)
                    .responseMessage(AccountUtils.INSUFFICIENT_BALANCE_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        else {
            userToDebit.setAccountBalance(userToDebit.getAccountBalance().subtract(request.getAmount()));
            userRepository.save(userToDebit);
            
            // Save Transaction
 	       TransactionDto transactionDto = TransactionDto.builder()
 	    		   .accountNumber(userToDebit.getAccountNumber())
 	    		   .transactionType("DEBIT")
 	    		   .amount(request.getAmount())
 	    		   .build();
 	       transactionService.saveTransaction(transactionDto);
 		   //
            
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_DEBITED_SUCCESS)
                    .responseMessage(AccountUtils.ACCOUNT_DEBITED_MESSAGE)
                    .accountInfo(AccountInfo.builder()
                            .accountNumber(request.getAccountNumber())
                            .accountName(userToDebit.getFirstName() + " " + userToDebit.getLastName() + " " + userToDebit.getOtherName())
                            .accountBalance(userToDebit.getAccountBalance())
                            .build())
                    .build();
        }
        
        

    }

	@Override
	public BankResponse transfer(TransferRequest request) {
//        boolean isSourceAccountExist = userRepository.existsByAccountNumber(request.getSourceAccountNumber());
        boolean isDestinationAccountExist = userRepository.existsByAccountNumber(request.getDestinationAccountNumber());
//
//        if (!isSourceAccountExist){
//            return BankResponse.builder()
//                    .responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
//                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
//                    .accountInfo(null)
//                    .build();
//        }

        if (!isDestinationAccountExist){
    		System.out.println("Tai khoan nhan khong ton tai");

            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        
        User sourceAccountUser = userRepository.findByAccountNumber(request.getSourceAccountNumber());
        if (request.getAmount().compareTo(sourceAccountUser.getAccountBalance()) > 0) {
    		System.out.println("Khong du de chuyen");

        	 return BankResponse.builder()
                     .responseCode(AccountUtils.INSUFFICIENT_BALANCE_CODE)
                     .responseMessage(AccountUtils.INSUFFICIENT_BALANCE_MESSAGE)
                     .accountInfo(null)
                     .build();
        }
        
        sourceAccountUser.setAccountBalance(sourceAccountUser.getAccountBalance().subtract(request.getAmount()));
        userRepository.save(sourceAccountUser);
        //  Can them Sent mail
        
        /// 
        User destinationAccountUser = userRepository.findByAccountNumber(request.getDestinationAccountNumber());
        destinationAccountUser.setAccountBalance(destinationAccountUser.getAccountBalance().add(request.getAmount()));
        userRepository.save(destinationAccountUser);

        // Save Transaction
	       TransactionDto transactionDto = TransactionDto.builder()
	    		   .accountNumber(destinationAccountUser.getAccountNumber())
	    		   .transactionType("TRANSFER")
	    		   .amount(request.getAmount())
	    		   .build();
	       transactionService.saveTransaction(transactionDto);
		   //
        System.out.println("CK thanh cong");

        return BankResponse.builder()
        		
                .responseCode(AccountUtils.TRANSFER_SUCCESSFUL_CODE)
                .responseMessage(AccountUtils.TRANSFER_SUCCESSFUL_MESSAGE)
                .accountInfo(null)
                .build();
	}
	
	

}
