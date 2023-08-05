package com.barabanov.tgbot;


import java.util.Arrays;

public enum QuoteCallBack
{
    NEED_A_QUOTE("Need a quote"), NO_QUOTE_NEEDED("No quote needed");

    private final String callBackSyntax;


    QuoteCallBack(String callBackSyntax)
    {
        this.callBackSyntax = callBackSyntax;
    }


    public static QuoteCallBack fromString(String callBackAsTxt)
    {
    return QuoteCallBack.valueOf(String.join("_", callBackAsTxt.split(" ")).toUpperCase());
    }


    public static boolean isItCallBack(String callBackAsTxt)
    {
        for (String callBack : Arrays.stream(QuoteCallBack.values()).map(QuoteCallBack::getCallBackSyntax).toList())
            if (callBackAsTxt.equals(callBack))
                return true;

        return false;
    }

    private String getCallBackSyntax() { return callBackSyntax; }
}
