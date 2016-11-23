package dto;

import java.util.ArrayList;

import adapter.EMIPlansDTO;


public class EMIOptionDTO {
    private String gtwId;
    private String gtwName;
    private String subventionPaidBy;
    private String tenureMonths;
    private String processingFeeFlat;
    private String processingFeePercent;
    private String ccAvenueFeeFlat;
    private String ccAvenueFeePercent;
    private String tenureData;
    private String planId;
    private String accountCurrName;
    private String emiPlanId;
    private String midProcesses;
    private String bins;

    private ArrayList<EMIPlansDTO> emiPlansDTO = new ArrayList<EMIPlansDTO>();

    public String getGtwId() {
        return gtwId;
    }
    public void setGtwId(String gtwId) {
        this.gtwId = gtwId;
    }
    public String getGtwName() {
        return gtwName;
    }
    public void setGtwName(String gtwName) {
        this.gtwName = gtwName;
    }
    public String getSubventionPaidBy() {
        return subventionPaidBy;
    }
    public void setSubventionPaidBy(String subventionPaidBy) {
        this.subventionPaidBy = subventionPaidBy;
    }
    public String getTenureMonths() {
        return tenureMonths;
    }
    public void setTenureMonths(String tenureMonths) {
        this.tenureMonths = tenureMonths;
    }
    public String getProcessingFeeFlat() {
        return processingFeeFlat;
    }
    public void setProcessingFeeFlat(String processingFeeFlat) {
        this.processingFeeFlat = processingFeeFlat;
    }
    public String getProcessingFeePercent() {
        return processingFeePercent;
    }
    public void setProcessingFeePercent(String processingFeePercent) {
        this.processingFeePercent = processingFeePercent;
    }
    public String getCcAvenueFeeFlat() {
        return ccAvenueFeeFlat;
    }
    public void setCcAvenueFeeFlat(String ccAvenueFeeFlat) {
        this.ccAvenueFeeFlat = ccAvenueFeeFlat;
    }
    public String getCcAvenueFeePercent() {
        return ccAvenueFeePercent;
    }
    public void setCcAvenueFeePercent(String ccAvenueFeePercent) {
        this.ccAvenueFeePercent = ccAvenueFeePercent;
    }
    public String getTenureData() {
        return tenureData;
    }
    public void setTenureData(String tenureData) {
        this.tenureData = tenureData;
    }
    public String getPlanId() {
        return planId;
    }
    public void setPlanId(String planId) {
        this.planId = planId;
    }
    public String getAccountCurrName() {
        return accountCurrName;
    }
    public void setAccountCurrName(String accountCurrName) {
        this.accountCurrName = accountCurrName;
    }
    public String getEmiPlanId() {
        return emiPlanId;
    }
    public void setEmiPlanId(String emiPlanId) {
        this.emiPlanId = emiPlanId;
    }
    public String getMidProcesses() {
        return midProcesses;
    }
    public void setMidProcesses(String midProcesses) {
        this.midProcesses = midProcesses;
    }
    public String getBins() {
        return bins;
    }
    public void setBins(String bins) {
        this.bins = bins;
    }
    public ArrayList<EMIPlansDTO> getEmiPlansDTO() {
        return emiPlansDTO;
    }
    public void setEmiPlansDTO(ArrayList<EMIPlansDTO> emiPlansDTO) {
        this.emiPlansDTO = emiPlansDTO;
    }
}
