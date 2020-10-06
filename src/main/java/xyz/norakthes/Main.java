package xyz.norakthes;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.json.simple.JSONArray;
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

        //Events
        jda.addEventListener(new Ping());
        jda.addEventListener(new Reaction());

        //Activity
        jda.getPresence().setActivity(Activity.playing("shit bot lol die"));
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
                    if (event.getMember().getId().equals("196283420853272576")) {
                        event.getChannel().sendMessage("Bruh moment").queue();
                    }
                    else {
                        event.getChannel().sendMessage("bruh").queue();
                    }


                }
                break;
        }
    }
}

class Reaction extends ListenerAdapter {
    protected static boolean announce = false;
    protected static String messageId;
    static {
        try {
            JSONObject messageIdJson = (JSONObject) new JSONParser().parse(new FileReader(new File("./src/main/resources/messageId.json")));
            messageId = (String) messageIdJson.get("id");
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }


    public void onMessageReceived(MessageReceivedEvent event){
        try {
            String channelId = event.getChannel().getId();
            if (channelId.equals("761634429210722344") && announce) {
                messageId = event.getMessageId();
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("id", messageId);
                Files.write(Paths.get("./src/main/resources/messageId.json"), jsonObj.toJSONString().getBytes());
                event.getMessage().addReaction("\uD83D\uDC4D").queue();
                announce = false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        switch (event.getMessage().getContentRaw()) {
            case "l!userlist":
                try {
                    JSONObject jsonObj = (JSONObject) new JSONParser().parse(new FileReader(new File("./src/main/resources/list.json")));
                    JSONArray jsonArray = (JSONArray) jsonObj.get("username");

                    String[] userList = new String[jsonArray.size()];
                    MessageBuilder message = new MessageBuilder();
                    message.append("Attendee list:\n");
                    for (int i = 1; i<=jsonArray.size(); i++) {
                        userList[i] = jsonArray.get(i).toString();
                        if (i == jsonArray.size()) {
                            message.append(userList[i]);
                        }
                        else {
                            message.append(userList[i] + ", ");
                        }
                    }
                    event.getChannel().sendMessage(message.build()).queue();

                } catch (IOException | ParseException e) {
                    e.printStackTrace();
                }
                break;
            case "l!announce":
                if (event.getMember().hasPermission(Permission.MESSAGE_MENTION_EVERYONE)){
                    TextChannel textChannel = event.getGuild().getTextChannelById("761634429210722344");
                    event.getChannel().sendMessage(event.getMember().getAsMention() + " write your announcement in " + textChannel.getAsMention()).queue();
                    announce = true;
                    break;
                }
            case "l!test":
                event.getChannel().sendMessage(Boolean.toString(announce)).queue();
                break;
        }
    }

    public void onMessageReactionAdd(MessageReactionAddEvent event) {
        if (!event.getUser().isBot()) {
            try {
                JSONObject jsonObj = (JSONObject) new JSONParser().parse(new FileReader(new File("./src/main/resources/list.json")));
                JSONArray jsonId = (JSONArray) jsonObj.get("userId");
                JSONArray jsonName = (JSONArray) jsonObj.get("username");
                String emote = event.getReactionEmote().getName();
                if (event.getMessageId().equals(messageId) && emote.equals("\uD83D\uDC4D") && !event.getUser().isBot()) {
                    String userId = event.getUserId();
                    String username = event.getMember().getEffectiveName();
                    if (event.getMember().getNickname() != null) {
                        username = event.getMember().getNickname();
                    }
                    jsonId.add(userId);
                    jsonName.add(username);

                    jsonObj.put("userId", jsonId);
                    jsonObj.put("username", jsonName);

                    Files.write(Paths.get("./src/main/resources/list.json"), jsonObj.toJSONString().getBytes());
                }
            } catch (IOException | ParseException e) {
                e.printStackTrace();
            }
        }
    }

    public void onMessageReactionRemove(MessageReactionRemoveEvent event) {
        try {
            JSONObject jsonObj = (JSONObject) new JSONParser().parse(new FileReader(new File("./src/main/resources/list.json")));
            JSONArray jsonId = (JSONArray) jsonObj.get("userId");
            JSONArray jsonName = (JSONArray) jsonObj.get("username");
            String emote = event.getReactionEmote().getName();
            if (event.getMessageId().equals(messageId) /*&& emote.equals("YEP")*/ && !event.getUser().isBot()) {
                String userId = event.getUserId();
                String username = event.getMember().getEffectiveName();
                if (event.getMember().getNickname() != null){
                    username = event.getMember().getNickname();
                }
//                TextChannel textChannel = event.getGuild().getTextChannelById("761629744356655125");
//                User user = User.fromId("206872418428518403");
//                textChannel.sendMessage(user.getAsMention() + " " + userName + " has reacted with the emote: " + emote).queue();
                jsonId.remove(userId);
                jsonName.remove(username);

                jsonObj.put("userId",jsonId);
                jsonObj.put("username",jsonName);
                System.out.println(jsonId + " " + jsonName);

                Files.write(Paths.get("./src/main/resources/list.json"), jsonObj.toJSONString().getBytes());
            }
        } catch (IOException | ParseException e){
            e.printStackTrace();
        }
    }
}