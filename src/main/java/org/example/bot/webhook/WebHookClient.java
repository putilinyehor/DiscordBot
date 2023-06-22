package org.example.bot.webhook;

import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.WebhookClientBuilder;
import club.minnced.discord.webhook.send.WebhookEmbed;
import club.minnced.discord.webhook.send.WebhookEmbedBuilder;
import org.example.bot.Bot;

public class WebHookClient {
    // WebHook instance
    private WebhookClient client = null;
    /**
     * Constructor
     */
    public WebHookClient() {
        // Creating the builder, initializing WebhookClient
        WebhookClientBuilder builder = new WebhookClientBuilder(Bot.getWebHookUrl()); // or id, token
        builder.setThreadFactory((job) -> {
            Thread thread = new Thread(job);
            thread.setName("YouTube Video Viewer");
            thread.setDaemon(true);
            return thread;
        });
        builder.setWait(true);
        this.client = builder.build();
    }

    /**
     * Displays a list of YouTube videos to user as Embeds
     *
     * @param videos String[][], list of YouTube videos to be displayed
     */
    public void displayYoutubeVideosList(String[][] videos) {
        // initializing embed view to display YouTube videos
        WebhookEmbed embed = null;

        for (String[] video : videos) {
            embed = new WebhookEmbedBuilder()
                    .setTitle(new WebhookEmbed.EmbedTitle(video[0], Bot.getWebHookUrl()))
                    .setDescription(video[1])
                    .setImageUrl(video[2])
                    .setColor(0xFF00EE)
                    .build();

            this.client.send(embed);
        }
    }
}