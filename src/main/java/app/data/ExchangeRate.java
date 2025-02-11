package app.data;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ExchangeRate extends Rate{

    @JsonProperty("mid")
    private double mid;

    public ExchangeRate(){}

    public double getMid() {
        return mid;
    }


}

