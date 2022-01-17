package btc.mocha.welshpricebot.listener;

import btc.mocha.welshpricebot.service.WelshPriceBotService;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class EventListener extends ListenerAdapter {
    private final WelshPriceBotService service;

    public EventListener() {
        this.service = new WelshPriceBotService();
    }

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        BigDecimal latestWelshPerStx = service.getLatestWelshPerStx();
        BigDecimal welshInUsd = service.getWelshInUsd(latestWelshPerStx);

        String status = "$" + welshInUsd.toPlainString();
        event.getJDA().getPresence()
                .setPresence(OnlineStatus.ONLINE, Activity.watching(status));
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if ("!price".equals(event.getMessage().getContentRaw())) {
            BigDecimal latestWelshPerStx = service.getLatestWelshPerStx().setScale(3, RoundingMode.HALF_UP);
            BigDecimal latestStxPerWelsh = BigDecimal.ONE.divide(latestWelshPerStx, 7, RoundingMode.HALF_UP);
            BigDecimal welshInUsd = service.getWelshInUsd(latestWelshPerStx);

            // Start building message here...
            EmbedBuilder eb = new EmbedBuilder();

            eb.setColor(new Color(224, 133, 57));
            eb.addField("1 $WELSH",
                    "$" + welshInUsd.toPlainString() + "\n" +
                    latestStxPerWelsh.toPlainString() + " $STX" + "\n",
                    false);

            eb.addField(
                    "You can buy with 1 $STX:",
                    latestWelshPerStx.toPlainString() + " $WELSH",
                    false
            );

            eb.setDescription("You check realtime price and swap at [Arkadiko](https://app.arkadiko.finance/)!");
            // end

            event.getChannel().sendMessage(" ").setEmbeds(eb.build())
                    .queue();
        }
    }
}
