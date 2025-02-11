package app.data;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class RateResponse {

    @JsonProperty("table")
    private String table;

    @JsonProperty("code")
    private String code;

    @JsonProperty("currency")
    private String currency;

    public RateResponse(){}

    public String getTable() {
        return table;
    }

    public String getCode() {
        return code;
    }

    public String getCurrency() {
        return currency;
    }

}


