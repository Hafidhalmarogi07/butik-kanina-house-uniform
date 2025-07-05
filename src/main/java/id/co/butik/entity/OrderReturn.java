package id.co.butik.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import id.co.butik.entity.users.UserProfile;
import id.co.butik.enums.ReturnStatus;
import id.co.butik.enums.ReturnType;
import id.co.butik.util.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "return_tbl")
public class OrderReturn extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private UserProfile admin;

    private LocalDateTime returnDate;

    private String reason;

    private BigDecimal totalRefund;

    @JsonIgnoreProperties({"customer", "admin", "details"})
    @ManyToOne
    private Order order; // nullable

    @JsonIgnoreProperties("order_return")
    @OneToMany(mappedBy = "orderReturn", cascade = CascadeType.ALL)
    private List<ReturnDetail> details;

    @Enumerated(EnumType.STRING)
    private ReturnType returnType;

    @Enumerated(EnumType.STRING)
    private ReturnStatus statusReturn;

}