package currencies.privatratecurrencies.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import java.util.List;
import lombok.Data;

@Data
public class CurrencyArchiveResponseDto {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    private LocalDate date;
    private String bank;
    private int baseCurrency;
    private String baseCurrencyLit;
    private List<ExchangeRateDto> exchangeRate;
}
