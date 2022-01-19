package btc.mocha.welshpricebot;

import btc.mocha.welshpricebot.listener.EventListener;
import net.dv8tion.jda.api.JDABuilder;


public class WelshPriceBot {
    private static final String BOT_TOKEN_ID = System.getenv("BOT_TOKEN_ID");

    public static void main(String[] arguments) throws Exception {
        if (BOT_TOKEN_ID != null) {
            JDABuilder
                    .createDefault(BOT_TOKEN_ID)
                    .addEventListeners(new EventListener())
                    .build();
        } else {
            throw new RuntimeException("BOT_TOKEN_ID must be in environment variable.");
        }
    }
}
