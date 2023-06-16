package org.example.bot.listeners;

import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class LoginListener extends ListenerAdapter {
    @Override
    public void onReady(@NotNull ReadyEvent event) {
        System.out.println("Bot is successfully running and is now online!");
    }
}
