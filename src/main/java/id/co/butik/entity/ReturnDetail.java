package id.co.butik.entity;

import id.co.butik.enums.Size;
import id.co.butik.util.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "return_detail")
public class ReturnDetail extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private SaleReturn saleReturn;

    @ManyToOne
    private Product product;

    @Enumerated(EnumType.STRING)
    private Size size;

    private int quantity;
    private String note;
}