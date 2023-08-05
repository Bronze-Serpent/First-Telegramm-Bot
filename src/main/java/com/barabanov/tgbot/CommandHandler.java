package com.barabanov.tgbot;


import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethodMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface CommandHandler
{

    BotApiMethodMessage handleUpdate(Update update);

}
