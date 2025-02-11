package app.data;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class BuySellRateResponse extends RateResponse {

    @JsonProperty("rates")
    private List<BuySellRate> rates;

    public BuySellRateResponse() {
    }

    public List<BuySellRate> getRates() {
        return rates;
    }

}
