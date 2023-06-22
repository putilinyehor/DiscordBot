package org.example.bot.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.example.bot.Bot;

import java.io.FileNotFoundException;

public class RemoveDefaultChannel extends ListenerAdapter {
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (!event.getName().equalsIgnoreCase("removechannel"))
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
