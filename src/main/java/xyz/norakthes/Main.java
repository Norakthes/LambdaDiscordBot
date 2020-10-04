package xyz.norakthes;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.EventListener;

public class Main implements EventListener {
    public static String key;
    static {
        try {
            Object obj = new JSONParser().parse(new FileReader("key.json"));
            JSONObject jo = (JSONObject) obj;
            key = (String) jo.get("key");
        } catch (IOException | ParseException ignored) {

        }


    }

    public static void main(String[] args) throws Exception{


        //Authenticates the bot
        JDA jda = JDABuilder.createDefault(key).build();

        jda.addEventListener(new Ping());
        jda.addEventListener(new Reaction());
    }





}
class Ping extends ListenerAdapter {
    public void onMessageReceived(MessageReceivedEvent event) {
        switch (event.getMessage().getContentRaw()) {
            case "l!ping":
                event.getChannel().sendMessage("Pong! \uD83C\uDFD3").queue();
                break;
            case "bruh":
                if (!event.getAuthor().isBot()) {
                    event.getChannel().sendMessage("bruh").queue();

                }
                break;
        }
    }
}
class Reaction extends ListenerAdapter {
    static String messageId;
    public void onMessageReceived(MessageReceivedEvent event){
        String channelId = event.getChannel().getId();
        if (channelId.equals("761634429210722344")) {
            messageId = event.getMessageId();
        }
    }
    public void onMessageReactionAdd(MessageReactionAddEvent event) {
        if (event.getMessageId().equals(messageId)){
            String emoteId = event.getReactionEmote().getName();
            String userName = event.getUser().getName();
            TextChannel textChannel = event.getGuild().getTextChannelById("761629744356655125");
            User user = User.fromId("206872418428518403");
            textChannel.sendMessage(user.getAsMention() + " " + userName + " has reacted with the emote: " + emoteId).queue();
        }
    }
}