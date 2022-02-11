package net.fabricmc.shutoff;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.MinecraftServer;
import org.slf4j.Logger;

import org.slf4j.LoggerFactory;

import java.io.IOException;

public class ShutoffModRuntime implements ServerTickEvents.EndTick {

    public static final Logger LOGGER = LoggerFactory.getLogger("modid");
    int minSinceLastPlayer = 0;

    public void shutoff() throws IOException {
        try {
            LOGGER.info("Suspending Computer");
            String[] cmd = { "/bin/bash", "-c", "sudo /bin/systemctl suspend" };
            Process process = Runtime.getRuntime().exec(cmd);
            process.waitFor();
        } catch (InterruptedException e) {
            LOGGER.info(e.getMessage());
        }
    }

    @Override
    public void onEndTick(MinecraftServer server) {
        if(server.getCurrentPlayerCount() == 0) {
            minSinceLastPlayer++;
        }
        else
            minSinceLastPlayer = 0;

        if(minSinceLastPlayer >= 36000) {
            try {
                minSinceLastPlayer = 0;
                shutoff();
            } catch (IOException e) {}
        }
    }
}

