package currencies.privatratecurrencies.service;

import currencies.privatratecurrencies.config.BotConfig;
import currencies.privatratecurrencies.dto.CurrencyArchiveResponseDto;
import currencies.privatratecurrencies.dto.CurrencyResponseDataDto;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
@RequiredArgsConstructor
public class TelegramBot extends TelegramLongPollingBot {
    private static final String DEFAULT_MESSAGE = "Вибачте, але ми не можемо розпізнати команду";
    private static final String FIRST_ERROR_MESSAGE = "Введена дата не може бути в майбутньому";
    private static final String SECOND_ERROR_MESSAGE = "Дата не може бути старшою "
            + "ніж 4 роки від сьогодні";
    private static final String THIRD_ERROR_MESSAGE = "Невірний формат дати. "
            + "Будь ласка, використовуйте формат: yyyy-MM-dd";
    private static final String FOURTH_ERROR_MESSAGE = "Невірний формат команди. "
            + "Будь ласка, надайте дату у форматі: /currenciesArchive yyyy-MM-dd";
    private static final String START_MESSAGE = """
                Вітаю, %s, у PrivatBankCurrenciesRate!
                
                Дізнайтесь більше інформації за допомогою команди /help
                
                """;
    private static final String HELP_MESSAGE = """
                
                Щоб запустити бота використовуйте команду /start
                
                Щоб дізнатись більше інформації використовуйте команду /help
                
                Щоб дізнатись курс валют використовуйте команду /rate
                
                Щоб дізнатись більше інформації про архів використовуйте команду /archiveHelp
                
                """;
    private static final String ARCHIVE_MESSAGE = """
                Щоб дізнатись курс валют з архіву, використовуйте команду 
                /currenciesArchive yyyy-MM-dd.
                
                Зверніть увагу, що рік не повинен бути меншим ніж 4 роки від сьогоднішнього дня.
                Для коректної роботи команди, використовуйте формат дати yyyy-MM-dd.
                """;
    private static final String START = "/start";
    private static final String HELP = "/help";
    private static final String RATE = "/rate";
    private static final String ARCHIVE_HELP = "/archive";
    private static final String CURRENCIES_ARCHIVE = "/currenciesArchive";
    private static final String CURRENCIES_RATE = "Курс валют за %s :\n";
    private static final String CURRENCIES = "Курс валют :\n";
    private static final String BUY_OR_SALE = "%s/%s: Купівля: %s, Продаж: %s\n";
    private static final int DEFAULT_YEAR = 4;
    private final BotConfig botConfig;
    private final PrivatBankService privatBankService;

    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();

            switch (messageText) {
                case START:
                    startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                    break;

                case RATE:
                    rateCommandReceived(chatId);
                    break;

                case HELP:
                    helpCommandReceived(chatId);
                    break;

                case ARCHIVE_HELP:
                    archiveHelpCommandReceived(chatId);
                    break;

                default:
                    if (messageText.matches(CURRENCIES_ARCHIVE + "\\s+\\d{4}-\\d{2}-\\d{2}")) {
                        handleCurrenciesArchiveCommand(messageText, chatId);
                    } else {
                        sendMessage(chatId, DEFAULT_MESSAGE);
                    }
            }
        }
    }

    private void startCommandReceived(Long chatId, String name) {
        String answer = String.format(START_MESSAGE, name);
        sendMessage(chatId, answer);
    }

    private void rateCommandReceived(Long chatId) {
        List<CurrencyResponseDataDto> currencies = privatBankService.getCurrencies();

        StringBuilder message = new StringBuilder(CURRENCIES);

        currencies.stream()
                .forEach(currency -> {
                    message.append(String.format(BUY_OR_SALE,
                            currency.getCcy(),
                            currency.getBaseCcy(),
                            currency.getBuy(),
                            currency.getSale()));
                });

        sendMessage(chatId, message.toString());
    }

    private void helpCommandReceived(Long chatId) {
        String answer = String.format(HELP_MESSAGE);
        sendMessage(chatId, answer);
    }

    private void archiveHelpCommandReceived(Long chatId) {
        String answer = String.format(ARCHIVE_MESSAGE);
        sendMessage(chatId, answer);
    }

    private void currenciesArchiveCommandReceived(Long chatId, LocalDate date) {
        CurrencyArchiveResponseDto currenciesArchiveRates =
                privatBankService.getCurrenciesArchiveRates(date);

        String formatted = CURRENCIES_RATE.formatted(date);

        StringBuilder message = new StringBuilder(formatted);

        currenciesArchiveRates.getExchangeRate()
                .forEach(currency -> {
                    message.append(String.format(BUY_OR_SALE,
                            currency.getBaseCurrency(),
                            currency.getCurrency(),
                            currency.getSaleRateNB(),
                            currency.getPurchaseRateNB(),
                            currency.getSaleRate(),
                            currency.getPurchaseRate()));
                });

        sendMessage(chatId, message.toString());
    }

    private void handleCurrenciesArchiveCommand(String message, Long chatId) {
        if (message.matches(CURRENCIES_ARCHIVE + "\\s+\\d{4}-\\d{2}-\\d{2}")) {
            String dateString = message.split("\\s+")[1];
            try {
                LocalDate date = LocalDate.parse(dateString);

                if (date.isAfter(LocalDate.now())) {
                    sendMessage(chatId, FIRST_ERROR_MESSAGE);
                } else if (date.isBefore(LocalDate.now().minusYears(DEFAULT_YEAR))) {
                    sendMessage(chatId, SECOND_ERROR_MESSAGE);
                } else {
                    currenciesArchiveCommandReceived(chatId, date);
                }
            } catch (Exception e) {
                sendMessage(chatId, THIRD_ERROR_MESSAGE);
            }
        } else {
            sendMessage(chatId, FOURTH_ERROR_MESSAGE);
        }
    }

    private void sendMessage(Long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException("Telegram failed: " + e.getMessage());
        }
    }
}
