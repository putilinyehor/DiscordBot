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
        int numberOfVideosReturned = 5; // default number of videos to be returned

        String searchStr = Objects.requireNonNull(event.getOption("search")).getAsString();
        if (event.getOption("amount") != null)
            numberOfVideosReturned = Objects.requireNonNull(event.getOption("amount")).getAsInt();
        String[][] result = Bot.getYoutube().getSearchResult(searchStr, numberOfVideosReturned);

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
