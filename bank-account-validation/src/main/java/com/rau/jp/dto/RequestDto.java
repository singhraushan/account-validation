package com.rau.jp.dto;

import java.util.Arrays;

public class RequestDto {
    private String accountNumber;
    private String[] providers;

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String[] getProviders() {
        return providers;
    }

    public void setProviders(String[] providers) {
        this.providers = providers;
    }

    @Override
    public String toString() {
        return "RequestDto{" +
                "accountNumber='" + accountNumber + '\'' +
                ", providers=" + Arrays.toString(providers) +
                '}';
    }
}
