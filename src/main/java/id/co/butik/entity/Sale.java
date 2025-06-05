package id.co.butik.entity;

import id.co.butik.entity.users.User;
import id.co.butik.entity.users.UserProfile;
import id.co.butik.enums.SaleStatus;
import id.co.butik.util.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.codehaus.jackson.annotate.JsonManagedReference;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "sale")
public class Sale extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String invoiceNumber;

    private LocalDateTime date;

    @ManyToOne
    private Customer customer;

    @ManyToOne
    private UserProfile admin;

    private BigDecimal total;

   private Integer items;

   @Enumerated(EnumType.STRING)
   private SaleStatus status;


    @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL)
    private List<SaleDetail> details;
}