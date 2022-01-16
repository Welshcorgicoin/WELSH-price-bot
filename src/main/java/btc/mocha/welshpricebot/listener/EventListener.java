package btc.mocha.welshpricebot.listener;

import btc.mocha.welshpricebot.service.WelshPriceBotService;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class EventListener extends ListenerAdapter {
    private WelshPriceBotService service;

    public EventListener() {
        this.service = new WelshPriceBotService();
    }

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        String status = "$" + service.getWelshInUsd().toPlainString();
        event.getJDA().getPresence()
                .setPresence(OnlineStatus.ONLINE, Activity.watching(status));
    }
}
