package org.example.bot.handler;

import net.dv8tion.jda.api.entities.Message;

public class Responses {
    /**
     * Handles message string to execute the right command
     *
     * @param message Message, message content instance
     * @param content String, str message
     *
     * @return String, response message, that will be displayed to user
     */
    public static String handleResponse(Message message, String content) {

        if (content.equalsIgnoreCase("ping"))
            return "Pong!";

        return "";
    }
}
