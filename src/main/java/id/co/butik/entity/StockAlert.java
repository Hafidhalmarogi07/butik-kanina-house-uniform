package id.co.butik.entity;

import id.co.butik.util.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "stock_alert")
@Data
public class StockAlert  extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Product product;

    private int neededQuantity; // jumlah yang harus diproduksi
    private int currentStock;
    private String reason; // "ORDER_DEFICIT" / "LOW_STOCK"
    private LocalDateTime createdAt;
    private boolean resolved; // jika sudah diproduksi, set true
}
