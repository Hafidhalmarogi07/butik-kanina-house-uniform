package id.co.butik.entity;

import id.co.butik.entity.users.User;
import id.co.butik.entity.users.UserProfile;
import id.co.butik.enums.ExpenseType;
import id.co.butik.enums.PaymentMethod;
import id.co.butik.util.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name = "expense")
public class Expense extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private LocalDate date;
    private String description;
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private ExpenseType type;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    private String note;

    @ManyToOne
    private UserProfile createdBy;
}