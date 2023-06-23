package org.example.bot.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.example.bot.Bot;
import org.example.bot.listeners.adapters.ExtendedListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.io.FileNotFoundException;
import java.util.Objects;

/**
 * Executes logic for /setwebhook command
 */
public class ChangeYoutubeSearchChannel extends ExtendedListenerAdapter {
    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (!isCommand(event, "setwebhook"))
            return;

        if (!isUserAllowedToExecuteCommand(event))
            return;


        String url = Objects.requireNonNull(event.getOption("url")).getAsString();
        String channelName = event.getChannel().getName();

        if (!url.contains("https://discord.com/api/webhooks/")) {
            event.reply("This is not a WebHook link. Try to create and copy one again.").setEphemeral(false).queue();
            return;
        }

        try {
            Bot.saveConfig(url, 1);
        } catch (FileNotFoundException e) {
            System.out.println("Config file does not exist");
            event.reply("Config file does not exist").setEphemeral(false).queue();
            return;
        }
        event.reply("Now you can use search functions only in \"" + channelName + "\"!").setEphemeral(false)
                .queue();
    }
}
