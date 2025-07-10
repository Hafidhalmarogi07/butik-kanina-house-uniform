package id.co.butik.service;

import id.co.butik.dto.UserProfileDto;
import id.co.butik.entity.users.UserProfile;
import id.co.butik.entity.users.Role;
import id.co.butik.entity.users.User;
import id.co.butik.repository.RoleRepository;
import id.co.butik.repository.UserProfileRepository;
import id.co.butik.repository.UserRepository;
import id.co.butik.responseException.BadRequest;
import id.co.butik.util.PageableSpec;
import id.co.butik.util.SpecificationUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class UserProfileService {

    private final UserProfileRepository userProfileRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserProfileService(UserProfileRepository userProfileRepository, UserRepository userRepository,
                              RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userProfileRepository = userProfileRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Get all user profiles with pagination and filtering
     *
     * @param specification the specification for filtering
     * @param pageable      the pagination information
     * @return a page of user profiles
     */
    public Page<UserProfile> getUserProfiles(Map<String, String> params, HttpServletRequest request) {
        PageableSpec<UserProfile> pageableSpec = SpecificationUtils.of(params);
        Specification<UserProfile> spec = (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.notEqual(root.get("email"), request.getRemoteUser());
        Page<UserProfile> userProfilePage = userProfileRepository.findAll(pageableSpec.getSpecification().and(spec), pageableSpec.getPageable());
        userProfilePage.getContent().forEach(userProfile -> {
            User user = userRepository.findOneByUsername(userProfile.getEmail());
            userProfile.setStatus(user.isEnabled());
            userProfile.setRole(user.getRoles().get(0).getName());
        });

        return userProfilePage;
    }

    /**
     * Get a user profile by ID
     *
     * @param id the ID of the user profile
     * @return the user profile
     * @throws BadRequest if the user profile is not found
     */
    public UserProfile getUserProfileById(Long id) {
        UserProfile userProfile = userProfileRepository.findById(id)
                .orElseThrow(() -> new BadRequest("User profile with id not found"));

        User user = userRepository.findOneByUsername(userProfile.getEmail());
        userProfile.setStatus(user.isEnabled());
        userProfile.setRole(user.getRoles().get(0).getName());

        return userProfile;
    }

    /**
     * Create a new user profile with associated user and role
     *
     * @param userProfileDto the DTO containing user profile information
     * @return the created user profile
     */
    @Transactional
    public UserProfile createUserProfile(UserProfileDto userProfileDto) {
        // Check if email already exists
        UserProfile existingProfile = userProfileRepository.findFirstByEmail(userProfileDto.getEmail());
        if (existingProfile != null) {
            throw new BadRequest("Email already exists");
        }

        // Create user with role
        List<Role> roles = roleRepository.findByName(userProfileDto.getRole());
        if (roles.isEmpty()) {
            throw new BadRequest("Role not found");
        }

        User user = new User();
        user.setUsername(userProfileDto.getEmail());
        user.setPassword(passwordEncoder.encode(userProfileDto.getPassword()));
        user.setEmail(userProfileDto.getEmail());
        user.setEnabled(true);
        user.setRoles(roles);
        userRepository.save(user);

        // Create user profile
        UserProfile profile = new UserProfile();
        profile.setEmail(userProfileDto.getEmail());
        profile.setFullName(userProfileDto.getFullName());
        profile.setAddress(userProfileDto.getAddress());
        profile.setPhoneNumber(userProfileDto.getPhoneNumber());
        profile.setDescription(userProfileDto.getDescription());
        profile.setJobTittle(userProfileDto.getJobTittle());

        return userProfileRepository.save(profile);
    }

    /**
     * Update an existing user profile
     *
     * @param id             the ID of the user profile to update
     * @param userProfileDto the DTO containing updated user profile information
     * @return the updated user profile
     */
    public UserProfile updateUserProfile(Long id, UserProfileDto userProfileDto) {
        UserProfile existingProfile = getUserProfileById(id);

        // Update user profile fields
        if (userProfileDto.getEmail() != null) {
            // Check if new email already exists for another profile
            UserProfile profileWithEmail = userProfileRepository.findFirstByEmail(userProfileDto.getEmail());
            if (profileWithEmail != null && !profileWithEmail.getId().equals(id)) {
                throw new BadRequest("Email already exists");
            }
            existingProfile.setEmail(userProfileDto.getEmail());
        }

        if (userProfileDto.getFullName() != null) {
            existingProfile.setFullName(userProfileDto.getFullName());
        }

        if (userProfileDto.getAddress() != null) {
            existingProfile.setAddress(userProfileDto.getAddress());
        }

        if (userProfileDto.getPhoneNumber() != null) {
            existingProfile.setPhoneNumber(userProfileDto.getPhoneNumber());
        }

        if (userProfileDto.getDescription() != null) {
            existingProfile.setDescription(userProfileDto.getDescription());
        }

        if (userProfileDto.getJobTittle() != null) {
            existingProfile.setJobTittle(userProfileDto.getJobTittle());
        }

        User user = userRepository.findOneByUsername(existingProfile.getEmail());
        // Update associated user if username exists

        // Update user fields
        if (userProfileDto.getPassword() != null && !userProfileDto.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userProfileDto.getPassword()));
        }

        if (userProfileDto.getEmail() != null) {
            user.setEmail(userProfileDto.getEmail());
        }

        // Update role if provided
        if (userProfileDto.getRole() != null) {
            List<Role> roles = roleRepository.findByName(userProfileDto.getRole());
            if (!roles.isEmpty()) {
                user.setRoles(roles);
            }
        }
        if(userProfileDto.getStatus() != null) {
            user.setEnabled(userProfileDto.getStatus());
        }

        userRepository.save(user);


        return userProfileRepository.save(existingProfile);
    }

    /**
     * Delete a user profile and its associated user
     *
     * @param id the ID of the user profile to delete
     * @return success message
     */
    public String deleteUserProfile(Long id) {
        UserProfile profile = getUserProfileById(id);

        // Delete associated user if exists
        User user = userRepository.findOneByUsername(profile.getEmail());
        if (user != null) {
            userRepository.delete(user);
        }

        userProfileRepository.deleteById(id);
        return "{\"success\":true}";
    }
}
