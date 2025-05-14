package peter.bankapp.service;

import org.springframework.stereotype.Service;

import peter.bankapp.dto.BankResponse;
import peter.bankapp.dto.UserRequest;
@Service
public interface UserService {
	BankResponse createAccount(UserRequest userRequest) ;
	
}
