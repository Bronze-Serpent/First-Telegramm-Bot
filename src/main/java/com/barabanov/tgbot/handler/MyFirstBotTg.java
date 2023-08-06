package com.barabanov.tgbot.handler;

import com.barabanov.tgbot.utils.AppPropUtil;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethodMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


public class MyFirstBotTg extends TelegramLongPollingBot
{

    private static final String BOT_TOKEN = "bot.token";
    private static final String BOT_USERNAME = "bot.username";

    private final CommandHandler commandHandler;


    public MyFirstBotTg(CommandHandler commandHandler)
    {
        this.commandHandler = commandHandler;
    }


    @Override
    public void onUpdateReceived(Update update)
    {
        BotApiMethodMessage sendMsg = commandHandler.handleUpdate(update);

        try {
            execute(sendMsg);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }


    @Override
    public String getBotUsername() {
        return AppPropUtil.get(BOT_USERNAME);
    }


    @Override
    public String getBotToken()
    {
        return AppPropUtil.get(BOT_TOKEN);
    }

}
