package dto;


public class PaymentOptionDTO {
    private String payOptId;
    private String payOptName;

    public PaymentOptionDTO(String payOptId, String payOptName) {
        super();
        this.payOptId = payOptId;
        this.payOptName = payOptName;
    }
    public String getPayOptId() {
        return payOptId;
    }
    public void setPayOptId(String payOptId) {
        this.payOptId = payOptId;
    }
    public String getPayOptName() {
        return payOptName;
    }
    public void setPayOptName(String payOptName) {
        this.payOptName = payOptName;
    }
}
