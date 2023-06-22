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
                            /changechannel - change the default channel, where !! commands are available
                                    channel         choose the channel you want
                            /removechannel - remove default channel, BOT will be available anywhere
                            
                            /youtube - search for a YouTube video
                                    search          search input
                                    amount          how many videos do you want to display
                            /changesearchchannel - as you can search for youtube videos only in one channel,
                                            this command is used to set the channel where you can search for
                                            YouTube videos
                                    url             url of a webhook that you have created for a required channel
                            
                            --------
                            Requests
                            !!ping - bot should respond with Pong! if he is online and working
                            """).setEphemeral(true) // reply or acknowledge.
                .queue();
    }
}
