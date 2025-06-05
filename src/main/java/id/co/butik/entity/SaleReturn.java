package id.co.butik.entity;

import id.co.butik.entity.users.User;
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
@Table(name = "sale_return")
public class SaleReturn extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Sale sale;

    @ManyToOne
    private User admin;

    private LocalDateTime date;
    private String reason;
    private BigDecimal totalRefund;

    @OneToMany(mappedBy = "saleReturn", cascade = CascadeType.ALL)
    private List<ReturnDetail> details;

}