package vn.vanquang239dn.service;

import java.io.IOException;

public interface EmailService {

    public void sendEmail(String receiverEmail, String subject, String text) throws IOException;

}
