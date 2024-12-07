package currencies.privatratecurrencies.dto;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class ExchangeRateDto {
    private String baseCurrency;
    private String currency;
    private BigDecimal saleRateNB;
    private BigDecimal purchaseRateNB;
    private BigDecimal saleRate;
    private BigDecimal purchaseRate;
}
