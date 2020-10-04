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

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.EventListener;

public class Main implements EventListener {
    public static String key;
    static {
        try {
            Object obj = new JSONParser().parse(new FileReader(new File("./src/main/resources/key.json")));
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
    private static String messageId;
    static {
        try {
            JSONObject MessageIdJson = (JSONObject) new JSONParser().parse(new FileReader(new File("./src/main/resources/messageId.json")));
            messageId = (String) MessageIdJson.get("id");
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }


    public void onMessageReceived(MessageReceivedEvent event){

        try {
            String channelId = event.getChannel().getId();
            if (channelId.equals("761634429210722344")) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("id", messageId);
                Files.write(Paths.get("./src/main/resources/messageId.json"), jsonObj.toJSONString().getBytes());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void onMessageReactionAdd(MessageReactionAddEvent event) {
        try {
            JSONParser parser = new JSONParser();
            Object obj = parser.parse()
            String emote = event.getReactionEmote().getName();
            if (event.getMessageId().equals(messageId) /*&& emote.equals("YEP")*/) {
                String userId = event.getUserId();
                String userName = event.getUser().getName();
                TextChannel textChannel = event.getGuild().getTextChannelById("761629744356655125");
                User user = User.fromId("206872418428518403");
                textChannel.sendMessage(user.getAsMention() + " " + userName + " has reacted with the emote: " + emote).queue();


                Files.write(Paths.get("./src/main/resources/list.json"), jsonObj.toJSONString().getBytes());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}