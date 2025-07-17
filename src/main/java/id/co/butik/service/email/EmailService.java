package id.co.butik.service.email;

import id.co.butik.util.EmailTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class EmailService {

    private final EmailTemplate emailTemplate;
    private final EmailSender emailSender;

    @Value("${base.url}")
    private String baseUrl;


    public EmailService(EmailTemplate emailTemplate, EmailSender emailSender) {
        this.emailTemplate = emailTemplate;
        this.emailSender = emailSender;
    }

    public Map<String, Boolean> sendRegisterUser(String adminName, String email, String password) {
        Map<String, Boolean> map = new HashMap<>();
        String template = emailTemplate.getEmailRegister();
        Date tanggalSekarang = new Date();
        LocalDate localDateNow = tanggalSekarang.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        int yearNow  = localDateNow.getYear();
        template = template.replaceAll("\\{\\{admin_name}}",adminName);
        template = template.replaceAll("\\{\\{email}}", email);
        template = template.replaceAll("\\{\\{password}}",password);
        template = template.replaceAll("\\{\\{year}}",String.valueOf(yearNow));
        template = template.replaceAll("\\{\\{login_url}}",baseUrl+"/login");
        try {
            emailSender.sendAsync(email,"Akun Admin Anda Telah Dibuat - Informasi Login", template);

            map.put("success", true);
        }catch (Exception e){
            map.put("success", false);
        }
        return map;

    }
}
