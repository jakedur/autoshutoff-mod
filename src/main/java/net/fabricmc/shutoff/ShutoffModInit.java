package net.fabricmc.shutoff;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.server.ServerTickCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Locale;

public class ShutoffModInit implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger("modid");

	// Load config 'shutoff.properties', if it isn't present create one
	SimpleConfig CONFIG = SimpleConfig.of( "shutoff" ).provider( this::provider ).request();

	// Custom config provider, returns the default config content
	// if the custom provider is not specified SimpleConfig will create an empty file instead
	private String provider( String filename ) {
		return "Wait-Time-Minutes=30";
	}

	public final int Wait = CONFIG.getOrDefault( "Wait-Time-Minutes", 30 );


	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		ServerTickEvents.END_SERVER_TICK.register(new ShutoffModRuntime(Wait));
		
		LOGGER.info("Loaded Shutoff Mod");
	}


}
