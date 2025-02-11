package org.example.app_nbp;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;

public class EmailSender {

    public void sendEmail(String recipentEmail, String currency, String bid, String ask) throws MessagingException {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        // Dane logowania
        final String username = "walutozaur@gmail.com";
        final String password = "bwvm saco nqdc ilmq";

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(username));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipentEmail));
        message.setSubject("Walutozaur");

        String htmlContent = """
                <div style="font-family: Arial, sans-serif; line-height: 1.5; padding: 20px; color: black;">
                              <p>Cześć! Tu Walutozaur :)</p>
                              <div style="background-image: url('https://cdn.pixabay.com/photo/2021/03/05/22/44/dinosaur-6072475_640.png');\s
                                          background-size: contain;\s
                                          background-repeat: no-repeat;\s
                                          background-position: center;\s
                                          padding: 20px;\s
                                          border: 1px solid #ddd;\s
                                          border-radius: 8px;\s
                                          margin: 20px 0;">
                                  <p>Kurs waluty <strong>""" + currency + """
                                  </strong></p>
                                  <p><strong>Kupno: </strong>""" + bid + """
                                  </p>
                                  <p><strong>Sprzedaż: </strong>""" + ask + """
                              </p>
                              </div>
                              <hr>
                              <footer style="font-size: 10px; color: gray;">
                                  <p>Pozdrawiamy,</p>
                                  <p>Zespół aplikacji Walutozaur</p>
                                  <p><a href="https://bitbucket.org/olazawadka/sadowskaskoczylaszawadka/">https://bitbucket.org/olazawadka/sadowskaskoczylaszawadka/</a></p>
                                  <p style="font-style: italic;">Ta wiadomość została wygenerowana automatycznie, prosimy na nią nie odpowiadać.</p>
                              </footer>
                          </div>
            """;
        message.setContent(htmlContent, "text/html; charset=utf-8");

        Transport.send(message);

    }

}
