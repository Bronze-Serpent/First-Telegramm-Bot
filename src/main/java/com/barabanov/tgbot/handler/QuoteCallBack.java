package com.barabanov.tgbot.handler;


import java.util.Arrays;

enum QuoteCallBack
{
    NEED_A_QUOTE("Need a quote"), NO_QUOTE_NEEDED("No quote needed");

    private final String callBackSyntax;


    QuoteCallBack(String callBackSyntax)
    {
        this.callBackSyntax = callBackSyntax;
    }


    static QuoteCallBack fromString(String callBackAsTxt)
    {
    return QuoteCallBack.valueOf(String.join("_", callBackAsTxt.split(" ")).toUpperCase());
    }


    static boolean isItCallBack(String callBackAsTxt)
    {
        for (String callBack : Arrays.stream(QuoteCallBack.values()).map(QuoteCallBack::getCallBackSyntax).toList())
            if (callBackAsTxt.equals(callBack))
                return true;

        return false;
    }

    String getCallBackSyntax() { return callBackSyntax; }
}
