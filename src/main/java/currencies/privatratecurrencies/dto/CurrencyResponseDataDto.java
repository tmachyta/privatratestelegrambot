package currencies.privatratecurrencies.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CurrencyResponseDataDto {
    private String ccy;
    @JsonProperty("base_ccy")
    private String baseCcy;
    private String buy;
    private String sale;
}
