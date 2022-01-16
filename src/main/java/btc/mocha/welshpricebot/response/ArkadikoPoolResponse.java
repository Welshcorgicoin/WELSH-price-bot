package btc.mocha.welshpricebot.response;

import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
public class ArkadikoPoolResponse {
    private List<List<Double>> prices;

    public BigDecimal getLatestPrice() {
        return BigDecimal.valueOf(prices.get(prices.size() - 1).get(1));
    }
}
