package peter.bankapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import peter.bankapp.dto.BankResponse;
import peter.bankapp.dto.CreditDebitRequest;
import peter.bankapp.dto.EnquiryRequest;
import peter.bankapp.dto.TransferRequest;
import peter.bankapp.dto.UserRequest;
import peter.bankapp.service.UserService;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    UserService userService;
    
	@GetMapping("/users")
	public String getAllPost(){
		
		return "kkkk";
	}
	
	@PostMapping
	public BankResponse createAccount(@RequestBody UserRequest userRequest ){
		
		return userService.createAccount(userRequest);
	}
	
	 @GetMapping("/balanceEnquiry")
	    public BankResponse balanceEnquiry(@RequestBody EnquiryRequest request){
	        return userService.balanceEnquiry(request);
	    }

	    @GetMapping("/nameEnquiry")
	    public String nameEnquiry(@RequestBody EnquiryRequest request){
	        return userService.nameEnquiry(request);
	    }
	
	    
		@PostMapping("/credit")
		public BankResponse creditAccount(@RequestBody CreditDebitRequest request ){
			
			return userService.creditAccount(request);
		}
		
		@PostMapping("/debit")
		public BankResponse debitAccount(@RequestBody CreditDebitRequest request ){
			
			return userService.debitAccount(request);
		}

		@PostMapping("/transfer")
		public BankResponse transfer(@RequestBody TransferRequest request ){
			return userService.transfer(request);
		}
		
}
