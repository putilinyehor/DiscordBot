package org.example.bot.listeners;

import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import org.example.bot.handler.ReactionResponse;
import org.example.bot.listeners.adapters.ExtendedListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class ReactionListener extends ExtendedListenerAdapter {
    /**
     * Occurs when a reaction to a message is added
     * @param event MessageReactionAddEvent, an instance of an event occurred
     */
    @Override
    public void onMessageReactionAdd(@NotNull MessageReactionAddEvent event) {
        if (!requiresReactionFromBot(event)) {
            return;
        }

        TextChannel channel = event.getChannel().asTextChannel();

        // Check if an original message is a WebHook
        channel.retrieveMessageById(event.getMessageId()).queue((message) -> ReactionResponse.handleReaction(message, event));
    }
}
