package com.barabanov.tgbot;


import java.util.Arrays;

public enum Command
{
    START("/start"), HELLO("/hello"), MESSAGE("/message"), MORE_FUNC("/more_func"),
    REFLECTIONS("/reflections"), SMILE("/smile"), HELP("/help");


    private final String cmdSyntax;


    Command(String cmdSyntax)
    {
        this.cmdSyntax = cmdSyntax;
    }

    static Command fromString(String cmdAsTxt)
    {
        return Command.valueOf(cmdAsTxt.substring(1).toUpperCase());
    }

    static boolean isItCmd(String cmdAsTxt)
    {
        for (String cmd : Arrays.stream(Command.values()).map(Command::getCmdSyntax).toList())
            if (cmdAsTxt.equals(cmd))
                return true;

        return false;
    }

    public String getCmdSyntax() { return cmdSyntax; }
}
