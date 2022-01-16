package btc.mocha.welshpricebot.service;

import btc.mocha.welshpricebot.response.ArkadikoPoolResponse;
import btc.mocha.welshpricebot.util.PriceFetcher;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class WelshPriceBotService {
    public BigDecimal getWelshInUsd() {
        ArkadikoPoolResponse priceList = PriceFetcher.getWelshPerStx().body();

        BigDecimal latestWelshPerStx = priceList.getLatestPrice();
        BigDecimal usdPerStx = PriceFetcher.getStxPrice();

        return usdPerStx.divide(latestWelshPerStx, 7, RoundingMode.HALF_UP);
    }
}
