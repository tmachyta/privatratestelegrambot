package currencies.privatratecurrencies.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import currencies.privatratecurrencies.dto.CurrencyResponseDataDto;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PrivatBankService {
    private static final String BASE_URL = "https://api.privatbank.ua/"
            + "p24api/pubinfo?exchange&coursid=11";

    private final ObjectMapper objectMapper;

    public List<CurrencyResponseDataDto> getCurrencies() {
        HttpClient httpClient = HttpClient.newHttpClient();

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(BASE_URL))
                .build();
        try {
            HttpResponse<String> response =
                    httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            CurrencyResponseDataDto[] dataDto =
                    objectMapper.readValue(response.body(), CurrencyResponseDataDto[].class);
            return Arrays.stream(dataDto).toList();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
