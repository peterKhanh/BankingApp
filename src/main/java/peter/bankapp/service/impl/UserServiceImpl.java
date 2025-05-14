package peter.bankapp.service.impl;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import peter.bankapp.dto.AccountInfo;
import peter.bankapp.dto.BankResponse;
import peter.bankapp.dto.UserRequest;
import peter.bankapp.entity.User;
import peter.bankapp.repository.UserRepository;
import peter.bankapp.service.UserService;
import peter.bankapp.ultil.AccountUtils;

@Service
public class UserServiceImpl implements UserService{
	 @Autowired
	    UserRepository userRepository;

	@Override
	public BankResponse createAccount(UserRequest userRequest) {
		   /**
         * Creating an account - saving a new user into the db
         * check if user already has an account
         */
        if (userRepository.existsByEmail(userRequest.getEmail())){
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
	        
	        return BankResponse.builder()
	        	      .responseCode(AccountUtils.ACCOUNT_CREATION_SUCCESS)
	                  .responseMessage(AccountUtils.ACCOUNT_CREATION_MESSAGE)
	                  .accountInfo(AccountInfo.builder()
	                		  .accountBalance(savedUser.getAccountBalance())
	                		  .accountNumber(savedUser.getAccountNumber())
	                		  .accountName(savedUser.getFirstName()+ "" + savedUser.getLastName() + " " + savedUser.getOtherName() )
	                		  .build())
	              	.build();
		
	}


	
	}

