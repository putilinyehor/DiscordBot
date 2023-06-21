package org.example.bot.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class HelpCommand extends ListenerAdapter {
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (!event.getName().equalsIgnoreCase("help"))
            return;

        event.reply("""
                            Here is the list of commands and requests available:
                            --------
                            Commands
                            /changechannel - change the default channel, BOT is operating in
                            /removechannel - remove default channel, BOT will be available anywhere
                            
                            --------
                            Requests
                            !!ping - bot should respond with Pong! if he is online and working
                            """).setEphemeral(true) // reply or acknowledge.
                .queue();
    }
}
