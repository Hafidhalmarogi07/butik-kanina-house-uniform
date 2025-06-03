package id.co.butik.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EChannel {
    @JsonProperty("bill_info1")
    private String billInfo1;

    @JsonProperty("bill_info2")
    private String billInfo2;

    public String getBillInfo1() {
        return billInfo1;
    }

    public void setBillInfo1(String billInfo1) {
        this.billInfo1 = billInfo1;
    }

    public String getBillInfo2() {
        return billInfo2;
    }

    public void setBillInfo2(String billInfo2) {
        this.billInfo2 = billInfo2;
    }
}
