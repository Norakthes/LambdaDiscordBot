package xyz.norakthes;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
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
    }





}
class Ping extends ListenerAdapter {
    public  void onMessageReceived(MessageReceivedEvent event){
        switch (event.getMessage().getContentRaw()){
            case "l!ping":
                event.getChannel().sendMessage("Pong! \uD83C\uDFD3").queue();
                break;
            case "bruh":
                if (!event.getAuthor().isBot()){
                    event.getChannel().sendMessage("bruh").queue();

                }
                break;

        }
    }
}