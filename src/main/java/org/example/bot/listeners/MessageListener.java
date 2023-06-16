package org.example.bot.listeners;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.example.bot.Bot;
import org.jetbrains.annotations.NotNull;


public class MessageListener extends ListenerAdapter {
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (!Bot.getDefaultChannelId().equalsIgnoreCase("none"))
            if (!event.getChannel().getId().equalsIgnoreCase(Bot.getDefaultChannelId()))
                return;

        if (event.getAuthor().isBot())
            return;

        Message message = event.getMessage();
        String content = event.getMessage().getContentRaw();

        Bot.sendMessage(message, content);
    }

}
