package btc.mocha.welshpricebot.dto;

import btc.mocha.welshpricebot.response.StacksPunksApiResponse;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Data
@Builder
public class HappyWelshDto {
    private final Long numberOfItems;
    private final Long numberOfOwners;
    private final BigDecimal floorPrice;
    private final BigDecimal allTimeVolumeTraded;

    public static HappyWelshDto fromResponse(StacksPunksApiResponse response) {
        BigDecimal floorPrice = BigDecimal.valueOf(response.getFloorPrice())
                .divide(BigDecimal.valueOf(1000000), 2, RoundingMode.HALF_UP);
        BigDecimal allTimeVolumeTraded = BigDecimal.valueOf(response.getVolumeTraded())
                .divide(BigDecimal.valueOf(1000000), 2, RoundingMode.HALF_UP);

        return HappyWelshDto.builder()
                .numberOfItems(response.getTotalCount())
                .numberOfOwners(response.getOwners())
                .floorPrice(floorPrice)
                .allTimeVolumeTraded(allTimeVolumeTraded)
                .build();
    }
}
