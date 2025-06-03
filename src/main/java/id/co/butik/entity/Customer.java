package id.co.butik.entity;

import id.co.butik.enums.CustomerType;
import id.co.butik.util.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "customer")
@Data
public class Customer extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nama;
    private String phoneNumber;
    private String alamat;

    @Enumerated(EnumType.STRING)
    private CustomerType tipe; // INDIVIDU, INSTANSI
}
