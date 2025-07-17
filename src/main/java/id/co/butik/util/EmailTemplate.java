package id.co.butik.util;

import org.springframework.stereotype.Component;

@Component("emailTemplate")
public class EmailTemplate {
    public String getResetPassword(){
        return
                "<!DOCTYPE html\n" +
                        "\tPUBLIC \"-//W3C//DTD XHTML 1.0 Transitional //EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" +
                        "<html xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:o=\"urn:schemas-microsoft-com:office:office\" xmlns:v=\"urn:schemas-microsoft-com:vml\"><head>\n" +
                        "\t<meta content=\"text/html; charset=utf-8\" http-equiv=\"Content-Type\">\n" +
                        "\t<meta content=\"width=device-width\" name=\"viewport\">\n" +
                        "\t<meta content=\"IE=edge\" http-equiv=\"X-UA-Compatible\">\n" +
                        "  <link rel=\"preconnect\" href=\"https://fonts.gstatic.com\">\n" +
                        "    <link href=\"https://fonts.googleapis.com/css?family=Poppins:200,200i,300,300i,400,400i,500,500i,600,600i,700,700i,800,800i,900,900i&amp;display=swap\" rel=\"stylesheet\">\n" +
                        "\t<title></title>\n" +
                        "\t<style type=\"text/css\">\n" +
                        "\t\tbody {\n" +
                        "\t\t\tmargin: 0;\n" +
                        "\t\t\tpadding: 0;\n" +
                        "      font-size: 14px;\n" +
                        "      font-family: 'Poppins', sans-serif;\n" +
                        "\t\t}\n" +
                        "\n" +
                        "    div{\n" +
                        "      font-size: 14px;\n" +
                        "    }\n" +
                        "\n" +
                        "\t\ttable,\n" +
                        "\t\ttd,\n" +
                        "\t\ttr {\n" +
                        "\t\t\tvertical-align: top;\n" +
                        "\t\t\tborder-collapse: collapse;\n" +
                        "\t\t}\n" +
                        "\n" +
                        "\t\t* {\n" +
                        "\t\t\tline-height: inherit;\n" +
                        "\t\t}\n" +
                        "\n" +
                        "\t\ta[x-apple-data-detectors=true] {\n" +
                        "\t\t\tcolor: inherit !important;\n" +
                        "\t\t\ttext-decoration: none !important;\n" +
                        "\t\t}\n" +
                        "\t</style>\n" +
                        "\t<style id=\"media-query\" type=\"text/css\">\n" +
                        "\t\t@media (max-width: 920px) {\n" +
                        "\n" +
                        "\t\t\t.block-grid,\n" +
                        "\t\t\t.col {\n" +
                        "\t\t\t\tmin-width: 420px !important;\n" +
                        "\t\t\t\tmax-width: 100% !important;\n" +
                        "\t\t\t\tdisplay: block !important;\n" +
                        "\t\t\t}\n" +
                        "\n" +
                        "\t\t\t.block-grid {\n" +
                        "\t\t\t\twidth: 100% !important;\n" +
                        "\t\t\t}\n" +
                        "\n" +
                        "\t\t\t.col {\n" +
                        "\t\t\t\twidth: 100% !important;\n" +
                        "\t\t\t}\n" +
                        "\n" +
                        "\t\t\t.col>div {\n" +
                        "\t\t\t\tmargin: 0 auto;\n" +
                        "\t\t\t}\n" +
                        "\n" +
                        "\t\t\timg.fullwidth,\n" +
                        "\t\t\timg.fullwidthOnMobile {\n" +
                        "\t\t\t\tmax-width: 100% !important;\n" +
                        "\t\t\t}\n" +
                        "\n" +
                        "\t\t\t.no-stack .col {\n" +
                        "\t\t\t\tmin-width: 0 !important;\n" +
                        "\t\t\t\tdisplay: table-cell !important;\n" +
                        "\t\t\t}\n" +
                        "\n" +
                        "\t\t\t.no-stack.two-up .col {\n" +
                        "\t\t\t\twidth: 50% !important;\n" +
                        "\t\t\t}\n" +
                        "\n" +
                        "\t\t\t.no-stack .col.num4 {\n" +
                        "\t\t\t\twidth: 33% !important;\n" +
                        "\t\t\t}\n" +
                        "\n" +
                        "\t\t\t.no-stack .col.num8 {\n" +
                        "\t\t\t\twidth: 66% !important;\n" +
                        "\t\t\t}\n" +
                        "\n" +
                        "\t\t\t.no-stack .col.num4 {\n" +
                        "\t\t\t\twidth: 33% !important;\n" +
                        "\t\t\t}\n" +
                        "\n" +
                        "\t\t\t.no-stack .col.num3 {\n" +
                        "\t\t\t\twidth: 25% !important;\n" +
                        "\t\t\t}\n" +
                        "\n" +
                        "\t\t\t.no-stack .col.num {\n" +
                        "\t\t\t\twidth: 50% !important;\n" +
                        "\t\t\t}\n" +
                        "\n" +
                        "\t\t\t.no-stack .col.num9 {\n" +
                        "\t\t\t\twidth: 75% !important;\n" +
                        "\t\t\t}\n" +
                        "\n" +
                        "\t\t\t.video-block {\n" +
                        "\t\t\t\tmax-width: none !important;\n" +
                        "\t\t\t}\n" +
                        "\n" +
                        "\t\t\t.mobile_hide {\n" +
                        "\t\t\t\tmin-height: 0px;\n" +
                        "\t\t\t\tmax-height: 0px;\n" +
                        "\t\t\t\tmax-width: 0px;\n" +
                        "\t\t\t\tdisplay: none;\n" +
                        "\t\t\t\toverflow: hidden;\n" +
                        "\t\t\t\tfont-size: 0px;\n" +
                        "\t\t\t}\n" +
                        "\n" +
                        "\t\t\t.desktop_hide {\n" +
                        "\t\t\t\tdisplay: block !important;\n" +
                        "\t\t\t\tmax-height: none !important;\n" +
                        "\t\t\t}\n" +
                        "\t\t}\n" +
                        "\t</style>\n" +
                        "</head>\n" +
                        "\n" +
                        "<body class=\"clean-body\" style=\"margin: 0; padding: 0; -webkit-text-size-adjust: 100%; background-color: #ffffff;\">\n" +
                        "\t<table bgcolor=\"#ffffff\" cellpadding=\"0\" cellspacing=\"0\" class=\"nl-container\" role=\"presentation\" style=\"table-layout: fixed; vertical-align: top; min-width: 420px; Margin: 0 auto; border-spacing: 0; border-collapse: collapse; mso-table-lspace: 0pt; mso-table-rspace: 0pt; background-color: #ffffff; width: 100%;\" valign=\"top\" width=\"100%\">\n" +
                        "\t\t<tbody>\n" +
                        "\t\t\t<tr style=\"vertical-align: top;\" valign=\"top\">\n" +
                        "\t\t\t\t<td style=\"word-break: break-word; vertical-align: top;\" valign=\"top\">\n" +
                        "\t\t\t\t\t<div style=\"background-color:#2d317e;\">\n" +
                        "\t\t\t\t\t\t<div class=\"block-grid\" style=\"Margin: 0 auto; min-width: 420px; max-width: 900px; overflow-wrap: break-word; word-wrap: break-word; word-break: break-word; background-color: transparent;\">\n" +
                        "\t\t\t\t\t\t\t<div style=\"border-collapse: collapse;display: table;width: 100%;background-color:transparent; background-color: #2d317e\">\n" +
                        "\t\t\t\t\t\t\t\t<div class=\"col num12\" style=\"min-width: 420px; display: table-cell; vertical-align: top; width: 100%;\">\n" +
                        "\t\t\t\t\t\t\t\t\t<div style=\"width:100% !important;\">\n" +
                        "\t\t\t\t\t\t\t\t\t\t<div style=\"border-top:0px solid transparent; border-left:0px solid transparent; border-bottom:0px solid transparent; border-right:0px solid transparent; padding-top:5px; padding-bottom:5px; padding-right: 0px; padding-left: 0px;\">\n" +
                        "\t\t\t\t\t\t\t\t\t\t\t<div align=\"center\" class=\"img-container center fixedwidth\" style=\"padding-right: 20px;padding-left: 20px;\">\n" +
                        "\t\t\t\t\t\t\t\t\t\t\t\t<div style=\"font-size:1px;line-height:20px\">&nbsp;</div><img align=\"center\" alt=\"I'm an image\" border=\"0\" class=\"center fixedwidth\" src=\"https://apis.lakugan.monstercode.net/cdn/logo/request-payment-logo.jpeg\" style=\"text-decoration: none; -ms-interpolation-mode: bicubic; height: auto; border: 0; width: 100%; max-width: 140px; display: block;\" title=\"I'm an image\" width=\"140\">\n" +
                        "\t\t\t\t\t\t\t\t\t\t\t\t\t<div style=\"font-size:1px;line-height:20px\">&nbsp;</div>\n" +
                        "\t\t\t\t\t\t\t\t\t\t\t\t</div>\n" +
                        "\t\t\t\t\t\t\t\t\t\t\t</div>\n" +
                        "\t\t\t\t\t\t\t\t\t\t</div>\n" +
                        "\t\t\t\t\t\t\t\t\t</div>\n" +
                        "\t\t\t\t\t\t\t\t</div>\n" +
                        "\t\t\t\t\t\t\t</div>\n" +
                        "\t\t\t\t\t\t</div>\n" +
                        "\t\t\t\t\t</div>\n" +
                        "\t\t\t\t\t<div style=\"background-color:transparent;\">\n" +
                        "\t\t\t\t\t\t<div class=\"block-grid\" style=\"Margin: 0 auto; min-width: 420px; max-width: 900px; overflow-wrap: break-word; word-wrap: break-word; word-break: break-word; \">\n" +
                        "\t\t\t\t\t\t\t<div style=\"border-collapse: collapse;display: table;width: 100%;\">\n" +
                        "\t\t\t\t\t\t\t\t<div class=\"col num12\" style=\"min-width: 420px; max-width: 900px; display: table-cell; vertical-align: top; width: 900px;\">\n" +
                        "\t\t\t\t\t\t\t\t\t<div style=\"width:100% !important;\">\n" +
                        "\t\t\t\t\t\t\t\t\t\t<div style=\"border-top:0px solid transparent; border-left:0px solid transparent; border-bottom:0px solid transparent; border-right:0px solid transparent; padding-top:5px; padding-bottom:5px; padding-right: 0px; padding-left: 0px;\">\n" +
                        "\t\t\t\t\t\t\t\t\t\t\t<!--<![endif]-->\n" +
                        "\t\t\t\t\t\t\t\t\t\t\t<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"divider\" role=\"presentation\" style=\"table-layout: fixed; vertical-align: top; border-spacing: 0; border-collapse: collapse; mso-table-lspace: 0pt; mso-table-rspace: 0pt; min-width: 100%; -ms-text-size-adjust: 100%; -webkit-text-size-adjust: 100%;\" valign=\"top\" width=\"100%\">\n" +
                        "\t\t\t\t\t\t\t\t\t\t\t\t<tbody>\n" +
                        "\t\t\t\t\t\t\t\t\t\t\t\t\t<tr style=\"vertical-align: top;\" valign=\"top\">\n" +
                        "\t\t\t\t\t\t\t\t\t\t\t\t\t\t<td class=\"divider_inner\" style=\"word-break: break-word; vertical-align: top; min-width: 100%; -ms-text-size-adjust: 100%; -webkit-text-size-adjust: 100%; padding-top: 10px; padding-right: 10px; padding-bottom: 10px; padding-left: 10px;\" valign=\"top\">\n" +
                        "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"divider_content\" height=\"15\" role=\"presentation\" style=\"table-layout: fixed; vertical-align: top; border-spacing: 0; border-collapse: collapse; mso-table-lspace: 0pt; mso-table-rspace: 0pt; border-top: 0px solid transparent; height: 15px; width: 100%;\" valign=\"top\" width=\"100%\">\n" +
                        "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<tbody>\n" +
                        "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<tr style=\"vertical-align: top;\" valign=\"top\">\n" +
                        "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<td height=\"15\" style=\"word-break: break-word; vertical-align: top; -ms-text-size-adjust: 100%; -webkit-text-size-adjust: 100%;\" valign=\"top\"><span></span></td>\n" +
                        "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t</tr>\n" +
                        "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t</tbody>\n" +
                        "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t</table>\n" +
                        "\t\t\t\t\t\t\t\t\t\t\t\t\t\t</td>\n" +
                        "\t\t\t\t\t\t\t\t\t\t\t\t\t</tr>\n" +
                        "\t\t\t\t\t\t\t\t\t\t\t\t</tbody>\n" +
                        "\t\t\t\t\t\t\t\t\t\t\t</table>\n" +
                        "\t\t\t\t\t\t\t\t\t\t\t<div style=\"font-family:Poppins, Trebuchet MS, Lucida Grande, Lucida Sans Unicode, Lucida Sans, Tahoma, sans-serif;line-height:1.2;padding-top:10px;padding-right:10px;padding-left:10px;\">\n" +
                        "\t\t\t\t\t\t\t\t\t\t\t\t<div style=\"line-height: 1.2; font-size: 12px; ; font-family: Poppins, Trebuchet MS, Lucida Grande, Lucida Sans Unicode, Lucida Sans, Tahoma, sans-serif; mso-line-height-alt: 14px;\">\n" +
                        "\t\t\t\t\t\t\t\t\t\t\t\t\t<p style=\"font-size:14px\">Hai {{NAME}},</p>\n" +
                        "\t\t\t\t\t\t\t\t\t\t\t\t\t<p style=\"font-size:14px\">Masukan kode ini untuk merubah ulang kata sandi anda</p>\n" +
                        "\t\t\t\t\t\t\t\t\t\t\t\t\t<p style=\"font-size:14px\">{{PASS_TOKEN}}</p>\n" +
                        "\t\t\t\t\t\t\t\t\t\t\t\t</div>\n" +
                        "\t\t\t\t\t\t\t\t\t\t\t</div>\n" +
                        "\t\t\t\t\t\t\t\t\t\t</div>\n" +
                        "\t\t\t\t\t\t\t\t\t</div>\n" +
                        "\t\t\t\t\t\t\t\t</div>\n" +
                        "\t\t\t\t\t\t\t</div>\n" +
                        "\t\t\t\t\t\t</div>\n" +
                        "\t\t\t\t\t</div>\n" +
                        "\t\t\t\t\t<div style=\"background-color:transparent;\">\n" +
                        "\t\t\t\t\t\t<div class=\"block-grid\" style=\"Margin: 0 auto; min-width: 420px; max-width: 900px; overflow-wrap: break-word; word-wrap: break-word; word-break: break-word; \">\n" +
                        "\t\t\t\t\t\t\t<div style=\"border-collapse: collapse;display: table;width: 100%;\">\n" +
                        "\t\t\t\t\t\t\t\t<div class=\"col num12\" style=\"min-width: 420px; max-width: 900px; display: table-cell; vertical-align: top; width: 900px;\">\n" +
                        "\t\t\t\t\t\t\t\t\t<div style=\"width:100% !important;\">\n" +
                        "\t\t\t\t\t\t\t\t\t\t<div style=\"border-top:0px solid transparent; border-left:0px solid transparent; border-bottom:0px solid transparent; border-right:0px solid transparent; padding-top:5px; padding-bottom:5px; padding-right: 0px; padding-left: 0px;\">\n" +
                        "\t\t\t\t\t\t\t\t\t\t\t<!--<![endif]-->\n" +
                        "\t\t\t\t\t\t\t\t\t\t\t<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"divider\" role=\"presentation\" style=\"table-layout: fixed; vertical-align: top; border-spacing: 0; border-collapse: collapse; mso-table-lspace: 0pt; mso-table-rspace: 0pt; min-width: 100%; -ms-text-size-adjust: 100%; -webkit-text-size-adjust: 100%;\" valign=\"top\" width=\"100%\">\n" +
                        "\t\t\t\t\t\t\t\t\t\t\t\t<tbody>\n" +
                        "\t\t\t\t\t\t\t\t\t\t\t\t\t<tr style=\"vertical-align: top;\" valign=\"top\">\n" +
                        "\t\t\t\t\t\t\t\t\t\t\t\t\t\t<td class=\"divider_inner\" style=\"word-break: break-word; vertical-align: top; min-width: 100%; -ms-text-size-adjust: 100%; -webkit-text-size-adjust: 100%; padding-top: 10px; padding-right: 10px; padding-bottom: 10px; padding-left: 10px;\" valign=\"top\">\n" +
                        "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"divider_content\" height=\"15\" role=\"presentation\" style=\"table-layout: fixed; vertical-align: top; border-spacing: 0; border-collapse: collapse; mso-table-lspace: 0pt; mso-table-rspace: 0pt; border-top: 0px solid transparent; height: 15px; width: 100%;\" valign=\"top\" width=\"100%\">\n" +
                        "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<tbody>\n" +
                        "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<tr style=\"vertical-align: top;\" valign=\"top\">\n" +
                        "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<td height=\"15\" style=\"word-break: break-word; vertical-align: top; -ms-text-size-adjust: 100%; -webkit-text-size-adjust: 100%;\" valign=\"top\"><span></span></td>\n" +
                        "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t</tr>\n" +
                        "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t</tbody>\n" +
                        "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t</table>\n" +
                        "\t\t\t\t\t\t\t\t\t\t\t\t\t\t</td>\n" +
                        "\t\t\t\t\t\t\t\t\t\t\t\t\t</tr>\n" +
                        "\t\t\t\t\t\t\t\t\t\t\t\t</tbody>\n" +
                        "\t\t\t\t\t\t\t\t\t\t\t</table>\n" +
                        "\t\t\t\t\t\t\t\t\t\t\t<div style=\"font-family:Poppins, Trebuchet MS, Lucida Grande, Lucida Sans Unicode, Lucida Sans, Tahoma, sans-serif;line-height:1.2;padding-top:10px;padding-right:10px;padding-left:10px;\">\n" +
                        "\t\t\t\t\t\t\t\t\t\t\t\t<div style=\"line-height: 1.2; font-size: 12px; ; font-family: Poppins, Trebuchet MS, Lucida Grande, Lucida Sans Unicode, Lucida Sans, Tahoma, sans-serif; mso-line-height-alt: 14px;\">\n" +
                        "\t\t\t\t\t\t\t\t\t\t\t\t\t<p style=\"font-size:14px\">Apabila membutuhkan informasi lebih lanjut , silahkan menghubungi kami</p>\n" +
                        "\t\t\t\t\t\t\t\t\t\t\t\t\t<p style=\"font-size:14px\">melalui email di halo@lakugan.com atau 082180005188.</p>\n" +
                        "\t\t\t\t\t\t\t\t\t\t\t\t</div>\n" +
                        "\t\t\t\t\t\t\t\t\t\t\t</div>\n" +
                        "\t\t\t\t\t\t\t\t\t\t</div>\n" +
                        "\t\t\t\t\t\t\t\t\t</div>\n" +
                        "\t\t\t\t\t\t\t\t</div>\n" +
                        "\t\t\t\t\t\t\t</div>\n" +
                        "\t\t\t\t\t\t</div>\n" +
                        "\t\t\t\t\t</div>\n" +
                        "\t\t\t\t\t<div style=\"background-color:transparent; border-top: 2px solid #2d317e;margin-top: 100px;\">\n" +
                        "\t\t\t\t\t\t<div class=\"block-grid\" style=\"Margin: 0 auto; min-width: 420px; max-width: 900px; overflow-wrap: break-word; word-wrap: break-word; word-break: break-word; background-color: #ffffff;\">\n" +
                        "\t\t\t\t\t\t\t<div style=\"border-collapse: collapse;display: table;width: 100%;\">\n" +
                        "\t\t\t\t\t\t\t\t<div class=\"col num12\" style=\"min-width: 420px; max-width: 900px; display: table-cell; vertical-align: top; width: 900px;\">\n" +
                        "\t\t\t\t\t\t\t\t\t<div style=\"width:100% !important;\">\n" +
                        "\t\t\t\t\t\t\t\t\t\t<div style=\"border-top:0px solid transparent; border-left:0px solid transparent; border-bottom:0px solid transparent; border-right:0px solid transparent; padding-top:5px; padding-bottom:5px; padding-right: 0px; padding-left: 0px;\">\n" +
                        "\t\t\t\t\t\t\t\t\t\t\t<div style=\"font-family:Poppins, Trebuchet MS, Lucida Grande, Lucida Sans Unicode, Lucida Sans, Tahoma, sans-serif;line-height:1.5;padding-top:10px;padding-right:10px;padding-bottom:5px;padding-left:10px; text-align: center;\">\n" +
                        "\t\t\t\t\t\t\t\t\t\t\t\t<div style=\"line-height: 1.5; font-size: 14px; ; font-family: Poppins, Trebuchet MS, Lucida Grande, Lucida Sans Unicode, Lucida Sans, Tahoma, sans-serif; mso-line-height-alt: 18px;\">\n" +
                        "                          <h3 style=\"color:#2d317e;margin: 5px;\">\n" +
                        "                            PT. Virtu Inovasi Digital (Lakugan)\n" +
                        "                          </h3>\n" +
                        "                          <p style=\"color: #aaa;margin: 5px;\">Jl. Aren No 29 RT 007/03, Jati Pulo,\n" +
                        "                            Palmerah, Tomang, Jakarta Barat, Jakarta,\n" +
                        "                            11430, Indonesia</p>\n" +
                        "                          <p style=\"font-size: 12px;color: #aaa;margin: 5px;\">\n" +
                        "                            Email dikirim secara otomatis oleh sistem. Harap jangan membalas email ini.</p>\n" +
                        "\t\t\t\t\t\t\t\t\t\t\t\t\t<p style=\"font-size: 14px; line-height: 1.5; word-break: break-word; text-align: center; mso-line-height-alt: 21px; color: #2d317e;margin: 0;\">\n" +
                        "\t\t\t\t\t\t\t\t\t\t\t\t\t\t©{{YEARS_NOW}} PT. Virtu Inovasi Digital (Lakugan).</p>\n" +
                        "\t\t\t\t\t\t\t\t\t\t\t\t</div>\n" +
                        "\t\t\t\t\t\t\t\t\t\t\t</div>\n" +
                        "\t\t\t\t\t\t\t\t\t\t</div>\n" +
                        "\t\t\t\t\t\t\t\t\t</div>\n" +
                        "\t\t\t\t\t\t\t\t</div>\n" +
                        "\t\t\t\t\t\t\t</div>\n" +
                        "\t\t\t\t\t\t</div>\n" +
                        "\t\t\t\t\t</div>\n" +
                        "\t\t\t\t</td>\n" +
                        "\t\t\t</tr>\n" +
                        "\t\t</tbody>\n" +
                        "\t</table>\n" +
                        "\n" +
                        "\n" +
                        "\n" +
                        "</body></html>";
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
