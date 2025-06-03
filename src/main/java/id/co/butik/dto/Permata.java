package id.co.butik.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Permata {

    @JsonProperty("recipient_name")
    private String recipientName;

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }
}
