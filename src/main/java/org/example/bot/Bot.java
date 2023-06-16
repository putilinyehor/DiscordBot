package org.example.bot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.example.bot.commands.ChangeDefaultChannelCommand;
import org.example.bot.commands.HelpCommand;
import org.example.bot.commands.RemoveDefaultChannel;
import org.example.bot.handler.Responses;
import org.example.bot.listeners.LoginListener;
import org.example.bot.listeners.MessageListener;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Bot {
    private static final String configFilePath = System.getProperty("user.dir") + "\\src\\main\\java\\org\\example\\configuration\\config.yml";

    private static String defaultChannelId = "";
    private static String token = "";
    private static JDA jda = null;
    public Bot() {
        try {
            loadConfig();
        } catch (FileNotFoundException e) {
            System.out.println("Config file does not exist");
            System.exit(-1);
        }
    }

    public void run_bot() {
        JDABuilder builder = JDABuilder.createDefault(token);

        configureBasicSettings(builder);
        configureMemoryUsage(builder);
        registerListeners(builder);

        jda = builder.build();

        registerCommands();
    }

    public static JDA getJda() {
        return jda;
    }

    public static String getDefaultChannelId() {
        return defaultChannelId;
    }

    private static void loadConfig() throws FileNotFoundException {
        InputStream inputStream = new FileInputStream(new File(configFilePath));

        Yaml yaml = new Yaml();
        Map<String, String> data = yaml.load(inputStream);
        token = data.get("token");
        defaultChannelId = data.get("default-channel-id");
    }

    public static void saveDefaultChannelNameInConfig(String id) throws FileNotFoundException {
        defaultChannelId = id;

        Map<String, String> data = new HashMap<>();
        data.put("token", token);
        data.put("default-channel-id", defaultChannelId);

        PrintWriter writer = new PrintWriter(new File(configFilePath));
        Yaml yaml = new Yaml();
        yaml.dump(data, writer);
    }

    private static void configureBasicSettings(JDABuilder builder) {
        // Enable intents to access message content
        builder.enableIntents(GatewayIntent.MESSAGE_CONTENT);
        // Disable parts of the cache
        builder.disableCache(CacheFlag.MEMBER_OVERRIDES, CacheFlag.VOICE_STATE);
        // Enable the bulk delete event
        builder.setBulkDeleteSplittingEnabled(false);
        // Set activity (like "playing Something")
        builder.setActivity(Activity.watching("after you from under your bed"));
    }

    private static void configureMemoryUsage(JDABuilder builder) {
        // Disable cache for member activities (streaming/games/spotify)
        builder.disableCache(CacheFlag.ACTIVITY);

        // Only cache members who are either in a voice channel or owner of the guild
        builder.setMemberCachePolicy(MemberCachePolicy.VOICE.or(MemberCachePolicy.OWNER));

        // Disable member chunking on startup
        builder.setChunkingFilter(ChunkingFilter.NONE);

        // Consider guilds with more than 50 members as "large".
        // Large guilds will only provide online members in their setup and thus reduce bandwidth if chunking is disabled.
        builder.setLargeThreshold(50);
    }

    private static void registerListeners(JDABuilder builder) {
        // Listeners
        builder.addEventListeners(new LoginListener());
        builder.addEventListeners(new MessageListener());

        // Slash Commands
        builder.addEventListeners(new ChangeDefaultChannelCommand());
        builder.addEventListeners(new RemoveDefaultChannel());
        builder.addEventListeners(new HelpCommand());
    }

    private static void registerCommands() {
        jda.updateCommands().addCommands(
                Commands.slash("changechannel", "Change a channel, where the BOT is operating.").
                        addOption(OptionType.CHANNEL, "channel", "The new channel to operate in", true),
                Commands.slash("removechannel", "Remove the default channel to use the BOT everywhere."),
                Commands.slash("help", "List of all commands available, with / and !!")
        ).queue();
    }

    public static void sendMessage(Message message, String content) {
        int contentLength = content.length();

        if (contentLength > 2 && content.substring(0, 2).equalsIgnoreCase("!!")) {
            String responseMessage = Responses.handleResponse(message, content.substring(2, contentLength));
            if (!responseMessage.equalsIgnoreCase("")) {
                MessageChannel channel = message.getChannel();
                channel.sendMessage(responseMessage).queue();
            }
        }
    }
}
