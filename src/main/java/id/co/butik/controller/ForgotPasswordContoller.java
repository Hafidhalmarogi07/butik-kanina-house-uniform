package id.co.butik.controller;

import id.co.butik.dto.ForgotRequest;
import id.co.butik.service.PasswordResetService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/forgot-password")
public class ForgotPasswordContoller {

    private final PasswordResetService passwordResetService;

    public ForgotPasswordContoller(PasswordResetService passwordResetService) {
        this.passwordResetService = passwordResetService;
    }

    /**
     * Request password reset
     * @param email the email address
     * @return response entity with result
     */
    @PostMapping("/request")
    public ResponseEntity<Map<String, Object>> requestPasswordReset(@RequestBody ForgotRequest request) {
        Map<String, Object> response = passwordResetService.requestPasswordReset(request.getEmail());
        return ResponseEntity.ok(response);
    }

    /**
     * Reset password
     * @param email the email address
     * @param otp the OTP code
     * @param newPassword the new password
     * @return response entity with result
     */
    @PostMapping("/reset")
    public ResponseEntity<Map<String, Object>> resetPassword(@RequestBody ForgotRequest request) {
        Map<String, Object> response = passwordResetService.validateOtpAndResetPassword(request);
        return ResponseEntity.ok(response);
    }
}
