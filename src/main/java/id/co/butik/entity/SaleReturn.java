package id.co.butik.entity;

import id.co.butik.entity.users.User;
import id.co.butik.entity.users.UserProfile;
import id.co.butik.enums.ReturnSourceType;
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
public class SaleReturn extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Sale sale;

    @ManyToOne
    private UserProfile admin;

    private LocalDateTime returnDate;

    private String reason;

    private BigDecimal totalRefund;

    @ManyToOne
    private Order order; // nullable

    @Enumerated(EnumType.STRING)
    private ReturnSourceType sourceType;

    @OneToMany(mappedBy = "saleReturn", cascade = CascadeType.ALL)
    private List<ReturnDetail> details;

    @Enumerated(EnumType.STRING)
    private ReturnType returnType;

    private Boolean refundCompleted;     // true jika refund sudah dilakukan

    private Boolean replacementSent; // true jika barang pengganti sudah dikirim

    private String notes;

}