package org.example.bot.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.example.bot.Bot;
import org.example.bot.listeners.adapters.ExtendedListenerAdapter;
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

        event.deferReply().setEphemeral(false).queue();

        String search = Objects.requireNonNull(event.getOption("search")).getAsString();
        int numberOfVideosReturned = 5; // default number of videos to be returned
        if (event.getOption("amount") != null)
            numberOfVideosReturned = Objects.requireNonNull(event.getOption("amount")).getAsInt();

        String[][] result = Bot.getYoutube().getSearchResult(search, numberOfVideosReturned);
        if (result == null) {
            event.getHook().editOriginal("No search results.").queue();
            return;
        }

        event.getHook().editOriginal("Search results:").queue();
        displayYoutubeVideosList(event, result);

    }

    /**
     * Displays a list of YouTube videos to user as Embeds
     *
     * @param videos String[][], list of YouTube videos to be displayed
     */
    public void displayYoutubeVideosList(SlashCommandInteractionEvent event, String[][] videos) {
        // initializing embed builder to display YouTube videos
        EmbedBuilder embedBuilder;

        for (String[] video : videos) {
            embedBuilder = new EmbedBuilder()
                    .setTitle(video[0])
                    .setFooter(video[1])
                    .setImage(video[2])
                    .setColor(0xFF00EE);

            event.getChannel().asTextChannel().sendMessage("").setEmbeds(embedBuilder.build()).queue();
        }
    }
}
