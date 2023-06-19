package org.example.bot.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class HelpCommand extends ListenerAdapter {
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equalsIgnoreCase("help")) {

            event.reply("""
                            /changechannel - change the default channel the BOT is operating in
                                                        
                            Always use !! before asking the bot to do something!
                                                        
                            !!ping - bot should respond with Pong! if he is online and working
                            """).setEphemeral(true) // reply or acknowledge.
                    .queue();
        }
    }
}
