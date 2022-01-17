package btc.mocha.welshpricebot.util;

import btc.mocha.welshpricebot.response.ArkadikoPoolResponse;
import com.litesoftwares.coingecko.CoinGeckoApiClient;
import com.litesoftwares.coingecko.constant.Currency;
import com.litesoftwares.coingecko.impl.CoinGeckoApiClientImpl;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class PriceFetcher {
    private static final HttpClient httpClient = HttpClient.newHttpClient();
    private static final URI welshPerStx = URI.create("https://arkadiko-api.herokuapp.com/api/v1/pools/6/prices");
    private static final JsonBodyHandler<ArkadikoPoolResponse> arkadikoPoolResponseHandler = new JsonBodyHandler<>(
            ArkadikoPoolResponse.class
    );

    private static final CoinGeckoApiClient coinGeckoClient = new CoinGeckoApiClientImpl();

    public static HttpResponse<ArkadikoPoolResponse> getWelshPerStx() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(welshPerStx)
                .timeout(Duration.ofSeconds(30))
                .header("Content-Type", "application/json")
                .build();
        try {
            return httpClient.send(request, arkadikoPoolResponseHandler);
        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();

            throw new RuntimeException();
        }
    }

    public static BigDecimal getStxPrice() {
        double price = coinGeckoClient.getPrice("blockstack", Currency.USD)
                .get("blockstack")
                .get("usd");

        return BigDecimal.valueOf(price);
    }
}
