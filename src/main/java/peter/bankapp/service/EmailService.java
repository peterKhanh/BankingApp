package peter.bankapp.service;

import org.springframework.stereotype.Service;

import peter.bankapp.dto.EmailDetails;

public interface EmailService {
	void sendEmailAlert(EmailDetails emailDetails);

}
