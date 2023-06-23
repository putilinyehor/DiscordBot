package org.example.bot.listeners.adapters;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.example.bot.Bot;
import org.jetbrains.annotations.NotNull;

/**
 * Extends original ListenerAdapter class
 */
public class ExtendedListenerAdapter extends ListenerAdapter {
    /**
     * Checks if the inputted command matches the command logic that needs to be executed
     *
     * @param event SlashCommandInteractionEvent, an instance of an event occurred
     * @param command String, command that has been inputted
     * @return true if the commands match
     */
    public static boolean isCommand(@NotNull SlashCommandInteractionEvent event, String command) {
        return event.getName().equalsIgnoreCase(command);
    }

    /**
     * Check if the command is allowed to be run in a specific channel - defaultChannel
     * @see Bot
     *
     * @param event SlashCommandInteractionEvent, an instance of an event occurred
     * @return true if a user can run the / command
     */
    public static boolean isUserAllowedToExecuteCommand(@NotNull SlashCommandInteractionEvent event) {
        if (event.getChannel().getId().equalsIgnoreCase(Bot.getDefaultChannelId()))
            return true;

        event.reply("You cannot call slash commands from this channel. \n" +
                "Check if you have set the default channel with /changechannel").setEphemeral(true).queue();
        return false;
    }

    /**
     * Check if the command is allowed to be run in a specific channel - defaultChannel
     * @see Bot
     *
     * @param event MessageReceivedEvent, an instance of an event occurred
     * @return true if a user can run the !! command
     */
    public static boolean isUserAllowedToExecuteCommand(@NotNull MessageReceivedEvent event) {
        return event.getChannel().getId().equalsIgnoreCase(Bot.getDefaultChannelId());
    }

    /**
     * Checks if an additional action is needed if a user added a reaction to a message
     * @see Bot
     *
     * @param event MessageReactionAddEvent, an instance of an event occurred
     * @return true if additional action is needed
     */
    public static boolean requiresReactionFromBot(@NotNull MessageReactionAddEvent event) {
        return event.getChannel().getId().equalsIgnoreCase(Bot.getDefaultChannelId());
    }
}
