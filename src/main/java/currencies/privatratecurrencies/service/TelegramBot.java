package currencies.privatratecurrencies.service;

import currencies.privatratecurrencies.config.BotConfig;
import currencies.privatratecurrencies.dto.CurrencyResponseDataDto;
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
    private static final String DEFAULT_ANSWER = "Sorry, command was not recognized";
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
                case "/start":
                    startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                    break;

                case "/rate":
                    rateCommandReceived(chatId);
                    break;

                case "/help":
                    helpCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                    break;

                default: sendMessage(chatId, DEFAULT_ANSWER);
            }
        }
    }

    private void startCommandReceived(Long chatId, String name) {
        String answer = """
                Вітаю, %s, у PrivatBankCurrenciesRate!
                
                Дізнайтесь більше інформації за допомогою команди /help
                
                """.formatted(name);

        sendMessage(chatId, answer);
    }

    private void rateCommandReceived(Long chatId) {
        List<CurrencyResponseDataDto> currencies = privatBankService.getCurrencies();

        StringBuilder message = new StringBuilder("Курс валют:\n");

        currencies.stream()
                .forEach(currency -> {
                    message.append(String.format("%s/%s: Купівля: %s, Продаж: %s\n",
                            currency.getCcy(),
                            currency.getBaseCcy(),
                            currency.getBuy(),
                            currency.getSale()));
                });

        sendMessage(chatId, message.toString());
    }

    private void helpCommandReceived(Long chatId, String name) {
        String answer = """
                Вітаю, %s!
                
                Щоб запустити бота використовуйте команду /start
                
                Щоб дізнатись більше інформації використовуйте команду /help
                
                Щоб дізнатись курс валют використовуйте команду /rate
                
                """.formatted(name);

        sendMessage(chatId, answer);
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
