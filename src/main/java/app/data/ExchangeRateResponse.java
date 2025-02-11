package app.data;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ExchangeRateResponse extends RateResponse{

    @JsonProperty("rates")
    private List<ExchangeRate> rates;

    public ExchangeRateResponse() {
    }

    public List<ExchangeRate> getRates() {
        return rates;
    }

}
