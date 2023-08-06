package com.barabanov.tgbot.handler;


import java.util.Arrays;

public enum Command
{
    START("/start", "displays a greeting, talks about himself and talks about help"),
    HELLO("/hello", "say hello to you"),
    MESSAGE("/message", "will write the text that you write after the command separated by a space"),
    MORE_FUNC("/more_func", "will give you even more functions, but already in the form of buttons :)"),
    REFLECTIONS("/reflections", "give you something to think about"),
    SMILE("/smile", "will smile at you"),
    HELP("/help", "describes what commands are");


    private final String cmdSyntax;
    private final String description;


    Command(String cmdSyntax, String description)
    {
        this.cmdSyntax = cmdSyntax;
        this.description = description;
    }


    public static Command fromString(String cmdAsTxt)
    {
        return Command.valueOf(cmdAsTxt.substring(1).toUpperCase());
    }

    public static boolean isItCmd(String cmdAsTxt)
    {
        for (String cmd : Arrays.stream(Command.values()).map(Command::getCmdSyntax).toList())
            if (cmdAsTxt.equals(cmd))
                return true;

        return false;
    }


    public String getCmdSyntax() { return cmdSyntax; }

    public String getDescription() { return description; }
}
