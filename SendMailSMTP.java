package warehouse;

import java.util.Properties;

import javax.activation.CommandMap;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.activation.MailcapCommandMap;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class SendMailSMTP {
	String host = "smtp.naver.com";
	private String user = "deuackr2017@naver.com"; // 발신자 아이디
	private String password = "deu@2017"; // 발신자 비밀번호

	public SendMailSMTP(String toEmail, String toTitle, String filePath, String setMessage) {
		SMTP(toEmail, toTitle, filePath, setMessage);
	}

	public void SMTP(String toEmail, String toTitle, String filePath, String setMessage) {
		// Get the session object
		Properties props = new Properties();
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.auth", "true");

		Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(user, password);
			}
		});

		// Compose the message
		try {
			// 메일 헤더
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(user));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
			message.setSubject(toTitle);// 메일 주제
		
			// 메일 내용
			MimeBodyPart mbp1 = new MimeBodyPart();
			mbp1.setContent(setMessage, "text/html; charset=UTF-8");
			// 파일 읽기
			MimeBodyPart mbp2 = new MimeBodyPart();
			FileDataSource fds = new FileDataSource(filePath); // 파일 읽어오기
			mbp2.setDataHandler(new DataHandler(fds)); // 파일 첨부
			mbp2.setFileName(fds.getName());
			// 각각의 헤더 및 바디 multipart에 추가
			Multipart messageMulti = new MimeMultipart();
			messageMulti.addBodyPart(mbp1);
			if(!filePath.equals("")){
			messageMulti.addBodyPart(mbp2);	
			}
			message.setContent(messageMulti);
			
			// 첨부할 파일 확장자 정의
			MailcapCommandMap MailcapCmdMap = (MailcapCommandMap) CommandMap.getDefaultCommandMap();
			MailcapCmdMap.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
			MailcapCmdMap.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
			MailcapCmdMap.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
			MailcapCmdMap.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
			MailcapCmdMap.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822");
			CommandMap.setDefaultCommandMap(MailcapCmdMap);
			// send the message
			Transport.send(message);
			System.out.println("message sent successfully...");

		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
}
