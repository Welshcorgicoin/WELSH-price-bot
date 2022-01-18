package btc.mocha.welshpricebot.util;

import btc.mocha.welshpricebot.response.ArkadikoPoolResponse;
import btc.mocha.welshpricebot.response.StacksPunksApiResponse;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class FPFetcher {
    private static final HttpClient httpClient = HttpClient.newHttpClient();
    private static final URI happyWelshFloorPrice = URI.create("https://stacks-punks-api.herokuapp.com/api/v1/collections/happy-welsh");
    private static final JsonBodyHandler<StacksPunksApiResponse> stacksPunksApiResponseHandler = new JsonBodyHandler<>(
            StacksPunksApiResponse.class
    );

    public static HttpResponse<StacksPunksApiResponse> getHappyWelshFloorPrice() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(happyWelshFloorPrice)
                .timeout(Duration.ofSeconds(30))
                .header("Content-Type", "application/json")
                .build();

        try {
            return httpClient.send(request, stacksPunksApiResponseHandler);
        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();

            throw new RuntimeException();
        }
    }
}
