/*
 * Copyright (C) 2014, 2015 Sanjay Madnani
 *
 * This file is free to use: you can redistribute it and/or modify it under the terms of the 
 * GPL General Public License V2 as published by the Free Software Foundation, subject to the following conditions:
 *                                                                                          
 * The above copyright notice should never be changed and should always included wherever this file is used.
 *                                                                                          
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY.  
 * See the GNU General Public License V2 for more details.                                       
 *
 */
package com.sanjay.communication.helper;

import java.io.File;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.BodyPart;
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

/**
 * Helper for sending mail to list of users with attachment.
 * 
 * @author SANJAY
 * @see for list of mail servers and configuration details: http://sanjaymadnani.wordpress.com/
 */
public class CommunicationHelper {
	/**
	 * USER_ID String email-Id for sending email.
	 */
	private final String USER_ID = "email address";
	/**
	 * USER_PASSWORD String email-Password for sending email.
	 */
	private final String USER_PASSWORD = "password";

	/**
	 * Creates SMTP Session required for sending mail.
	 * 
	 * @return Session.
	 */
	private Session getSendMailSession() {
		// Configurable Properties (as per Mail server).
		// SMTP_HOST ex: smtp.gmail.com
		final String SMTP_HOST = "smtp.gmail.com";
		// SMTP_PORT ex: 465 (gmail with SSL authentication), 587 (gmail with StartTLS authentication).
		final String SMTP_PORT = "465";
		// IS_AUTHENTICATION_REQUIRED for most of the mail server authentication is required.
		final String IS_AUTHENTICATION_REQUIRED = "true";
		// IS_STARTTLS_ENABLE ex: gmail & outlook mail can be send via startTLS authentication.
		// all servers don't use startTLS authentication like yahoo mail server don't have this authentication.
		final String IS_STARTTLS_ENABLE = "false";
		// IS_SSL_ENABLE ex: gmail mail can be send either by SSL or by startTLS.
		// For yahoo mail server it is compulsory to use SSL authentication for sending mail.
		final String IS_SSL_ENABLE = "true";

		final String IS_DEBUG = "false";

		Properties props = new Properties();
		props.put("mail.smtp.host", SMTP_HOST);
		props.put("mail.smtp.auth", IS_AUTHENTICATION_REQUIRED);
		props.put("mail.debug", IS_DEBUG);
		props.put("mail.smtp.ssl.enable", IS_SSL_ENABLE);
		props.put("mail.smtp.starttls.enable", IS_STARTTLS_ENABLE);
		props.put("mail.smtp.port", SMTP_PORT);
		if (IS_STARTTLS_ENABLE.equals("false") && IS_SSL_ENABLE.equals("true")) {
			props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		}
		Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(USER_ID, USER_PASSWORD);
			}
		});
		return session;
	}

	/**
	 * Sends Mail to list of user(to and cc list) with attachment. send mails to list of e-mail ids with list of carbon copy ids via secure socket
	 * layer Authentication. Simple mail Transfer Protocol is used.
	 * 
	 * @param toList
	 *            List of email-Ids.
	 * @param ccList
	 *            (Optional) List of email-Ids, can be null.
	 * @param msgSubject
	 *            String subject line of mail.
	 * @param msgBody
	 *            String TEXT/PLAIN or HTML/PLAIN message to deliver.
	 * @param file
	 *            (Optional) File to send as attachment.
	 * @throws MessagingException.
	 */
	public boolean sendMail(List<String> toList, List<String> ccList, String msgSubject, String msgBody, File file) throws MessagingException {
		// Creates MimeMessage by SMTP Session.
		final MimeMessage message = new MimeMessage(this.getSendMailSession());
		message.setFrom(new InternetAddress(USER_ID));
		final Address[] toAddress = new InternetAddress[toList.size()];
		for (int i = 0; i < toAddress.length; i++) {
			toAddress[i] = new InternetAddress(toList.get(i));
		}
		// sets the to list for sending mail.
		message.addRecipients(Message.RecipientType.TO, toAddress);
		if (ccList != null && ccList.size() > 0) {
			final Address[] toCcAddress = new InternetAddress[ccList.size()];
			for (int i = 0; i < toCcAddress.length; i++) {
				toCcAddress[i] = new InternetAddress(ccList.get(i));
			}
			// sets the cc list for sending mail. the same way BCC list can also be added.
			message.addRecipients(Message.RecipientType.CC, toCcAddress);
		}
		final Multipart multipart = new MimeMultipart();
		// Attach a file with mail if present.
		if (file != null) {
			final MimeBodyPart messageBodyPart2 = new MimeBodyPart();
			final DataSource source = new FileDataSource(file);
			messageBodyPart2.setDataHandler(new DataHandler(source));
			messageBodyPart2.setFileName(file.getName());
			multipart.addBodyPart(messageBodyPart2);
		}

		message.setSubject(msgSubject);
		message.setSentDate(new java.util.Date());
		final BodyPart messageBodyPart1 = new MimeBodyPart();
		// Sends the message in html format.
		messageBodyPart1.setContent(msgBody, "text/html");
		multipart.addBodyPart(messageBodyPart1);

		message.setContent(multipart);
		Transport.send(message);
		return true;
	}
}
