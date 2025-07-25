package id.co.butik.dto;

import lombok.Data;

@Data
public class UserProfileDto {

    private String password;

    private String email;

    private String phoneNumber;

    private String address;

    private String role;

    private String fullName;

    private String description;

    private String jobTittle;

    private Boolean status;

}
