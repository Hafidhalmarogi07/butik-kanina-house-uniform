package id.co.butik.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChargeRequest {
    @JsonProperty("payment_type")
    private String paymentType;

    @JsonProperty("transaction_details")
    private TransactionDetail transactionDetail;

    @JsonProperty("customer_details")
    private CustomerDetail customerDetail;

    @JsonProperty("item_details")
    private List<ItemDetail> itemDetails;

    @JsonProperty("bank_transfer")
    private BankTransfer bankTransfer;

    @JsonProperty("echannel")
    private EChannel eChannel;

    @JsonProperty("permata")
    private Permata permata;

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public TransactionDetail getTransactionDetail() {
        return transactionDetail;
    }

    public void setTransactionDetail(TransactionDetail transactionDetail) {
        this.transactionDetail = transactionDetail;
    }

    public CustomerDetail getCustomerDetail() {
        return customerDetail;
    }

    public void setCustomerDetail(CustomerDetail customerDetail) {
        this.customerDetail = customerDetail;
    }

    public List<ItemDetail> getItemDetails() {
        return itemDetails;
    }

    public void setItemDetails(List<ItemDetail> itemDetails) {
        this.itemDetails = itemDetails;
    }

    public BankTransfer getBankTransfer() {
        return bankTransfer;
    }

    public void setBankTransfer(BankTransfer bankTransfer) {
        this.bankTransfer = bankTransfer;
    }

    public EChannel getEChannel() {
        return eChannel;
    }

    public void setEChannel(EChannel eChannel) {
        this.eChannel = eChannel;
    }

    public Permata getPermata() {
        return permata;
    }

    public void setPermata(Permata permata) {
        this.permata = permata;
    }
}
