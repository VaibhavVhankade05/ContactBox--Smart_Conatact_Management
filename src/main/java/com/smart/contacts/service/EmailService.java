package com.smart.contacts.service;

public interface EmailService {
	void sendEmail(String to, String subject, String body);

    //
    void sendEmailWithHtml();

    //
    void sendEmailWithAttachment();
}
