package currencies.privatratecurrencies.controller;

import currencies.privatratecurrencies.dto.CurrencyArchiveResponseDto;
import currencies.privatratecurrencies.dto.CurrencyResponseDataDto;
import currencies.privatratecurrencies.service.PrivatBankService;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/currencies")
public class CurrenciesController {
    private final PrivatBankService privatBankService;

    @GetMapping("/rate")
    public List<CurrencyResponseDataDto> getCurrencies() {
        return privatBankService.getCurrencies();
    }

    @GetMapping("/archive")
    public CurrencyArchiveResponseDto getCurrenciesArchiveRates(@RequestParam LocalDate date) {
        return privatBankService.getCurrenciesArchiveRates(date);
    }
}
