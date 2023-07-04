package org.example.bot.handler;

public class CommandResponse {
    /**
     * Handles message string to execute the right command
     *
     * @param content String, str message
     *
     * @return String, response message, that will be displayed to user
     */
    public static String handleResponse(String content) {

        if (content.equalsIgnoreCase("ping"))
            return "Pong!";

        return "";
    }
}
