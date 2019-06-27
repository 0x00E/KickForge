package com.github.qianniancc.kickforge;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import net.md_5.bungee.event.EventHandler;

public class Main extends Plugin implements Listener {

	public Configuration conf;

	public void onLoad() {
		if (!getDataFolder().exists())
			getDataFolder().mkdir();

		File file = new File(getDataFolder(), "config.yml");

		if (!file.exists()) {
			try (InputStream in = getResourceAsStream("config.yml")) {
				Files.copy(in, file.toPath());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		try {
			conf = ConfigurationProvider.getProvider(YamlConfiguration.class)
					.load(new File(getDataFolder(), "config.yml"));
		} catch (IOException e1) {
			getLogger().info(e1.getMessage());
		}

		getLogger().info("KickForge Done!");

		getProxy().getPluginManager().registerListener(this, this);
	}

	@EventHandler
	public void onConnect(ServerConnectedEvent e) {
		ProxiedPlayer p = e.getPlayer();
		if (conf.getBoolean("KickForge") == true) {
			if (p.isForgeUser() == true) {
				p.disconnect(new TextComponent(conf.getString("KickForgeMessage")));
			}
		}
		if (conf.getBoolean("KickDef") == true) {
			if (p.isForgeUser() == false) {
				p.disconnect(new TextComponent(conf.getString("KickDefMessage")));
			}
		}

	}
}