package org.example.bot.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.example.bot.listeners.adapters.ExtendedListenerAdapter;
import org.example.parsers.mobafire.BuildsParser;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Executes logic for /build command
 */
public class SearchBuild extends ExtendedListenerAdapter {
    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (!isCommand(event, "build"))
            return;

        if (!isUserAllowedToExecuteCommand(event)) {
            return;
        }

        event.deferReply().setEphemeral(false).queue();

        String champion = Objects.requireNonNull(event.getOption("champion")).getAsString();
        int numberOfBuilds = 5; // default number of videos to be returned
        if (event.getOption("amount") != null)
            numberOfBuilds = Objects.requireNonNull(event.getOption("amount")).getAsInt();
        if (numberOfBuilds > 10) {
            event.getHook().editOriginal("You cannot display more than 10 builds, sorry").queue();
            return;
        }


        BuildsParser mwp;
        try {
            mwp = new BuildsParser(champion);
        } catch (IllegalArgumentException e) {
            event.getHook().editOriginal("No search results, wrong champion name").queue();
            return;
        }

        String[][] builds = mwp.getChampionBuildsInformation(numberOfBuilds);
        event.getHook().editOriginal("Search results:").queue();
        displayBuildsList(event, builds);
    }

    /**
     * Displays a list of champion builds to user as Embeds
     *
     * @param builds String[][], list of champion builds to be displayed
     */
    public void displayBuildsList(SlashCommandInteractionEvent event, String[][] builds) {
        // initializing embed builder to display champion builds
        EmbedBuilder embedBuilder;

        for (String[] el : builds) {
            if (BuildsParser.isEmpty(el))
                continue;

            embedBuilder = new EmbedBuilder()
                    .setTitle(el[0])
                    .setDescription("Rating: " + el[3] +
                            "\nLikes: " + el[4] +
                            "\nDislikes: " + el[5])
                    .setFooter(el[1])
                    .setImage(el[2])
                    .setColor(0xFF00EE);

            event.getChannel().asTextChannel().sendMessage("").setEmbeds(embedBuilder.build()).queue();
        }
    }
}
