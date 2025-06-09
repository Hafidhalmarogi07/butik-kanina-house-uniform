package id.co.butik.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
public class SaleOrderReturn extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnoreProperties({"customer", "admin", "details"})
    @ManyToOne(fetch = FetchType.LAZY)
    private Sale sale;


    @ManyToOne
    private UserProfile admin;

    private LocalDateTime returnDate;

    private String reason;

    private BigDecimal totalRefund;

    @JsonIgnoreProperties({"customer", "admin", "details"})
    @ManyToOne
    private Order order; // nullable

    @Enumerated(EnumType.STRING)
    private ReturnSourceType sourceType;

    @JsonIgnoreProperties("sale_order_return")
    @OneToMany(mappedBy = "saleOrderReturn", cascade = CascadeType.ALL)
    private List<ReturnDetail> details;

    @Enumerated(EnumType.STRING)
    private ReturnType returnType;

    private Boolean refundCompleted;     // true jika refund sudah dilakukan

    private Boolean replacementSent; // true jika barang pengganti sudah dikirim

    private String notes;

}