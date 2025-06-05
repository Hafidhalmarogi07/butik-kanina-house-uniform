package id.co.butik.controller;

import id.co.butik.dto.UserProfileDto;
import id.co.butik.entity.users.UserProfile;
import id.co.butik.entity.users.Role;
import id.co.butik.entity.users.User;
import id.co.butik.service.UserService;
import id.co.butik.util.PageableSpec;
import id.co.butik.util.SpecificationUtils;
import org.springframework.data.domain.Page;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Secured({"ROLE_SUPERADMIN"})
    @GetMapping({"", "/"})
    public Page<User> getAllUsers(@RequestParam Map<String, String> params) {
        PageableSpec<User> pageableSpec = SpecificationUtils.of(params);
        return userService.getUsers(pageableSpec.getSpecification(), pageableSpec.getPageable());
    }

    @Secured({"ROLE_SUPERADMIN"})
    @GetMapping({"/{id}", "/{id}/"})
    public User getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @Secured({"ROLE_SUPERADMIN"})
    @PostMapping({"", "/"})
    public UserProfile createUser(@RequestBody UserProfileDto profile) {
        return userService.createUser(profile);
    }

    @Secured({"ROLE_SUPERADMIN"})
    @PutMapping({"/{id}", "/{id}/"})
    public User updateUser(@PathVariable Long id, @RequestBody User user) {
        return userService.updateUser(id, user);
    }

    @Secured({"ROLE_SUPERADMIN"})
    @DeleteMapping({"/{id}", "/{id}/"})
    public String deleteUser(@PathVariable Long id) {
        return userService.deleteUser(id);
    }

    @GetMapping({"/role", "/role/"})
    public Page<Role> getRoles(@RequestParam Map<String, String> params) {
        PageableSpec<User> pageableSpec = SpecificationUtils.of(params);
        return userService.getRoles(pageableSpec.getPageable());
    }

    @GetMapping("/me")
    public User getUser(HttpServletRequest request){
        return userService.userMe(request);

    }
}
