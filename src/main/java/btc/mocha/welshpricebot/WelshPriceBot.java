package btc.mocha.welshpricebot;

import btc.mocha.welshpricebot.listener.EventListener;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDABuilder;


@Slf4j
public class WelshPriceBot {
    public static void main(String[] arguments) throws Exception {
        String botToken;
        try {
            botToken = arguments[0];
        } catch (IndexOutOfBoundsException e) {
            log.error("Argument 0 does not exist.");

            throw new RuntimeException("You should provide your Discord bot token on argument 0.");
        }

        JDABuilder
                .createDefault(botToken)
                .addEventListeners(new EventListener())
                .build();
    }
}
