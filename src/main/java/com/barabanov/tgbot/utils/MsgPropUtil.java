package com.barabanov.tgbot.utils;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

public class MsgPropUtil
{
    private static final Properties PROPERTIES = new Properties();

    private static final List<String> QUOTES = new LinkedList<>(); //quotes must be formatted as "quote.*number*" in the file

    private static final List<String> SMILES = new LinkedList<>(); //smiles must be formatted as "smile.*number*" in the file


    static
    {
        loadProperties();
        readQuotes();
        readSmiles();
    }


    private MsgPropUtil() {}


    public static String get(String key)
    {
        return PROPERTIES.getProperty(key);
    }

    public static List<String> getSmiles() { return SMILES; }

    public static List<String> getQuotes() { return QUOTES; }


    private static void loadProperties()
    {
        try (var inputStream = AppPropUtil.class.getClassLoader().getResourceAsStream("messages.properties"))
        {
            PROPERTIES.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private static void readSmiles()
    {
        for (int i = 0; ;i++)
        {
            String readSmile = PROPERTIES.getProperty("smile." + i);
            if (readSmile != null)
                SMILES.add(readSmile);
            else
                return;
        }
    }


    private static void readQuotes()
    {
        for (int i = 0; ;i++)
        {
            String readQuote = PROPERTIES.getProperty("quote." + i);
            if (readQuote != null)
                QUOTES.add(readQuote);
            else
                return;
        }
    }
}
