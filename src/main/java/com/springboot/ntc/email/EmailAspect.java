package com.springboot.ntc.email;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


@Aspect
public class EmailAspect {
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private EmailSenderService emailSenderService;
	
	/**
	 * @author folaukaveinga
	 * @param jp JoinPoint
	 * @param mail Mail
	 * @return DeliveryStatus
	 */
	@Around("execution(* com.monomono.ntc.email.EmailSenderService.sendEmail(com.monomono.ntc.email.Email)) && args(email)")
	public DeliveryStatus logMail(final ProceedingJoinPoint jp, Email email) {
		log.info("aspect ==> log mail");
		DeliveryStatus status = null;
		try {
			status = (DeliveryStatus) jp.proceed();
			if(status.isDelivered()==false){
				StringBuilder errorMsg = new StringBuilder("<h2>Iate Bad Email</h2>");
				errorMsg.append("This email could not be sent.");
				errorMsg.append("<br/>");
				errorMsg.append("Receiver email: "+email.getSendTo());
				errorMsg.append("<br/>");
				errorMsg.append("Error message: "+status.getMessage());
				errorMsg.append("<br/><br/>");
				errorMsg.append("<h3>Email content below</h3>");
				errorMsg.append("<hr>");
				errorMsg.append(email.getMessage());
				log.error(errorMsg.toString());
				emailSenderService.reportBadEmail(errorMsg.toString());
			}
			log.error("status: "+status);
		} catch (final Throwable e) {
			log.error(e.getLocalizedMessage());
		}
		return status;
	}
	
	
}