package org.example.bot.handler;

import net.dv8tion.jda.api.entities.Message;

public class Responses {
    public static String handleResponse(Message message, String content) {

        if (content.equalsIgnoreCase("ping"))
            return "Pong!";

        return "";
    }
}
