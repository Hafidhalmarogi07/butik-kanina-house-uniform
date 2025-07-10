package id.co.butik.controller;

import id.co.butik.dto.UserProfileDto;
import id.co.butik.entity.users.UserProfile;
import id.co.butik.service.UserProfileService;
import id.co.butik.util.PageableSpec;
import id.co.butik.util.SpecificationUtils;
import org.springframework.data.domain.Page;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/user-profiles")
public class UserProfileController {

    private final UserProfileService userProfileService;

    public UserProfileController(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    @Secured({"ROLE_SUPERADMIN", "ROLE_ADMIN"})
    @GetMapping({"", "/"})
    public Page<UserProfile> getAllUserProfiles(@RequestParam Map<String, String> params, HttpServletRequest request) {
        
        return userProfileService.getUserProfiles(params, request);
    }

    @Secured({"ROLE_SUPERADMIN", "ROLE_ADMIN"})
    @GetMapping({"/{id}", "/{id}/"})
    public UserProfile getUserProfileById(@PathVariable Long id) {
        return userProfileService.getUserProfileById(id);
    }

    @Secured({"ROLE_SUPERADMIN", "ROLE_ADMIN"})
    @PostMapping({"", "/"})
    public UserProfile createUserProfile(@RequestBody UserProfileDto userProfileDto) {
        return userProfileService.createUserProfile(userProfileDto);
    }

    @Secured({"ROLE_SUPERADMIN", "ROLE_ADMIN"})
    @PutMapping({"/{id}", "/{id}/"})
    public UserProfile updateUserProfile(@PathVariable Long id, @RequestBody UserProfileDto userProfileDto) {
        return userProfileService.updateUserProfile(id, userProfileDto);
    }

    @Secured({"ROLE_SUPERADMIN", "ROLE_ADMIN"})
    @DeleteMapping({"/{id}", "/{id}/"})
    public String deleteUserProfile(@PathVariable Long id) {
        return userProfileService.deleteUserProfile(id);
    }
}