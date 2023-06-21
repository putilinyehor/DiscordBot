package org.example.bot.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.example.bot.Bot;

import java.io.FileNotFoundException;
import java.util.Objects;

public class ChangeDefaultChannelCommand extends ListenerAdapter {
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (!event.getName().equals("changechannel"))
            return;

        String channelID = Objects.requireNonNull(event.getOption("channel")).getAsChannel().getId();
        String channelName = Objects.requireNonNull(event.getOption("channel")).getAsChannel().getName();
        try {
            Bot.saveConfig(channelID);
        } catch (FileNotFoundException e) {
            System.out.println("Config file does not exist");
            event.reply("Config file does not exist").setEphemeral(false).queue();
            return;
        }
        event.reply("Now you can use the BOT only in \"" + channelName + "\"!").setEphemeral(false)// reply or acknowledge
                .queue(); // Queue both reply and edit
//                .flatMap(v ->
//                        event.getHook().editOriginalFormat("Pong!") // edit original message
//                )
    }
}
