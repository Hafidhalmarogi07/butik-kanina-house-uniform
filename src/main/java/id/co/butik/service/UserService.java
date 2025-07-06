package id.co.butik.service;

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
        return userRepository.findOneByUsername(username);
    }
}
