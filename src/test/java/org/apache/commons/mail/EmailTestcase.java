package org.apache.commons.mail;

import static org.junit.Assert.*;

import java.util.Date;

import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMultipart;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class EmailTestcase {

	private EmailConcrete email;
	private static final String[] TEST_EMAILS  = {"abc@def.com", "def@abc.org", "ghi@jkl.io"};
	
	@Before
	public void setup() throws Exception {
		//Object for concrete class is needed to call methods of Email class
		email = new EmailConcrete();
	}
	
	@After
	public void teardown() throws Exception {
		
	}
	
	@Test
	public void testAddBcc() throws Exception {
		//Test error handling when passing a null string to addBcc()
		try {
			email.addBcc();
		}
		catch (EmailException e) {
			System.out.println("testAddBcc: ");
			System.out.println(e.getMessage());
			System.out.println();
		}
		assertEquals(0, email.getBccAddresses().size());
		
		//Test passing multiple emails to addBCc
		email.addBcc(TEST_EMAILS);
		try {
			InternetAddress address1 = new InternetAddress("abc@def.com");
			InternetAddress address2 = new InternetAddress("def@abc.org");
			InternetAddress address3 = new InternetAddress("ghi@jkl.io");
			
			//Check bccList content and size
			assertEquals(3, email.getBccAddresses().size());
			assertEquals(address1, email.getBccAddresses().get(0));
			assertEquals(address2, email.getBccAddresses().get(1));
			assertEquals(address3, email.getBccAddresses().get(2));
		}
		catch (Exception e) {
			System.out.println("testAddBcc: ");
			System.out.println(e.getMessage());
			System.out.println();
		}
	}
	
	@Test
	public void testAddCc() throws Exception {
		
		//Similar to test Bcc
		try {
			email.addCc();
		}
		catch (EmailException e) {
			System.out.println("Test AddCc: ");
			System.out.println(e.getMessage());
			System.out.println();
		}
		assertEquals(0, email.getCcAddresses().size());
		
		//Passing only one email to addCc and check CcList content and size
		email.addCc("email@email.com");
		try {
			InternetAddress address = new InternetAddress("email@email.com");
			assertEquals(1, email.getCcAddresses().size());
			assertEquals(address, email.getCcAddresses().get(0));
		}
		catch (Exception e) {
			System.out.println("Test AddCc: ");
			System.out.println(e.getMessage());
			System.out.println();
		}
	}
	
	@Test
	public void testAddHeader() throws Exception{
		//Test error handling of addHeader when passing only one 
		//of the two required arguments
		try {
			email.addHeader("newHeader", "");
		}
		catch (Exception e) {
			System.out.println("Test addHeader: ");
			System.out.println(e.getMessage());
			System.out.println();
		}
		
		try {
			email.addHeader("", "this header");
		}
		catch (Exception e) {
			System.out.println("Test addHeader: ");
			System.out.println(e.getMessage());
			System.out.println();
		}
		
		//Test addHeader and check Header map element
		email.addHeader("newHeader", "this header");
		assertEquals(email.headers.get("newHeader"), "this header");
	}
	
	@Test
	public void testAddReplyTo() throws Exception {
		//Test addReply to
		try {
			email.addReplyTo("abc@def.com", "Nathan");
		}
		catch (Exception e) {
			System.out.println("Test addReplyTo: ");
			System.out.println(e.getMessage());
			System.out.println();
		}
		
		//Assert only works with two values of the same type, so we need
		//to create an InternetAddress with the same name and email as
		//the parameters passed to addHeader
		InternetAddress address = new InternetAddress("abc@def.com", "Nathan");
		assertEquals(1, email.getReplyToAddresses().size());
		assertEquals(address, email.getReplyToAddresses().get(0));
	}
	
	@Test
	public void testGetHostName() throws Exception {
		//Test getHostname without a prior hostname setup
		String hostname = email.getHostName();
		
		//Test getHostname with setup hostname but no session
		email.setHostName("ddoan");
		hostname = email.getHostName();
		assertEquals(hostname, "ddoan");
		
		//Test getHostname with both hostname and session setup
		email.setHostName("google");
		Session testSession = email.getMailSession();
		hostname = email.getHostName();
		assertEquals(hostname, "google");
	}
	
	@Test
	public void testGetSession() throws Exception {
		Session newSession;
		//Test getMailSession without any setup
		try {
			newSession = email.getMailSession();
		}
		catch (Exception e) {
			System.out.println("Test getSession: ");
			System.out.println(e.getMessage());
			System.out.println();
		}
		
		//Test getMailSession with authenticator, bounceAddress,
		//SSLCheckServerIdentity = true, setSSLOnConnect = true
		//and a hostname
		try {
			email.setAuthentication("thisUsername", "thisPassword");
			email.setBounceAddress("ddoan@email.com");
			email.setSSLCheckServerIdentity(true);
			email.setSSLOnConnect(true);
			email.setHostName("google");
			newSession = email.getMailSession();
			
		}
		catch (Exception e) {
			System.out.println("Test getSession: ");
			System.out.print(e.getMessage());
			System.out.println();
		}
	}
	
	@Test
	public void testGetSentDate() throws Exception {
		//Newly created Date object will have the default time as now
		Date newDate = new Date();
		
		//Without setting a sentDate, getSentDate should return the current time
		try {
			assertEquals(newDate,email.getSentDate());
		}
		catch (Exception e) {
			System.out.println("Test getSentDate: ");
			System.out.println(e.getMessage());
			System.out.println();
		}
		
		//Test setSentDate with getSentDate
		newDate.setTime(375123000);
		email.setSentDate(newDate);
		assertEquals(newDate, email.getSentDate());
		
	}
	
	@Test
	public void testGetConnectionTimeout() throws Exception {
		
		//Default timeout for SocketConnectionTimeout is 60000
		int timeout;
		try {
			timeout = email.getSocketConnectionTimeout();
			assertEquals(60000, timeout);
		}
		catch (Exception e) {
			System.out.println("Test getConnectionTimeout: ");
			System.out.println(e.getMessage());
			System.out.println();
		}
		
		//Test setter and getter for SocketConnectionTimeout
		email.setSocketConnectionTimeout(10);
		timeout = email.getSocketConnectionTimeout();
		assertEquals(10, timeout);
	}
	
	@Test
	public void testSetFrom() throws Exception {
		
		//Test error handler
		try {
			email.setFrom(null);
		}
		catch (Exception e) {
			System.out.println("Test setFrom: ");
			System.out.println(e.getMessage());
			System.out.println();
		}
		
		//Test setter and getter with an InternetAddress object
		email.setFrom("ddoan@email.com");
		assertEquals(email.getFromAddress(), new InternetAddress("ddoan@email.com"));
	}
	
	@Test
	public void testBuildMimeMessage() throws Exception {
		
		//Test error handler when from address is not set
		try {
			email.setHostName("thisHostname");
			email.addHeader("ddoan", "ddoan@email.com");
			email.setSubject("some subject");
			email.buildMimeMessage();
		}
		catch (Exception e) {
			System.out.println("Test buildMimeMessage: ");
			System.out.println(e.getMessage());
			System.out.println();
		}
		
		//Test error handler when no receiver is set
		try {
			email.message = null;
			email.setFrom("thisemail@email.com");
			email.buildMimeMessage();
		}
		catch (Exception e) {
			System.out.println("Test buildMimeMessage: ");
			System.out.println(e.getMessage());
			System.out.println();
		}
		
		//Test building mime message with minimum necessary components
		try {
			email.message = null;
			email.addBcc("abc@def.com");
			email.addCc("def@abc.org");
			email.addTo("receiver@email.com");
			email.addReplyTo("reply@email.com");
			email.buildMimeMessage();
		}
		catch (Exception e) {
			System.out.println("Test buildMimeMessage: ");
			System.out.println(e.getMessage());
			System.out.println();
		}
		
		//Test building mime message with a body
		try {
			MimeMultipart newBody = new MimeMultipart("text/plain");
			email.setContent(newBody);
			email.setCharset("UTF-16BE");
			email.message = null;
			email.buildMimeMessage();
		}
		catch (Exception e) {
			System.out.println("Test buildMimeMessage: ");
			System.out.println(e.getMessage());
			System.out.println();
		}
		
		//Test error handler when the mime message is already built
		try {
			email.buildMimeMessage();
		}
		catch (Exception e) {
			System.out.println("Test buildMimeMessage: ");
			System.out.println(e.getMessage());
			System.out.println();
		}
	}

}
