package app.data;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class CurrencyCRateResponse {
    @JsonProperty("table")
    private String table;
    @JsonProperty("no")
    private String no;
    @JsonProperty("tradingDate")
    private String tradingDate;
    @JsonProperty("effectiveDate")
    private String effectiveDate;
    @JsonProperty("rates")
    private List<CurrencyCRate> rates;

    public CurrencyCRateResponse() {
    }

    public String getTable() {
        return table;
    }

    public String getNo() {
        return no;
    }

    public String getTradingDate() {
        return tradingDate;
    }

    public String getEffectiveDate() {
        return effectiveDate;
    }

    public List<CurrencyCRate> getRates() {
        return rates;
    }

}
