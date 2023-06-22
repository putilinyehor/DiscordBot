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
import org.example.bot.commands.*;
import org.example.bot.handler.Responses;
import org.example.bot.listeners.LoginListener;
import org.example.bot.listeners.MessageListener;
import org.example.youtubeapi.YoutubeAPI;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Bot {
    // Path to "keys.yml" configuration file
    private static final String keysFilePath = System.getProperty("user.dir") + "\\src\\main\\java\\org\\example\\configuration\\keys.yml";
    // Path to "config.yml" configuration file
    private static final String configFilePath = System.getProperty("user.dir") + "\\src\\main\\java\\org\\example\\configuration\\config.yml";
    private static String defaultChannelId = "";
    private static String webHookUrl = "";
    // Discord bot token that is stored in "keys.yml"
    private static String token = "";
    // YouTube's authorization token, to access a video database. Google Cloud > YouTube Data v3 API
    private static YoutubeAPI youtube = null;
    // JDA instance
    private static JDA jda = null;

    /**
     * Constructor
     */
    public Bot() {
        try {
            loadInitialConfiguration();
        } catch (FileNotFoundException e) {
            System.out.println("Config file does not exist");
            System.exit(-1);
        }
    }

    /**
     * Runs discord bot (1initialize JDA)
     */
    public void runBot() {
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

    public static String getToken() {
        return token;
    }

    public static YoutubeAPI getYoutube() {
        return youtube;
    }

    public static String getDefaultChannelId() {
        return defaultChannelId;
    }

    public static String getWebHookUrl() {
        return webHookUrl;
    }

    /**
     * Loads configuration and keys from files
     *
     * @throws FileNotFoundException if config file does not exist
     */
    private static void loadInitialConfiguration() throws FileNotFoundException {
        InputStream inputStream = null;
        Yaml yaml = new Yaml();
        Map<String, String> data;

        inputStream = new FileInputStream(keysFilePath);
        data = yaml.load(inputStream);
        token = data.get("token");
        String apiKey = data.get("api-key");

        inputStream = new FileInputStream(configFilePath);
        data = yaml.load(inputStream);
        defaultChannelId = data.get("default-channel-id");
        webHookUrl = data.get("webhook-url");

        initialiseYoutubeInstance(apiKey);
    }

    /**
     * Saves configuration file
     *
     * @param value String, id that needs to be saved
     * @param ch int, indicates, what needs to be save,
     *           <br>0 - save defaultChannelId
     *           <br>1 - save webHookUrl
     *
     * @throws FileNotFoundException if config file does not exist
     */
    public static void saveConfig(String value, int ch) throws FileNotFoundException {
        switch (ch) {
            case 0 -> defaultChannelId = value;
            case 1 -> webHookUrl = value;
            default -> {
                return;
            }
        }

        Map<String, String> data = new HashMap<>();
        data.put("default-channel-id", defaultChannelId);
        data.put("webhook-url", webHookUrl);

        PrintWriter writer = new PrintWriter(configFilePath);
        Yaml yaml = new Yaml();
        yaml.dump(data, writer);
    }

    /**
     * Initializes YouTube instance to work with a video database
     *
     * @param apiKey String, API key from Google Cloud Auth
     */
    private static void initialiseYoutubeInstance(String apiKey) {
        try {
            youtube = new YoutubeAPI(apiKey);
        } catch (IOException e) {
            System.out.println("Cannot connect to Youtube Search database. Search functions are not available. Exiting the program...");
            System.exit(-2);
        }
    }

    /**
     * Configures basic bot settings
     *
     * @param builder JDABuilder, builder instance
     */
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

    /**
     * Configures memory usage of a bot to get better performance
     *
     * @param builder JDABuilder, builder instance
     */
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

    /**
     * Registers listeners bot uses
     *
     * @param builder JDABuilder, builder instance
     */
    private static void registerListeners(JDABuilder builder) {
        // Listeners
        builder.addEventListeners(new LoginListener());
        builder.addEventListeners(new MessageListener());

        // Slash Commands
        builder.addEventListeners(new HelpCommand());
        builder.addEventListeners(new ChangeDefaultChannelCommand());
        builder.addEventListeners(new RemoveDefaultChannel());
        builder.addEventListeners(new SearchYoutubeCommand());
        builder.addEventListeners(new ChangeYoutubeSearchChannel());
    }

    /**
     * Registers commands bot provides
     */
    private static void registerCommands() {
        jda.updateCommands().addCommands(
                Commands.slash("help", "List of all commands available, with / and !!"),
                Commands.slash("changechannel", "Change a channel, where the !! commands are ran.").
                        addOption(OptionType.CHANNEL, "channel", "The new channel to operate in", true),
                Commands.slash("removechannel", "Remove the default channel to use the BOT everywhere."),
                Commands.slash("youtube", "Search for a youtube video")
                        .addOption(OptionType.STRING, "search", "What you want to search for", true)
                        .addOption(OptionType.INTEGER, "amount", "Set how many results you want to have, 5 if not specified", false),
                Commands.slash("changesearchchannel", "Change a channel where you search for YouTube videos")
                        .addOption(OptionType.STRING, "url", "Go to Create Webhook and paste a URL you were provided with", true)
        ).queue();
    }

    /**
     * Sends message to a discord channel
     *
     * @param message Message, message content instance
     * @param content String, str message
     */
    public static void sendMessage(Message message, String content) {
        if (content.length() < 2 || !content.substring(0, 2).equalsIgnoreCase("!!"))
            return;

        String responseMessage = Responses.handleResponse(message, content.substring(2));
        if (responseMessage.equalsIgnoreCase("")) {
            return;
        }

        MessageChannel channel = message.getChannel();
        channel.sendMessage(responseMessage).queue();
    }
}
