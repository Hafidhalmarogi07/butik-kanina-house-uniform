package id.co.butik.entity.users;


import id.co.butik.util.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "user_profile")
@Data
public class UserProfile extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    private String fullName;

    private String photoProfile;

    private String description;

    private String jobTittle;

    private String phoneNumber;

    private String address;

    @Transient
    private Boolean status;

    @Transient
    private String role;


}
