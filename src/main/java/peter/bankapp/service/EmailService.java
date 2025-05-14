package peter.bankapp.service;

import peter.bankapp.dto.EmailDetails;

public interface EmailService {
	void sendEmailAlert(EmailDetails emailDetails);

}
