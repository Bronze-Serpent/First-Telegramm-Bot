package com.barabanov.tgbot.handler;


import com.barabanov.tgbot.utils.MsgPropUtil;
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


public class SimpleCommandHandler implements CommandHandler
{

    private static final String START_MSG = "start";
    private static final String HELLO_MSG = "hello";
    private static final String NO_QUOTE= "quote.no";
    private static final String addFuncMsg = "Additional functionality";


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
            if (replyMsg.getText().equals(addFuncMsg))
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


    private BotApiMethodMessage processTheCmd(Command command, String text, String chatId)
    {
        switch (command)
        {
            case START:
                return new SendMessage(String.valueOf(chatId), MsgPropUtil.get(START_MSG));

            case HELP:
                return new SendMessage(chatId, "There are following commands:\n" +
                        Arrays.stream(Command.values())
                        .map(cmd -> cmd.getCmdSyntax() + " - " + cmd.getDescription()) + "\n");

            case SMILE:
                return new SendMessage(chatId,
                        MsgPropUtil.getSmiles().get(ThreadLocalRandom.current().nextInt(5)));

            case HELLO:
                return new SendMessage(chatId, MsgPropUtil.get(HELLO_MSG));

            case MESSAGE:
                return new SendMessage(chatId, text);

            case REFLECTIONS:
            {
                InlineKeyboardButton putBtn = new InlineKeyboardButton();
                putBtn.setText("I want a quote, please");
                putBtn.setCallbackData(QuoteCallBack.NEED_A_QUOTE.getCallBackSyntax());

                InlineKeyboardButton getBtn = new InlineKeyboardButton();
                getBtn.setText("I don't want a quote");
                getBtn.setCallbackData(QuoteCallBack.NO_QUOTE_NEEDED.getCallBackSyntax());

                InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup(List.of(List.of(putBtn), List.of(getBtn)));
                SendMessage sendMessage = new SendMessage(chatId, "What else do you need?");
                sendMessage.setReplyMarkup(keyboardMarkup);

                return sendMessage;
            }

            case MORE_FUNC:
            {
                KeyboardButton pollBtn = new KeyboardButton(ButtonCallBack.I_WANT_POLL.getCallBackSyntax());
                KeyboardButton songBtn = new KeyboardButton(ButtonCallBack.SONG.getCallBackSyntax());
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

                SendMessage buttonMsg = new SendMessage(chatId, addFuncMsg);
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
                    "You are here, lost:\nlongitude = %f\nlatitude = %f", userLocation.getLongitude().floatValue(),
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
                SendPoll sendPoll = new SendPoll(chatId, "Are you satisfied with the functionality of the bot?",
                        List.of("Absolutely yes", "Completely yes", "He is excellent", "The best interlocutor I have ever had"));
                sendPoll.setIsAnonymous(true);
                sendPoll.setAllowMultipleAnswers(true);
                sendPoll.setType("quiz");
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
                String quote = MsgPropUtil.getQuotes().get(ThreadLocalRandom.current().nextInt(5));
                return new SendMessage(chatId, quote);
            }
            case NO_QUOTE_NEEDED:
                return new SendMessage(chatId, MsgPropUtil.get(NO_QUOTE));
        }

        //unreachable line of code
        throw new IllegalArgumentException("Unknown callback");
    }

}
