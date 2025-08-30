package id.co.butik.service;

import id.co.butik.dto.ForgotRequest;
import id.co.butik.entity.PasswordResetOtp;
import id.co.butik.entity.users.User;
import id.co.butik.entity.users.UserProfile;
import id.co.butik.repository.PasswordResetOtpRepository;
import id.co.butik.repository.UserProfileRepository;
import id.co.butik.repository.UserRepository;
import id.co.butik.responseException.BadRequest;
import id.co.butik.service.email.EmailService;
import id.co.butik.util.EmailTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

@Service
public class PasswordResetService {

    private final UserRepository userRepository;
    private final PasswordResetOtpRepository passwordResetOtpRepository;
    private final EmailService emailService;
    private final EmailTemplate emailTemplate;
    private final PasswordEncoder passwordEncoder;
    private final UserProfileRepository userProfileRepository;

    @Value("${otp.expiry.minutes:5}")
    private int otpExpiryMinutes;

    public PasswordResetService(UserRepository userRepository,
                                PasswordResetOtpRepository passwordResetOtpRepository,
                                EmailService emailService,
                                EmailTemplate emailTemplate,
                                PasswordEncoder passwordEncoder, UserProfileRepository userProfileRepository) {
        this.userRepository = userRepository;
        this.passwordResetOtpRepository = passwordResetOtpRepository;
        this.emailService = emailService;
        this.emailTemplate = emailTemplate;
        this.passwordEncoder = passwordEncoder;
        this.userProfileRepository = userProfileRepository;
    }

    /**
     * Request a password reset for a user with the given email
     * @param email the email address
     * @return a map with success status
     */
    public Map<String, Object> requestPasswordReset(String email) {
        Map<String, Object> response = new HashMap<>();

        // Find user by email
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (!userOptional.isPresent()) {
            response.put("success", false);
            response.put("message", "Email tidak ditemukan");
            return response;
        }

        User user = userOptional.get();

        // Generate OTP
        String otp = generateOtp();

        // Save OTP to database
        PasswordResetOtp passwordResetOtp = new PasswordResetOtp();
        passwordResetOtp.setUser(user);
        passwordResetOtp.setOtp(otp);
        passwordResetOtp.setExpiryTime(LocalDateTime.now().plusMinutes(otpExpiryMinutes));
        passwordResetOtp.setUsed(false);
        passwordResetOtpRepository.save(passwordResetOtp);

        UserProfile userProfile = userProfileRepository.findFirstByEmail(user.getEmail());
        user.setName(userProfile.getFullName());

        // Send OTP email
        sendOtpEmail(user, otp);

        response.put("success", true);
        response.put("message", "Kode OTP telah dikirim ke email Anda");
        return response;
    }


    public Map<String, Object> validateOtpAndResetPassword(ForgotRequest request) {
        Map<String, Object> response = new HashMap<>();

        // Find user by email
        Optional<User> userOptional = userRepository.findByEmail(request.getEmail());
        if (!userOptional.isPresent()) {
            response.put("success", false);
            response.put("message", "Email tidak ditemukan");
            return response;
        }

        User user = userOptional.get();

        // Validate OTP
        Optional<PasswordResetOtp> passwordResetOtpOptional = 
            passwordResetOtpRepository.findValidOtp(user, request.getOtp(), LocalDateTime.now());

        if (!passwordResetOtpOptional.isPresent()) {
            response.put("success", false);
            response.put("message", "Kode OTP tidak valid atau sudah kadaluarsa");
            return response;
        }

        PasswordResetOtp passwordResetOtp = passwordResetOtpOptional.get();

        // Update password
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);

        // Mark OTP as used
        passwordResetOtp.setUsed(true);
        passwordResetOtpRepository.save(passwordResetOtp);

        response.put("success", true);
        response.put("message", "Password berhasil direset, silakan login dengan password baru Anda");
        return response;
    }

    /**
     * Generate a random 6-digit OTP
     * @return the OTP
     */
    private String generateOtp() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000); // 6-digit number
        return String.valueOf(otp);
    }

    /**
     * Send OTP email to user
     * @param user the user
     * @param otp the OTP code
     */
    private void sendOtpEmail(User user, String otp) {
        String template = emailTemplate.getResetPassword();
        template = template.replaceAll("\\{\\{namaUser}}", user.getName() != null ? user.getName() : "User");
        template = template.replaceAll("\\{\\{otpCode}}", otp);

        emailService.sendPasswordResetEmail(user.getEmail(), "Kode OTP Reset Password Anda", template);
    }
}
