package app.data;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CurrencyCRate {
    @JsonProperty("code")
    private String code;
    @JsonProperty("currency")
    private String currency;
    @JsonProperty("bid")
    private double bid;
    @JsonProperty("ask")
    private double ask;

    public CurrencyCRate() {
    }

    public String getCode() {
        return code;
    }

    public String getCurrency() {
        return currency;
    }

    public double getBid() {
        return bid;
    }

    public double getAsk() {
        return ask;
    }

    @Override
    public String toString() {
        return "CurrencyCRate{" +
                "code='" + code + '\'' +
                ", currency='" + currency + '\'' +
                ", bid=" + bid +
                ", ask=" + ask +
                '}';
    }
}
