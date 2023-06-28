package org.example.bot.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.example.bot.Bot;
import org.example.bot.listeners.adapters.ExtendedListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.io.FileNotFoundException;
import java.util.Objects;

/**
 * Executes logic for /setchannel command
 */
public class ChangeDefaultChannel extends ExtendedListenerAdapter {
    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (!isCommand(event, "setchannel"))
            return;

        event.deferReply().setEphemeral(false).queue();

        String channelID = Objects.requireNonNull(event.getOption("channel")).getAsChannel().getId();
        String channelName = Objects.requireNonNull(event.getOption("channel")).getAsChannel().getName();

        try {
            Bot.saveConfig(channelID, 0);
        } catch (FileNotFoundException e) {
            System.out.println("Config file does not exist");
            event.getHook().editOriginal("Config file does not exist").queue();
            return;
        }
        event.getHook().editOriginal("Now you can use the BOT only in \"" + channelName + "\"!")
                .queue();
    }
}
