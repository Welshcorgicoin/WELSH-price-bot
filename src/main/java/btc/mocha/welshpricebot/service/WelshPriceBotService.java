package btc.mocha.welshpricebot.service;

import btc.mocha.welshpricebot.response.ArkadikoPoolResponse;
import btc.mocha.welshpricebot.util.PriceFetcher;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class WelshPriceBotService {
    /**
     * Fetch the price of $WELSH in $STX from Arkadiko
     *
     * @return $WELSH per $STX
     */
    public BigDecimal getLatestWelshPerStx() {
        ArkadikoPoolResponse priceList = PriceFetcher.getWelshPerStx().body();

        return priceList.getLatestPrice();
    }

    /**
     * Use this method ONLY if you fetch the value - `$WELSH per $STX` from trusted source.
     *
     * @param latestWelshPerStx latest $WELSH per $STX value from trusted source
     * @return $WELSH price in USD
     */
    public BigDecimal getWelshInUsd(BigDecimal latestWelshPerStx) {
        BigDecimal usdPerStx = PriceFetcher.getStxPrice();

        return usdPerStx.divide(latestWelshPerStx, 7, RoundingMode.HALF_UP);
    }
}
