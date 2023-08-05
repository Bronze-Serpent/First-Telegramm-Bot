package com.barabanov.tgbot;

import java.util.Arrays;

public enum ButtonCallBack
{
    I_WANT_POLL("I want poll"), SONG("Song");

    private final String callBackSyntax;


    ButtonCallBack(String callBackSyntax)
    {
        this.callBackSyntax = callBackSyntax;
    }


    public static ButtonCallBack fromString(String callBackAsTxt)
    {
        return ButtonCallBack.valueOf(String.join("_", callBackAsTxt.split(" ")).toUpperCase());
    }


    public static boolean isItCallBack(String callBackAsTxt)
    {
        for (String callBack : Arrays.stream(ButtonCallBack.values()).map(ButtonCallBack::getCallBackSyntax).toList())
            if (callBackAsTxt.equals(callBack))
                return true;

        return false;
    }

    private String getCallBackSyntax() { return callBackSyntax; }
}
