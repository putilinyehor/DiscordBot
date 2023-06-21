package org.example.bot.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.example.bot.Bot;
import org.example.youtubeapi.YoutubeAPI;
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
        String reply = YoutubeAPI.getSearchResultAsString(result);

        event.reply("Result:\n" + reply).setEphemeral(true)
                .queue();
    }
}
