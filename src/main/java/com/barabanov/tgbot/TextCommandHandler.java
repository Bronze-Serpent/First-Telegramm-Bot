package com.barabanov.tgbot;


import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethodMessage;
import org.telegram.telegrambots.meta.api.methods.polls.SendPoll;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Location;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;


public class TextCommandHandler implements CommandHandler
{

    public BotApiMethodMessage handleUpdate(Update update)
    {
        if (update.hasMessage())
        {
            Message msg = update.getMessage();
            if (msg.hasText())
                return processTheText(msg);
            else if (msg.isReply())
                return processTheReply(update);
        }
        else if (update.hasCallbackQuery())
            return processTheCallBackQuery(update);

        return new SendMessage();
    }


    private BotApiMethodMessage processTheReply(Update update)
    {
        Message userMsg = update.getMessage();
        Message replyMsg = userMsg.getReplyToMessage();

        if (replyMsg.hasText())
            if (replyMsg.getText().equals("Обещанная дополнительная функциональность"))
                return ButtonReply(userMsg);

        return new SendMessage();
    }


    private BotApiMethodMessage processTheText(Message msg)
    {
        String msgText = msg.getText();
        if (ButtonCallBack.isItCallBack(msgText))
        {
            return processButtonsCallBack(ButtonCallBack.fromString(msgText), msg);
        }
        else
        {
            String[] msgWords = msgText.split(" ");
            if (Command.isItCmd(msgWords[0]))
            {
                String chatId = String.valueOf(msg.getChatId());

                if (msgWords.length == 1)
                    return processTheCmd(Command.fromString(msgWords[0]), "", chatId);
                else
                {
                    String msgTextWithoutCmd = String.join(" ", Arrays.copyOfRange(msgWords, 1, msgWords.length));
                    return processTheCmd(Command.fromString(msgWords[0]), msgTextWithoutCmd, chatId);
                }
            }
        }

        return new SendMessage();
    }


    private BotApiMethodMessage processTheCallBackQuery(Update update)
    {
        String chatId = update.getCallbackQuery().getMessage().getChatId().toString();
        String callbackText = update.getCallbackQuery().getData();
        if (QuoteCallBack.isItCallBack(callbackText))
            return processQuoteCallBack(QuoteCallBack.fromString(callbackText), chatId);

        return new SendMessage();
    }


    private BotApiMethodMessage processTheCmd(Command cmd, String text, String chatId)
    {
        switch (cmd)
        {
            case START:
                return new SendMessage(String.valueOf(chatId), "Приветствую. Я телеграмм бот. Мои способности весьма ограничены." +
                        " Чтобы посмотреть описание того, что я умею выполните команду /help.");

            case HELP:
                return new SendMessage(chatId, """
                        Есть следующие команды:
                        /hello - поздоровается с вами
                        /message - напишет текст, который вы напишите после команды
                        /more_func - выдаст вам ещё больше функций, но уже в виде кнопочек :)
                        /reflections - даст вам над чем можно будет подумать
                        /smile - улыбнётся вам
                        /start - выводит приветствие, рассказывает о себе и говорит о help
                        /help - описывает какие есть команды""");

            case SMILE:
                return new SendMessage(chatId,
                            List.of(":)", ":]", ":3", ":<)", "/:-]").get(ThreadLocalRandom.current().nextInt(5)));

            case HELLO:
                return new SendMessage(chatId, "Не, серьёзно? Ты нажал команду чтобы я написал тебе привет?" +
                            " Совсем чердак потёк там? Или может тебе совсем заняться нечем? Ну привет, бездельник.");

            case MESSAGE:
                return new SendMessage(chatId, text);

            case REFLECTIONS:
            {
                InlineKeyboardButton putBtn = new InlineKeyboardButton();
                putBtn.setText("Я хочу цитату, пожалуйста");
                putBtn.setCallbackData("Need a quote");

                InlineKeyboardButton getBtn = new InlineKeyboardButton();
                getBtn.setText("Я не хочу цитат!");
                getBtn.setCallbackData("No quote needed");

                InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup(List.of(List.of(putBtn), List.of(getBtn)));
                SendMessage sendMessage = new SendMessage(chatId, "Чего тебе ещё собака надо?");
                sendMessage.setReplyMarkup(keyboardMarkup);

                return sendMessage;
            }

            case MORE_FUNC:
            {
                KeyboardButton pollBtn = new KeyboardButton("I want poll");
                KeyboardButton songBtn = new KeyboardButton("Song");
                KeyboardButton whereIAmBtn = new KeyboardButton("Where I am");
                whereIAmBtn.setRequestLocation(true);

                KeyboardRow fstBtnRow = new KeyboardRow();
                fstBtnRow.addAll(List.of(pollBtn, songBtn));
                KeyboardRow scdBtnRow = new KeyboardRow();
                scdBtnRow.add(whereIAmBtn);

                ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
                keyboardMarkup.setResizeKeyboard(true);
                keyboardMarkup.setSelective(true);
                keyboardMarkup.setKeyboard(List.of(fstBtnRow, scdBtnRow));

                SendMessage buttonMsg = new SendMessage(chatId, "Обещанная дополнительная функциональность");
                buttonMsg.setReplyMarkup(keyboardMarkup);

                return buttonMsg;
            }
        }

        //unreachable line of code
        throw new IllegalArgumentException("Unknown command");
    }


    private  BotApiMethodMessage ButtonReply(Message userMsg)
    {
        if (userMsg.hasLocation())
        {
            Location userLocation = userMsg.getLocation();
            SendMessage sendMessage = new SendMessage(userMsg.getChatId().toString(), String.format(
                    "Ты находишься здесь, потеряшка:\nlongitude = %f\nlatitude = %f", userLocation.getLongitude().floatValue(),
                    userLocation.getLatitude().floatValue()));
            sendMessage.setReplyToMessageId(userMsg.getMessageId());

            return sendMessage;
        }

        return new SendMessage();
    }


    private BotApiMethodMessage processButtonsCallBack(ButtonCallBack btnCallBack, Message msg)
    {
        String chatId = String.valueOf(msg.getChatId());
        switch (btnCallBack)
        {
            case SONG:
                return new SendMessage(chatId,
                        "https://www.youtube.com/watch?v=Zi_XLOBDo_Y&ab_channel=michaeljacksonVEVO");

            case I_WANT_POLL:
            {
                SendPoll sendPoll = new SendPoll(chatId, "Функциональность бота устраивает?",
                        List.of("Абсолютно да", "Полностью да", "Он превосходен", "Лучший собеседник, что у меня когда-либо был"));
                sendPoll.setIsAnonymous(true);
                sendPoll.setType("regular");
                sendPoll.setCorrectOptionId(0);

                return sendPoll;
            }
        }

        //unreachable line of code
        throw new IllegalArgumentException("Unknown callback");
    }


    private BotApiMethodMessage processQuoteCallBack(QuoteCallBack quoteCallBack, String chatId)
    {
        switch (quoteCallBack)
        {
            case NEED_A_QUOTE:
            {
                String quote = List.of("""
                                        Я выделяю три типа людей:
                                        - Говно — плывут по течению и по пути воняют и ищут причину своих неудач во всем, кроме себя, и при этом не пытаются изменить ничего.
                                        - Бревно — плывут тихо по течению и их все устраивает, им не хватает смелости плыть против или хотя бы вонять, как говно.
                                        - Человек разумный — плывет на катере, захотел – повернул, захотел остановился — делает так, как нужно ему, объезжая или проезжая по говну и бревнам, не замечая их ничтожности и вони.
                                        """,
                                "Превосходство — это когда есть на что насрать, и есть чем.",
                                "Многие жалуются на свою внешность, но на мозги не жалуется никто.",
                                "Если ты думаешь, что мой создатель идиот, продолжай думать. Похоже ты на верном пути.",
                                """
                                Когда кажется, что весь мир настроен против тебя - помни, что самолёт взлетает против ветра.

                                 А ещё помни, что не стоит писать против ветра.""")
                        .get(ThreadLocalRandom.current().nextInt(5));
                return new SendMessage(chatId, quote);
            }
            case NO_QUOTE_NEEDED:
                return new SendMessage(chatId, "Ну и чё ты на кнопку нажал раз не хочешь? Сиди и не тыкай тогда. Тоже мне, умник.");
        }

        //unreachable line of code
        throw new IllegalArgumentException("Unknown callback");
    }

}
