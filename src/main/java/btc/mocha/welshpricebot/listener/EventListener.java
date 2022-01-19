package btc.mocha.welshpricebot.listener;

import btc.mocha.welshpricebot.dto.HappyWelshDto;
import btc.mocha.welshpricebot.service.WelshPriceBotService;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Channel;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class EventListener extends ListenerAdapter {
    private static final Long PRICE_CHANNEL_ID = Long.parseLong(System.getenv("PRICE_CHANNEL_ID"));
    private static final Long NFT_CHANNEL_ID = Long.parseLong(System.getenv("NFT_CHANNEL_ID"));

    private final WelshPriceBotService service;

    public EventListener() {
        this.service = new WelshPriceBotService();
    }

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

        Runnable task = () -> {
            BigDecimal latestWelshPerStx = service.getLatestWelshPerStx();
            BigDecimal welshInUsd = service.getWelshInUsd(latestWelshPerStx);

            String status = "$" + welshInUsd.toPlainString();

            event.getJDA().getPresence()
                    .setPresence(OnlineStatus.ONLINE, Activity.watching(status));
        };

        executor.scheduleWithFixedDelay(task, 0, 15, TimeUnit.MINUTES);
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        Long channelId = event.getChannel().getIdLong();
        String command = event.getMessage().getContentRaw();

        if ("!price".equals(command)) {
            if (channelId.equals(PRICE_CHANNEL_ID)) {
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
            } else {
                sendWrongChannelMessage(event, PRICE_CHANNEL_ID);
            }
        } else if ("!nft".equals(command)) {
            if (channelId.equals(NFT_CHANNEL_ID)) {
                event.getChannel().sendTyping().queue();
                HappyWelshDto dto = service.getHappyWelshNFTData();

                // Start building message here...
                EmbedBuilder eb = new EmbedBuilder();

                eb.setDescription(
                        "Get your own Happy Welsh! You can find Happy Welsh NFTs at \n" +
                                "- [Stacks Art](https://www.stacksart.com/collections/happy-welsh/market)\n" +
                                "- [stxnft](https://stxnft.com/collections/SPJW1XE278YMCEYMXB8ZFGJMH8ZVAAEDP2S2PJYG.happy-welsh)"
                );

                eb.setColor(new Color(224, 133, 57));
                eb.addField("Total items", dto.getNumberOfItems().toString(), false);
                eb.addField("Owners", dto.getNumberOfOwners().toString(), false);
                eb.addField("Floor price", dto.getFloorPrice().toPlainString() + " STX", false);
                eb.addField("All-time volume traded", dto.getAllTimeVolumeTraded().toPlainString() + " STX", false);

                eb.setFooter("This information is fetched from Stacks Art, and information may slightly vary from site to site.");
                // end

                event.getChannel().sendMessage(" ").setEmbeds(eb.build())
                        .queue();
            } else {
                sendWrongChannelMessage(event, NFT_CHANNEL_ID);
            }
        }
    }

    private void sendWrongChannelMessage(@NotNull MessageReceivedEvent event, Long priceChannelId) {
        Channel priceChannel = event.getGuild().getTextChannelById(priceChannelId);
        if (priceChannel != null) {
            String mention = priceChannel.getAsMention();

            event.getChannel().sendMessage("You cannot use this command at this channel. Try in " + mention).queue();

        } else {
            System.err.println("Channel with id: " + NFT_CHANNEL_ID + " does not exist on guild.");

            event.getChannel().sendMessage("You cannot use this command at this channel. Try in correct channel.")
                    .queue();
        }
    }
}
