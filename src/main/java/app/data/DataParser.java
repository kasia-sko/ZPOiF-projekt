package app.data;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataParser {

    public static ExchangeRateResponse parseTableA(String dateStart, String dateEnd, String code) throws IOException, InterruptedException {
        String url = "https://api.nbp.pl/api/exchangerates/rates/A/" + code + "/" + dateStart + "/" + dateEnd + "/?format=json";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Accept", "application/json")
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        ObjectMapper objectMapper = new ObjectMapper();
        ExchangeRateResponse exchangeRateResponse = objectMapper.readValue(
                response.body(), ExchangeRateResponse.class);

        return exchangeRateResponse;
    }

    public static BuySellRateResponse parseTableC(String code) throws IOException, InterruptedException {
        String url = "https://api.nbp.pl/api/exchangerates/rates/C/" + code + "/?format=json";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Accept", "application/json")
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        ObjectMapper objectMapper = new ObjectMapper();
        BuySellRateResponse buySellRateResponse = objectMapper.readValue(
                response.body(), BuySellRateResponse.class);

        return buySellRateResponse;
    }

    public static HashMap<String,String> createCurrencyMap(String table) throws IOException, InterruptedException {
        String url = "https://api.nbp.pl/api/exchangerates/tables/" + table + "/?format=json";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Accept", "application/json")
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        String responseBody = response.body();

        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode jsonResponse = objectMapper.readTree(responseBody);

        JsonNode ratesNode = jsonResponse.get(0).path("rates");

        HashMap<String, String> currencyMap = new HashMap<>();

        if (!ratesNode.isNull() && ratesNode.size() > 0) {
            for (JsonNode currency : ratesNode) {
                String code = currency.path("code").asText();
                String currencyName = currency.path("currency").asText();
                currencyMap.put(code, currencyName);
            }
        } else {
            System.out.println("Brak walut w odpowiedzi.");
        }

        return currencyMap;

    }

    public static List<String> createCurrencyCodeList(String table) throws IOException, InterruptedException {
        String url = "https://api.nbp.pl/api/exchangerates/tables/" + table + "/?format=json";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Accept", "application/json")
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        String responseBody = response.body();

        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode jsonResponse = objectMapper.readTree(responseBody);

        JsonNode ratesNode = jsonResponse.get(0).path("rates");

        List<String> currencyCodes = new ArrayList<>();

        if (!ratesNode.isNull() && ratesNode.size() > 0) {
            for (JsonNode currency : ratesNode) {
                String code = currency.path("code").asText();
                currencyCodes.add(code);
            }
        } else {
            System.out.println("Brak walut w odpowiedzi.");
        }

        return currencyCodes;
    }

}
