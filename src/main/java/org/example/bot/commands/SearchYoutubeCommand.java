package org.example.bot.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.example.bot.Bot;
import org.example.bot.webhook.WebHookClient;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class SearchYoutubeCommand extends ListenerAdapter {
    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (!event.getName().equalsIgnoreCase("youtube"))
            return;

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

        event.reply("Search results (Please wait until they all load fully)").setEphemeral(false).queue();
        webhook = new WebHookClient();
        webhook.displayYoutubeVideosList(result);
    }
}
