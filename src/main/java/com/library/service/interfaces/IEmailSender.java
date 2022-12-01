package com.library.service.interfaces;

public interface IEmailSender {
    void send(String to,String text,String subject);
    String buildEmail(String name,String link,String subject);
}
