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
                            /youtube - search for a YouTube video.
                                    search          search input
                                    amount          how many videos do you want to display
                                    Features:
                                        react to a video you want to watch with :regional_indicator_v: to re-send it to the channel as a link
                            /build - search for a build from "mobafire.com"
                                    champion        champion you want to search
                                    amount          amount of builds you want to display
                                    Features:
                                        - react to a build you want to display with :regional_indicator_v:
                                        - react to an item list with :regional_indicator_v: to view extended item viewer
                            --------
                            Requests
                            !!ping - bot should respond with Pong! if he is online and working
                            """).setEphemeral(true) // reply or acknowledge.
                .queue();
    }
}
