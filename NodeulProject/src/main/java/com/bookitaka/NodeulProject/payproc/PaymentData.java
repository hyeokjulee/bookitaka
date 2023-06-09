package com.bookitaka.NodeulProject.payproc;

public class PaymentData {
    private String imp_uid;
    private String merchant_uid;
    private String status;

    private int amount;
    // ... 기타 필드

    // Getters and Setters

    public String getImp_uid() {
        return imp_uid;
    }

    public void setImp_uid(String imp_uid) {
        this.imp_uid = imp_uid;
    }

    public String getMerchant_uid() {
        return merchant_uid;
    }

    public void setMerchant_uid(String merchant_uid) {
        this.merchant_uid = merchant_uid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // ... 기타 Getter와 Setter 메서드


    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}

