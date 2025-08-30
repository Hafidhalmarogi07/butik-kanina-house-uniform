package id.co.butik.util;

import org.springframework.stereotype.Component;

@Component("emailTemplate")
public class EmailTemplate {
    public String getResetPassword() {
        return "<!DOCTYPE html>\n" +
                "<html lang=\"id\">\n" +
                "<head>\n" +
                "  <meta charset=\"UTF-8\">\n" +
                "  <title>Reset Password OTP</title>\n" +
                "</head>\n" +
                "<body style=\"font-family: Arial, sans-serif; background-color: #f9fafb; padding: 20px;\">\n" +
                "\n" +
                "  <table align=\"center\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" style=\"max-width: 600px; background: #ffffff; border-radius: 10px; box-shadow: 0 4px 12px rgba(0,0,0,0.1); overflow: hidden;\">\n" +
                "    <tr>\n" +
                "      <td style=\"background: #2563eb; padding: 20px; text-align: center; color: #ffffff; font-size: 20px; font-weight: bold;\">\n" +
                "        Reset Password - OTP Verification\n" +
                "      </td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td style=\"padding: 30px; color: #111827; font-size: 15px; line-height: 1.6;\">\n" +
                "        <p>Halo <b>{{namaUser}}</b>,</p>\n" +
                "        <p>Kami menerima permintaan untuk mereset password akun Anda. Gunakan kode OTP berikut untuk melanjutkan proses reset password:</p>\n" +
                "        \n" +
                "        <div style=\"text-align: center; margin: 30px 0;\">\n" +
                "          <span style=\"font-size: 32px; letter-spacing: 6px; font-weight: bold; color: #2563eb;\">\n" +
                "            {{otpCode}}\n" +
                "          </span>\n" +
                "        </div>\n" +
                "\n" +
                "        <p>Kode OTP ini hanya berlaku selama <b>5 menit</b>. Jangan berikan kode ini kepada siapa pun untuk alasan keamanan.</p>\n" +
                "        <p>Jika Anda tidak meminta reset password, abaikan email ini.</p>\n" +
                "\n" +
                "        <p style=\"margin-top: 30px;\">Terima kasih,<br>Tim Support</p>\n" +
                "      </td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td style=\"background: #f3f4f6; padding: 15px; text-align: center; font-size: 12px; color: #6b7280;\">\n" +
                "        &copy; {{currentYear}} Aplikasi Anda. All rights reserved.\n" +
                "      </td>\n" +
                "    </tr>\n" +
                "  </table>\n" +
                "\n" +
                "</body>\n" +
                "</html>\n";
    }

    public String getEmailRegister(){
       return  "<!DOCTYPE html>\n" +
               "<html>\n" +
               "<head>\n" +
               "  <meta charset=\"UTF-8\">\n" +
               "  <style>\n" +
               "    body {\n" +
               "      font-family: 'Segoe UI', sans-serif;\n" +
               "      background-color: #f2f4f6;\n" +
               "      margin: 0;\n" +
               "      padding: 0;\n" +
               "    }\n" +
               "\n" +
               "    .container {\n" +
               "      max-width: 600px;\n" +
               "      margin: 40px auto;\n" +
               "      background-color: #ffffff;\n" +
               "      border-radius: 10px;\n" +
               "      overflow: hidden;\n" +
               "      box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);\n" +
               "    }\n" +
               "\n" +
               "    .header {\n" +
               "      background-color: #007BFF;\n" +
               "      color: white;\n" +
               "      padding: 20px;\n" +
               "      text-align: center;\n" +
               "    }\n" +
               "\n" +
               "    .header h2 {\n" +
               "      margin: 0;\n" +
               "    }\n" +
               "\n" +
               "    .content {\n" +
               "      padding: 30px 20px;\n" +
               "      color: #333;\n" +
               "    }\n" +
               "\n" +
               "    .info-box {\n" +
               "      background-color: #f1f1f1;\n" +
               "      padding: 15px 20px;\n" +
               "      border-left: 5px solid #007BFF;\n" +
               "      margin: 20px 0;\n" +
               "      border-radius: 5px;\n" +
               "    }\n" +
               "\n" +
               "    .info-box p {\n" +
               "      margin: 8px 0;\n" +
               "      font-size: 15px;\n" +
               "    }\n" +
               "\n" +
               "    .footer {\n" +
               "      background-color: #f9f9f9;\n" +
               "      padding: 15px;\n" +
               "      text-align: center;\n" +
               "      font-size: 12px;\n" +
               "      color: #999;\n" +
               "    }\n" +
               "\n" +
               "    .button {\n" +
               "      display: inline-block;\n" +
               "      margin-top: 20px;\n" +
               "      padding: 10px 20px;\n" +
               "      background-color: #007BFF;\n" +
               "      color: white;\n" +
               "      text-decoration: none;\n" +
               "      border-radius: 5px;\n" +
               "      font-weight: bold;\n" +
               "    }\n" +
               "\n" +
               "    .button:hover {\n" +
               "      background-color: #0056b3;\n" +
               "    }\n" +
               "  </style>\n" +
               "</head>\n" +
               "<body>\n" +
               "  <div class=\"container\">\n" +
               "    <div class=\"header\">\n" +
               "      <h2>Akun Admin Baru Telah Dibuat</h2>\n" +
               "    </div>\n" +
               "    <div class=\"content\">\n" +
               "      <p>Halo <strong>{{admin_name}}</strong>,</p>\n" +
               "      <p>Super Admin telah membuatkan akun untuk Anda pada sistem <strong>Butik</strong>. Berikut informasi akun Anda:</p>\n" +
               "\n" +
               "      <div class=\"info-box\">\n" +
               "        <p><strong>Email:</strong> {{email}}</p>\n" +
               "        <p><strong>Password:</strong> {{password}}</p>\n" +
               "      </div>\n" +
               "\n" +
               "      <p>Silakan login menggunakan informasi di atas dan segera ganti password Anda untuk menjaga keamanan akun.</p>\n" +
               "\n" +
               "      <a href=\"{{login_url}}\" class=\"button\">Login Sekarang</a>\n" +
               "\n" +
               "      <p style=\"margin-top: 20px;\">Jika Anda tidak mengenali pendaftaran ini, harap hubungi tim support kami secepatnya.</p>\n" +
               "    </div>\n" +
               "    <div class=\"footer\">\n" +
               "      &copy; {{year}} Butik System. Semua hak dilindungi.\n" +
               "    </div>\n" +
               "  </div>\n" +
               "</body>\n" +
               "</html>\n";
    }
}
