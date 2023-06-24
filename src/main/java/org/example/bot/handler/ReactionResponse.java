package org.example.bot.handler;

import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.emoji.UnicodeEmoji;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import org.example.bot.Bot;


public class ReactionResponse {
    /**
     * Handles emoji string to execute a specific action on adding an emoji to a WebHook
     *
     * @param message Message, message content instance
     * @param event MessageReactionAddEvent, event instance to access Emoji Unicode value etc.
     */
    public static void handleReaction(Message message, MessageReactionAddEvent event) {
        if (message.getEmbeds().size() == 0) // if message doesn't content Embeds:
            return;

        MessageEmbed embed = message.getEmbeds().get(0);
        UnicodeEmoji emoji = event.getReaction().getEmoji().asUnicode();

        switch (emoji.getFormatted()) {
//            case "\uD83C\uDDF5" ->  // P
//                Bot.sendMessage(message, "+play " + embed.getDescription());
            case "\uD83C\uDDFB" ->  // V
                    Bot.sendMessage(message, embed.getDescription());
            default -> {}
        }

    }

    /*
      An example of trying to connect to VC to run other bot. Unfortunately, it was not possible but I won't delete it
     */
//    public static void connectToVC(Message message, MessageReactionAddEvent event, String link) {
//        Member member = event.getMember();
//        assert member != null : "Member instance is null, cannot retrieve who sent the message";
//
//        VoiceChannel channel = null;
//        if (Objects.requireNonNull(member.getVoiceState()).getChannel() == null) {
//            Bot.sendMessage(message, "You need to connect to VC first.");
//            return;
//        }
//
//        channel = member.getVoiceState().getChannel().asVoiceChannel();
//        AudioManager audioManager = event.getGuild().getAudioManager();
//        audioManager.openAudioConnection(channel);
//
//        Bot.sendMessage(message, "+play " + link);
//
//        audioManager.closeAudioConnection();
//    }
}
