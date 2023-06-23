package org.example.bot.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.example.bot.Bot;
import org.example.bot.listeners.adapters.ExtendedListenerAdapter;
import org.example.bot.webhook.WebHookClient;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Executes logic for /youtube command
 */
public class SearchYoutube extends ExtendedListenerAdapter {
    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (!isCommand(event, "youtube"))
            return;

        if (!isUserAllowedToExecuteCommand(event)) {
            return;
        }

        String searchStr = Objects.requireNonNull(event.getOption("search")).getAsString();
        int numberOfVideosReturned = 5; // default number of videos to be returned
        if (event.getOption("amount") != null)
            numberOfVideosReturned = Objects.requireNonNull(event.getOption("amount")).getAsInt();

        String[][] result = Bot.getYoutube().getSearchResult(searchStr, numberOfVideosReturned);
        if (result == null) {
            event.reply("No search results.").setEphemeral(false).queue();
            return;
        }

        WebHookClient webhook;
        try {
            webhook = new WebHookClient();
        } catch (Exception e) {
            event.reply("Something went wrong while trying to display youtube videos. \n" +
                    "Try to run /changesearchchannel command to reset search channel.").setEphemeral(false).queue();
            return;
        }

        event.reply("Search results (Wait until everything will load) :").setEphemeral(false).queue();
        webhook.displayYoutubeVideosList(result);
        webhook.close();
    }
}
