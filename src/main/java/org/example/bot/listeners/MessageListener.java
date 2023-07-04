package org.example.bot.listeners;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.example.bot.handler.CommandResponse;
import org.example.bot.listeners.adapters.ExtendedListenerAdapter;
import org.jetbrains.annotations.NotNull;


public class MessageListener extends ExtendedListenerAdapter {
    /**
     * Occurs when a message is received
     *
     * @param event MessageReceivedEvent, an instance of an event occurred
     */
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (ExtendedListenerAdapter.isUserAllowedToExecuteCommand(event)) {
            return;
        }

        if (event.getAuthor().isBot())
            return;


        Message message = event.getMessage();
        String content = event.getMessage().getContentRaw();

        sendMessage(message, content);
    }

    /**
     * Sends message to a discord channel
     *
     * @param message Message, message content instance
     * @param content String, str message
     */
    public static void sendMessage(Message message, String content) {
        if (content.length() < 2 || !content.substring(0, 2).equalsIgnoreCase("!!"))
            return;

        String responseMessage = CommandResponse.handleResponse(content.substring(2));
        if (responseMessage.equalsIgnoreCase("")) {
            return;
        }

        MessageChannel channel = message.getChannel();
        channel.sendMessage(responseMessage).queue();
    }

}
