package id.co.butik.dto;

import lombok.Data;

/**
 * DTO for updating user password
 */
@Data
public class PasswordUpdateRequest {
    private String password;        // Current password
    private String newPassword;    // New password
    private String reNewPassword; // Confirmation of new password
}