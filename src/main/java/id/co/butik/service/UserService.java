package id.co.butik.service;

import id.co.butik.dto.PasswordUpdateRequest;
import id.co.butik.dto.UserProfileDto;
import id.co.butik.entity.users.UserProfile;
import id.co.butik.entity.users.Role;
import id.co.butik.entity.users.User;
import id.co.butik.repository.RoleRepository;
import id.co.butik.repository.UserProfileRepository;
import id.co.butik.repository.UserRepository;
import id.co.butik.responseException.BadRequest;
import id.co.butik.util.UsernameUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final RoleRepository roleRepository;

    private final UserProfileRepository userProfileRepository;

    public UserService(RoleRepository roleRepository, UserProfileRepository userProfileRepository, PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.roleRepository = roleRepository;
        this.userProfileRepository = userProfileRepository;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    public Page<User> getUsers(Specification<User> specification, Pageable pageable) {
        return userRepository.findAll(specification, pageable);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new BadRequest("User with id not found"));
    }

    public UserProfile createUser(UserProfileDto userProfileDto) {
        List<Role> role = roleRepository.findByName(userProfileDto.getRole());
        User user = new User();
        user.setUsername(userProfileDto.getEmail());
        user.setPassword(passwordEncoder.encode(userProfileDto.getPassword()));
        user.setEnabled(true);
        user.setRoles(role);
        userRepository.save(user);

        UserProfile profile = new UserProfile();
        profile.setEmail(userProfileDto.getEmail());
        profile.setFullName(userProfileDto.getFullName());
        profile.setAddress(userProfileDto.getAddress());
        profile.setPhoneNumber(userProfileDto.getPhoneNumber());
        profile.setDescription(userProfileDto.getDescription());
        profile.setJobTittle(userProfileDto.getJobTittle());

       return userProfileRepository.save(profile);
    }

    public User updateUser(Long id, User user) {
        User oldUser = getUserById(id);
        oldUser.setUsername(user.getUsername());
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            oldUser.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        oldUser.setEnabled(user.isEnabled());
        if (user.getRoles() != null && !user.getRoles().isEmpty()) {
            oldUser.setRoles(user.getRoles());
        }
        return userRepository.save(oldUser);
    }

    public String deleteUser(Long id) {
        userRepository.findById(id).orElseThrow(() -> new BadRequest("User not found"));
        userRepository.deleteById(id);
        return "{\"success\":true}";
    }

    public Page<Role> getRoles(Pageable var2) {
        return roleRepository.findAll(var2);
    }

    public User userMe(HttpServletRequest request){
        String username = UsernameUtils.getUsername(request);
        User user = userRepository.findOneByUsername(username);
        UserProfile userProfile = userProfileRepository.findFirstByEmail(user.getUsername());
        user.setName(userProfile.getFullName());
        user.setEmail(userProfile.getEmail());
        user.setPhotoProfile(userProfile.getPhotoProfile());
        user.setPhoneNumber(userProfile.getPhoneNumber());
        return user;
    }

    /**
     * Update user password
     * 
     * @param request PasswordUpdateRequest containing current password, new password, and confirmation
     * @param httpRequest HttpServletRequest to get current user
     * @return User object with updated password
     */
    public User updatePassword(PasswordUpdateRequest request, HttpServletRequest httpRequest) {
        System.out.println("start updatePassword ");
        // Validate request
        if (request.getPassword() == null || request.getPassword().isEmpty()) {
            throw new BadRequest("Kata sandi saat ini diperlukan");
        }

        if (request.getNewPassword() == null || request.getNewPassword().isEmpty()) {
            throw new BadRequest("Kata sandi baru diperlukan");
        }

        if (request.getReNewPassword() == null || request.getReNewPassword().isEmpty()) {
            throw new BadRequest("Konfirmasi kata sandi diperlukan");
        }

        if (!request.getNewPassword().equals(request.getReNewPassword())) {
            throw new BadRequest("Kata sandi baru dan konfirmasi tidak cocok");
        }

        // Get current user
        String username = UsernameUtils.getUsername(httpRequest);
        User user = userRepository.findOneByUsername(username);

        if (user == null) {
            throw new BadRequest("Pengguna tidak ditemukan");
        }

        // Verify current password
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadRequest("Kata sandi saat ini salah");
        }

        // Update password
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        return userRepository.save(user);
    }
}
