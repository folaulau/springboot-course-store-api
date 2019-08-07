package com.springboot.ntc.email;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderService {
	private Logger log = LoggerFactory.getLogger(this.getClass());

	@Value("${spring.mail.properties.mail.sender}")
	private String sender;

	@Autowired
	private JavaMailSender mailSender;

	private String personal = "Monomono Store";
	
	/**
	 * Send Mail
	 * subject,body, and recipient email
	 * @param email
	 * @return
	 */
	public DeliveryStatus sendEmail(Email email) {
		return sendEmail(email.getSendTo(), email.getSubject(), email.getMessage(), email.getBlindCopies(), email.getCarbonCopies());
	}

	public DeliveryStatus sendEmail(String sendTo, String subject, String content, List<String> bccs, List<String> ccs) {
		log.debug("sending mail...");
		MimeMessage message = mailSender.createMimeMessage();

		try {

			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setSubject(subject);
			helper.setTo(sendTo);
			helper.setText(content, true);
			helper.setFrom(new InternetAddress(sender, personal));
			// cc
			this.addBcc(bccs, helper);
			// bcc
			this.addCc(ccs, helper);

			mailSender.send(message);
			log.info("\nmail sent to " + sendTo + " about " + subject + "\n");
			return new DeliveryStatus(true, DeliveryStatus.DELIVERED);
		} catch (MessagingException | UnsupportedEncodingException e) {
			log.warn("Exception, mail not. msg: " + e.getMessage());
			return new DeliveryStatus(false, e.getLocalizedMessage());
		} catch (Exception e) {
			
			return new DeliveryStatus(false, e.getLocalizedMessage());
		}
	}

	public void reportBadEmail(String msg) {
		log.info("reporting bad email...");
	}

	private void addBcc(List<String> bccs, MimeMessageHelper helper) {
		try {
			
			if (bccs != null && bccs.size()>0) {
				helper.addBcc(new InternetAddress(sender));
				for (String bcc :bccs) {
					if(bcc!=null && bcc.length()>0) {
						helper.addBcc(new InternetAddress(bcc));
					}
				}
			}

		} catch (MessagingException e) {
			log.error("Bcc could not be done. MessagingException msg: " + e.getLocalizedMessage());
		}
	}

	private void addCc(List<String> ccs, MimeMessageHelper helper) {
		try {
			
			if (ccs != null && ccs.size()>0) {
				helper.addCc(new InternetAddress(sender));
				for (String cc : ccs) {
					if(cc!=null && cc.length()>0) {
						helper.addCc(new InternetAddress(cc));
					}
				}
			}

		} catch (MessagingException e) {
			log.error("Bcc could not be done. MessagingException msg: " + e.getLocalizedMessage());
		}
	}
}
