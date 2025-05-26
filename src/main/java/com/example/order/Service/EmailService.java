package com.example.order.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.example.order.Entity.AddressEntity;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;


@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

     public void sendOrderConfirmationMail(String toEmail, String customerName,
                                                          String productName,AddressEntity addressEntity
                                                ) throws MessagingException {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setFrom("ams.project.105@gmail.com");
        helper.setTo(toEmail);
        helper.setSubject("Order Confirmation");

       

        StringBuilder html = new StringBuilder();
        html.append("<h3>Hello ").append(customerName).append(",</h3>");
        html.append("<p>Thank you for your order! Here are the details:</p>");
        html.append("<h4>Product:</h4>");
        html.append("<p>").append(productName).append("</p>");

       
        String formattedAddress = formatAddress(addressEntity);

        html.append("<h4>Shipping Address:</h4>");
        html.append("<p>").append(formattedAddress).append("</p>");
       
        html.append("<br><p>Best regards,<br>Mobifree</p>");

        helper.setText(html.toString(), true);

        mailSender.send(message);
    }

    private String formatAddress(AddressEntity address) {
    if (address == null) return "";

    StringBuilder sb = new StringBuilder();

    if (address.getUserName() != null && !address.getUserName().isEmpty()) {
        sb.append("Name: ").append(address.getUserName()).append("<br>");
    }
    if (address.getPhoneNumber() != null) {
        sb.append("Phone: ").append(address.getPhoneNumber()).append("<br>");
    }
    if (address.getHouseNumber() != null && !address.getHouseNumber().isEmpty()) {
        sb.append(address.getHouseNumber()).append(",<br>");
    }
    if (address.getStreetName() != null && !address.getStreetName().isEmpty()) {
        sb.append(address.getStreetName()).append(",<br>");
    }
    if (address.getCity() != null && !address.getCity().isEmpty()) {
        sb.append(address.getCity()).append(",<br>");
    }
    if (address.getDistrict() != null && !address.getDistrict().isEmpty()) {
        sb.append(address.getDistrict()).append(",<br>");
    }
    if (address.getState() != null && !address.getState().isEmpty()) {
        sb.append(address.getState()).append(" - ");
    }
    if (address.getPincode() != null && !address.getPincode().isEmpty()) {
        sb.append(address.getPincode());
    }

    return sb.toString();
}

}
