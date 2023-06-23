package org.example.bot.listeners;

import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import org.example.bot.listeners.adapters.ExtendedListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class LoginListener extends ExtendedListenerAdapter {
    /**
     * Occurs when bot is entering the server
     *
     * @param event GuildJoinEvent, an instance of an event occurred
     */
    @Override
    public void onGuildJoin(@NotNull GuildJoinEvent event) {
        TextChannel channel = Objects.requireNonNull(event.getGuild().getDefaultChannel()).asTextChannel();
        channel.sendMessage("""
                Hello!
                Before you can use the bot, run 2 commands:
                /setchannel
                /setwebhook
                Check out my /help command for more information!""").queue();
    }

    /**
     * Occurs when bot is being online
     *
     * @param event ReadyEvent, an instance of an event occurred
     */
    @Override
    public void onReady(@NotNull ReadyEvent event) {
        System.out.println("Bot is successfully running and is now online!");
    }
}
