package app.data;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Rate {

    @JsonProperty("no")
    private String no;

    @JsonProperty("effectiveDate")
    private String effectiveDate;

    public Rate() {
    }

    public String getNo() {
        return no;
    }

    public String getEffectiveDate() {
        return effectiveDate;
    }

}
