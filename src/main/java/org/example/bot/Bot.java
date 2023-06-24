package org.example.bot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.example.bot.commands.*;
import org.example.bot.listeners.LoginListener;
import org.example.bot.listeners.MessageListener;
import org.example.bot.listeners.ReactionListener;
import org.example.youtubeapi.YoutubeAPI;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Bot {
    // YouTube's authorization token, to access a video database. Google Cloud > YouTube Data v3 API
    private static YoutubeAPI youtube = null;
    // JDA instance
    private static JDA jda = null;

    /**
     * Constructor
     */
    public Bot() {
        try {
            Variables.loadInitialConfiguration();
        } catch (FileNotFoundException e) {
            System.out.println("Config file does not exist");
            System.exit(-1);
        }
    }

    /**
     * Runs discord bot (1initialize JDA)
     */
    public void runBot() {
        JDABuilder builder = JDABuilder.createDefault(Variables.token);

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
        return Variables.token;
    }

    public static YoutubeAPI getYoutube() {
        return youtube;
    }

    public static String getDefaultChannelId() {
        return Variables.defaultChannelId;
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
        Variables.saveConfig(value, ch);
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
        // CacheFlag.VOICE_STATE enable to access memberVoiceState
        // Enable the bulk deleted event
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
        builder.addEventListeners(new ReactionListener());

        // Slash Commands
        builder.addEventListeners(new Help());
        builder.addEventListeners(new ChangeDefaultChannel());
        builder.addEventListeners(new RemoveDefaultChannel());
        builder.addEventListeners(new SearchYoutube());
    }

    /**
     * Registers commands bot provides
     */
    private static void registerCommands() {
        jda.updateCommands().addCommands(
                Commands.slash("help", "List of all commands available, with / and !!"),
                Commands.slash("setchannel", "Change a channel, where commands are ran.").
                        addOption(OptionType.CHANNEL, "channel", "The new channel to operate in", true),
                Commands.slash("removechannel", "Remove a channel, where bot operates to prevent access to all functions."),
                Commands.slash("youtube", "Search for a youtube video")
                        .addOption(OptionType.STRING, "search", "What you want to search for", true)
                        .addOption(OptionType.INTEGER, "amount", "Set how many results you want to have, 5 if not specified", false)
        ).queue();
    }

    /**
     * Sends message to a required channel using Message message
     *
     * @param message Message, an instance of a message that has been sent
     * @param content String, content to be sent
     */
    public static void sendMessage(Message message, String content) {
        TextChannel channel = message.getChannel().asTextChannel();
        channel.sendMessage(content).queue();
    }

    /**
     * Contains all variables that the bot needs
     */
    private static class Variables {
        // Path to "keys.yml" configuration file
        private static final String keysFilePath = System.getProperty("user.dir") + "\\src\\main\\java\\org\\example\\configuration\\keys.yml";
        // Path to "config.yml" configuration file
        private static final String configFilePath = System.getProperty("user.dir") + "\\src\\main\\java\\org\\example\\configuration\\config.yml";
        private static String defaultChannelId = "";
        private static String token = "";
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

            initialiseYoutubeInstance(apiKey);
        }

        public static void saveConfig(String value, int ch) throws FileNotFoundException {
            switch (ch) { // switch in case there will be new choices in the future
                case 0 -> defaultChannelId = value;
                default -> {
                    return;
                }
            }

            Map<String, String> data = new HashMap<>();
            data.put("default-channel-id", defaultChannelId);

            PrintWriter writer = new PrintWriter(configFilePath);
            Yaml yaml = new Yaml();
            yaml.dump(data, writer);
        }
    }
}
