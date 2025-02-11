package app.data;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BuySellRate extends Rate{

    @JsonProperty("bid")
    private double bid;

    @JsonProperty("ask")
    private double ask;

    public BuySellRate() {
    }

    public double getBid() {
        return bid;
    }

    public double getAsk() {
        return ask;
    }

}
