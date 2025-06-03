package id.co.butik.entity;

import id.co.butik.enums.Size;
import id.co.butik.util.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "product")
@Data
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private Size size;

    private Integer stock;

    private BigDecimal sellingPrice;

    private String description;

    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY,targetEntity = Category.class)
    private Category category;




}
