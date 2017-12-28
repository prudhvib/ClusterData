package com.infinote.commonapi;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;

public class MailingSystem {
	JavaProperties selProperties = new JavaProperties();
	String host,from,to,attachment;
	
	public void setMailProperties(JavaProperties selProperties){
		
		host=selProperties.getPropertyValue("SMTP_HOST");
		from=selProperties.getPropertyValue("FROM_EMAIL");
		to=selProperties.getPropertyValue("TO_EMAIL");
		//attachment=selProperties.getMessage("ATTACH_FILE");
	}
	
	public void sendMail(String subject, String bodyText){
		Properties mailProps = new Properties();
		mailProps.put("mail.smtp.host", "smtp.gmail.com");
		mailProps.put("mail.smtp.port", "587");
		mailProps.put("mail.smtp.starttls.enable", "true");
		/*mailProps.setProperty("mail.smtp.user", "pkr.bondu@gmail.com");
		mailProps.setProperty("mail.smtp.password", "!luMm1986");*/
		mailProps.setProperty("mail.smtp.auth", "true");
		String toEmail = "pkr.bondu1985@gmail.com";
		String fromEmail = "prudhvi@infinote.com";
//		subject = "Cloud Server not accessible";
		String password="Password0!";
		
		//create Authenticator object to pass in Session.getInstance argument
        Authenticator auth = new Authenticator() {
            //override the getPasswordAuthentication method
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        };
		
		Session session = Session.getDefaultInstance(mailProps, auth);
		Message msg = new MimeMessage(session);
		try{
			msg.setFrom(new InternetAddress(fromEmail));
			msg.setRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
			msg.setSubject(subject);
			msg.setText(bodyText);
			Transport.send(msg);
			System.out.println("Sent Mail Successfully...");
		}catch(MessagingException e){
			System.out.println(e);
		}
	}
	
	public void sendMail(String subject, String bodyText, String toEmail, String fromEmail, String password){
		Properties mailProps = new Properties();
		mailProps.put("mail.smtp.host", "smtp.gmail.com");
		mailProps.put("mail.smtp.port", "587");
		mailProps.put("mail.smtp.starttls.enable", "true");
		mailProps.setProperty("mail.smtp.auth", "true");
		/*String toEmail = "pkr.bondu1985@gmail.com";
		String fromEmail = "prudhvi@infinote.com";
		String password="Password0!";*/
		
		//create Authenticator object to pass in Session.getInstance argument
        Authenticator auth = new Authenticator() {
            //override the getPasswordAuthentication method
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        };
		
		Session session = Session.getDefaultInstance(mailProps, auth);
		Message msg = new MimeMessage(session);
		try{
			msg.setFrom(new InternetAddress(fromEmail));
			msg.setRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
			msg.setSubject(subject);
			msg.setText(bodyText);
			Transport.send(msg);
			System.out.println("Sent Mail Successfully...");
		}catch(MessagingException e){
			System.out.println(e);
		}
	}
}
