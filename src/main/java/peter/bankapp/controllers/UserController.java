package peter.bankapp.controllers;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
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
import peter.bankapp.ultil.PDFGenerator;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import peter.bankapp.entity.User;
import peter.bankapp.repository.UserRepository;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    UserService userService;
	@Autowired
	UserRepository userRepository;

	   /**
     * Get all user.
     *
     * @return the ResponseEntity with status 200 (OK) and with body of the list of user
     */
    @GetMapping("/list")
    public List<User> getAllUser() {
        return userRepository.findAll();
    }
 
	
    @GetMapping(value = "/pdf",produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> customerReport() throws IOException {
	        List<User> user = userRepository.findAll();
	        

	        ByteArrayInputStream bis = PDFGenerator.customerPDFReport(user);

	        HttpHeaders headers = new HttpHeaders();
	        headers.add("Content-Disposition", "inline; filename=customers.pdf");

	        return ResponseEntity
	                .ok()
	                .headers(headers)
	                .contentType(MediaType.APPLICATION_PDF)
	                .body(new InputStreamResource(bis));
	    }
		
	
	
	@PostMapping("/createAccount")
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
