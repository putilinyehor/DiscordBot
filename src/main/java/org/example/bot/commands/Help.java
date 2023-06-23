package org.example.bot.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.example.bot.listeners.adapters.ExtendedListenerAdapter;
import org.jetbrains.annotations.NotNull;

/**
 * Executes logic for /help command
 */
public class Help extends ExtendedListenerAdapter {
    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (!isCommand(event, "help"))
            return;

        event.reply("""
                            Here is the list of commands and requests available:
                            --------
                            Commands
                            Can run from any channel:
                            /setchannel - change the default channel, where !! commands are available
                                    channel         choose the channel you want
                            /removechannel - remove default channel, BOT will be available anywhere
                            
                            Can run after setting default channel with /setchannel
                            /setwebhook - as you can search for youtube videos only in one channel, this command is used to set WebHook to display information from YouTube etc. Note that WebHook should be set in the channel, that you've already set with /setchannel command
                                    url             url of a webhook that you have created for a required channel
                            /youtube - search for a YouTube video.
                            IF ONLY "Search results (Wait until everything will load) :" is displayed, then create a webhook and run /setwebhook first
                                    search          search input
                                    amount          how many videos do you want to display
                           
                            --------
                            Requests
                            !!ping - bot should respond with Pong! if he is online and working
                            """).setEphemeral(true) // reply or acknowledge.
                .queue();
    }
}
