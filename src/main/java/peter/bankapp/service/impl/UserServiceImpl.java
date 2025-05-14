package peter.bankapp.service.impl;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import peter.bankapp.dto.AccountInfo;
import peter.bankapp.dto.BankResponse;
import peter.bankapp.dto.EmailDetails;
import peter.bankapp.dto.EnquiryRequest;
import peter.bankapp.dto.UserRequest;
import peter.bankapp.entity.User;
import peter.bankapp.repository.UserRepository;
import peter.bankapp.service.EmailService;
import peter.bankapp.service.UserService;
import peter.bankapp.ultil.AccountUtils;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	UserRepository userRepository;
	@Autowired
	EmailService emailService;

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
	

}
