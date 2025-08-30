package id.co.butik.dto;

import lombok.Data;

@Data
public class ForgotRequest {

    String email;

    String password;

    String otp;
}
