package com.barabanov.tgbot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethodMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


public class MyFirstBotTg extends TelegramLongPollingBot
{

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
        return "MyFirstBot";
    }


    @Override
    public String getBotToken()
    {
        return "6549044859:AAHIoWC2ca9MvXTTUPsrBEAOyhjUHJiIARM";
    }

}
