package com.barabanov.tgbot;

import com.barabanov.tgbot.handler.Command;
import com.barabanov.tgbot.handler.CommandHandler;
import com.barabanov.tgbot.handler.MyFirstBotTg;
import com.barabanov.tgbot.handler.SimpleCommandHandler;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.Arrays;


public class BotStarter
{

    public static void startBot()
    {
        try
        {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(createBot(new SimpleCommandHandler()));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


    private static TelegramLongPollingBot createBot(CommandHandler commandHandler) throws TelegramApiException
    {
        TelegramLongPollingBot bot = new MyFirstBotTg(commandHandler);

        SetMyCommands commandsSet = SetMyCommands
                .builder()
                .commands(Arrays.stream(Command.values())
                        .map(cmd -> new BotCommand(cmd.getCmdSyntax(), cmd.getDescription()))
                        .toList())
                .build();
        bot.executeAsync(commandsSet);

        return bot;
    }
}
