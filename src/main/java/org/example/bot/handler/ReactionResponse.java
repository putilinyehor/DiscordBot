package org.example.bot.handler;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.emoji.UnicodeEmoji;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import org.example.parsers.mobafire.BuildParser;
import org.example.parsers.mobafire.ItemParser;

import java.io.IOException;
import java.util.List;
import java.util.Objects;


public class ReactionResponse {
    /**
     * Handles emoji string to execute a specific action on adding an emoji to a WebHook
     *
     * @param message Message, message content instance
     * @param event   MessageReactionAddEvent, event instance to access Emoji Unicode value etc.
     */
    public static void handleReaction(Message message, MessageReactionAddEvent event) {
        if (!message.getAuthor().isBot())
            return;
        if (message.getEmbeds().size() == 0) // if message doesn't content Embeds:
            return;

        MessageEmbed embed = message.getEmbeds().get(0);
        UnicodeEmoji emoji = event.getReaction().getEmoji().asUnicode();

        switch (emoji.getFormatted()) {
//            case "\uD83C\uDDF5" ->  // P
//                Bot.sendMessage(message, "+play " + embed.getDescription());
            case "\uD83C\uDDFB" -> { // V
                if (embed.getFooter() != null) {
                    if (Objects.requireNonNull(embed.getFooter().getText()).contains("youtube")) {
                        event.getChannel().asTextChannel().sendMessage(embed.getFooter().getText()).queue();
                        return;
                    }
                    if (embed.getFooter().getText().contains("mobafire")) {
                        displayChampionBuild(event, embed.getFooter().getText());
                        return;
                    }
                }

                if (embed.getFields().size() != 0) {
                    if (embed.getFields().size() == 1 || embed.getFields().size() == 2) {
                        displayItemsExtended(event, embed);
                        return;
                    }
                    if (embed.getFields().size() == 3) {
                        displayRunesExtended(event, embed);
                        return;
                    }
                }
            }
            default -> {
            }
        }
    }

    private static void displayChampionBuild(MessageReactionAddEvent event, String link) {
        BuildParser parser;
        try {
            parser = new BuildParser(link);
        } catch (IOException e) {
            event.getChannel().asTextChannel().sendMessage("Sorry, cannot display this build").queue();
            return;
        }

        // Temporary used variables
        StringBuilder str = new StringBuilder();
        EmbedBuilder builder;

        // -------- Core items embed ---------
        List<String> coreItems = parser.getCoreItems();
        for (int i = 1; i < coreItems.size(); i++)
            str.append(coreItems.get(i)).append("\n");

        builder = new EmbedBuilder()
                .setTitle("Core items")
                .addField("", str.toString(), false);
        event.getChannel().asTextChannel().sendMessage("").setEmbeds(builder.build()).queue();

        // -------- Runes embed ---------
        String[] runes = parser.getRunes();

        str = new StringBuilder()
                .append(runes[1])
                .append("\n")
                .append(runes[2])
                .append("\n")
                .append(runes[3])
                .append("\n")
                .append(runes[4])
                .append("\n");
        builder = new EmbedBuilder()
                .setTitle("Runes:")
                .addField(runes[0], str.toString(), false);

        str = new StringBuilder()
                .append(runes[6])
                .append("\n")
                .append(runes[7])
                .append("\n");
        builder.addField(runes[5], str.toString(), false);

        str = new StringBuilder()
                .append(runes[8])
                .append("\n")
                .append(runes[9])
                .append("\n")
                .append(runes[10])
                .append("\n");
        builder.addField("Shards:", str.toString(), false);

        event.getChannel().asTextChannel().sendMessage("").setEmbeds(builder.build()).queue();

        // -------- Spells embed ---------
        String[] spells = parser.getSpells();
        builder = new EmbedBuilder()
                .setTitle("Spells:")
                .addField("", spells[0] + "\n" + spells[1], false);
        event.getChannel().asTextChannel().sendMessage("").setEmbeds(builder.build()).queue();

        // -------- Items embed ---------
        List<List<String>> items = parser.getItems();

        for (List<String> row : items) {
            str = new StringBuilder();
            builder = new EmbedBuilder();
            builder.setTitle(row.get(0));

            for (int i = 1; i < row.size() - 1; i++)
                str.append(row.get(i))
                        .append("\n");

            builder.addField("", str.toString(), false);
            if (!row.get(row.size() - 1).equalsIgnoreCase(""))
                builder.addField("Notes:", row.get(row.size() - 1), false);

            event.getChannel().asTextChannel().sendMessage("").setEmbeds(builder.build()).queue();
        }
    }

    private static void displayItemsExtended(MessageReactionAddEvent event, MessageEmbed embed) {
        String items = embed.getFields().get(0).getValue();
        String[] separatedItemNames = Objects.requireNonNull(items).split("\n");

        ItemParser ip;
        try {
            ip = new ItemParser();
        } catch (IOException e) {
            event.getChannel().asTextChannel().sendMessage("Sorry, cannot access items list.").queue();
            return;
        }

        String[][] itemInformation = ip.getItemInformation(separatedItemNames);

        int i = 0;
        for (String[] item : itemInformation) {
            EmbedBuilder builder = new EmbedBuilder()
                    .setTitle(separatedItemNames[i])
                    .setImage(item[0])
                    .setFooter(item[1]);
            event.getChannel().asTextChannel().sendMessage("").setEmbeds(builder.build()).queue();

            i++;
        }
    }

    private static void displayRunesExtended(MessageReactionAddEvent event, MessageEmbed embed) {
        String items =
                embed.getFields().get(0).getValue() + "\n" +
                        embed.getFields().get(1).getValue() + "\n" +
                        embed.getFields().get(2).getValue();
        String[] separatedItemNames = Objects.requireNonNull(items).split("\n");

        ItemParser ip;
        try {
            ip = new ItemParser();
        } catch (IOException e) {
            event.getChannel().asTextChannel().sendMessage("Sorry, cannot access items list.").queue();
            return;
        }

        String[][] runeInformation = ip.getRuneInformation(separatedItemNames);

        int i = 0;
        for (String[] rune : runeInformation) {
            EmbedBuilder builder = new EmbedBuilder()
                    .setTitle(separatedItemNames[i])
                    .setImage(rune[0])
                    .setFooter(rune[1]);
            event.getChannel().asTextChannel().sendMessage("").setEmbeds(builder.build()).queue();

            i++;
        }
    }
}
