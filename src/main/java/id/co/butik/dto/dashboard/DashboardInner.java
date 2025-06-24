package id.co.butik.dto.dashboard;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DashboardInner {

    private long sales;

    private long orders;

    private long ordersPending;

    private BigDecimal revenue;

    private BigDecimal expenses;
}
