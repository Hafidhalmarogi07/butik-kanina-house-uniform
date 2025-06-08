package id.co.butik.dto;

import lombok.Data;

@Data
public class UserProfileDto {

    private String username;

    private String password;

    private String email;

    private String phoneNumber;

    private String address;

    private String roleName;

    private String fullName;

    private String description;

    private String jobTittle;

    private Boolean status;

}
