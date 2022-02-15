package net.fabricmc.shutoff;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.MinecraftServer;
import org.slf4j.Logger;

import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Locale;

public class ShutoffModRuntime implements ServerTickEvents.EndTick {

    public static final Logger LOGGER = LoggerFactory.getLogger("modid");
    int minSinceLastPlayer = 0;
    int Wait;

    public ShutoffModRuntime(int Wait) {
        this.Wait = Wait*20*60; //convert minutes to ticks
    }

    public void shutoff() throws IOException {
        try {
            LOGGER.info("Suspending Computer");
            String OS = System.getProperty("os.name", "generic").toLowerCase(Locale.ENGLISH);
            if(OS.contains("win")) {
                String[] cmd = {"Rundll32.exe powrprof.dll,SetSuspendState Sleep"};
                Process process = Runtime.getRuntime().exec(cmd);
                process.waitFor();
            }
            else if(OS.contains("mac")) {
                String[] cmd = {"osascript -e 'tell application \"System Events\" to sleep'"};
            }
            else {
                String[] cmd = {"/bin/bash", "-c", "sudo /sbin/pm-suspend --quirk-none"};
                Process process = Runtime.getRuntime().exec(cmd);
                process.waitFor();
            }
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

        if(minSinceLastPlayer >= Wait) {
            try {
                minSinceLastPlayer = 0;
                shutoff();
            } catch (IOException e) { LOGGER.info(e.getMessage()); }
        }
    }
}

