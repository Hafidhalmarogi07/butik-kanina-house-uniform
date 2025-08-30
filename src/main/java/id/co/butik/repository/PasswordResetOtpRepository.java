package id.co.butik.repository;

import id.co.butik.entity.PasswordResetOtp;
import id.co.butik.entity.users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface PasswordResetOtpRepository extends JpaRepository<PasswordResetOtp, Long> {

    /**
     * Find the latest valid OTP for a user
     * @param user the user
     * @param now the current time
     * @return the OTP if found
     */
    @Query("FROM PasswordResetOtp p WHERE p.user = :user AND p.expiryTime > :now AND p.used = false ORDER BY p.createdAt DESC")
    Optional<PasswordResetOtp> findValidOtpForUser(@Param("user") User user, @Param("now") LocalDateTime now);

    /**
     * Find OTP by user and OTP code
     * @param user the user
     * @param otp the OTP code
     * @return the OTP if found
     */
    Optional<PasswordResetOtp> findByUserAndOtp(User user, String otp);

    /**
     * Find valid OTP by user and OTP code
     * @param user the user
     * @param otp the OTP code
     * @param now the current time
     * @return the OTP if found and valid
     */
    @Query("FROM PasswordResetOtp p WHERE p.user = :user AND p.otp = :otp AND p.expiryTime > :now AND p.used = false")
    Optional<PasswordResetOtp> findValidOtp(@Param("user") User user, @Param("otp") String otp, @Param("now") LocalDateTime now);
}