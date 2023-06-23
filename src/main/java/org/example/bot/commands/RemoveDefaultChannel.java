package org.example.bot.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.example.bot.Bot;
import org.example.bot.listeners.adapters.ExtendedListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.io.FileNotFoundException;

/**
 * Executes logic for /removechannel command
 */
public class RemoveDefaultChannel extends ExtendedListenerAdapter {
    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (!isCommand(event, "removechannel"))
            return;

        try {
            Bot.saveConfig("none", 0);
        } catch (FileNotFoundException e) {
            System.out.println("Config file does not exist");
            event.reply("Config file does not exist").setEphemeral(false).queue();
            return;
        }
        event.reply("Successfully removed default channel. Now you can use the BOT anywhere!").setEphemeral(false).queue();
    }
}
