package peter.bankapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import peter.bankapp.dto.BankResponse;
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
	
	
	
}
