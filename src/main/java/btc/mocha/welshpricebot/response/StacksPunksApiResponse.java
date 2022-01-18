package btc.mocha.welshpricebot.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@Getter
public class StacksPunksApiResponse {
    private Object collection;
    @JsonProperty("total_count")
    private long totalCount;
    @JsonProperty("volume_traded")
    private long volumeTraded;
    @JsonProperty("owners")
    private long owners;
    @JsonProperty("floor_price")
    private long floorPrice;
    private List<Object> items;
}
