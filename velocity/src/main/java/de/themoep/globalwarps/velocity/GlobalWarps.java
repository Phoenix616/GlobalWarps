package de.themoep.globalwarps.velocity;

/*
 * ConnectorPlugin
 * Copyright (C) 2021 Max Lee aka Phoenix616 (max@themoep.de)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import de.themoep.connectorplugin.LocationInfo;
import de.themoep.connectorplugin.ProxyBridgeCommon;
import de.themoep.connectorplugin.velocity.VelocityConnectorPlugin;
import de.themoep.globalwarps.GlobalWarpsPlugin;
import de.themoep.globalwarps.Warp;
import de.themoep.globalwarps.WarpManager;
import de.themoep.globalwarps.commands.DelWarpCommand;
import de.themoep.globalwarps.commands.GlobalCommandSender;
import de.themoep.globalwarps.commands.SetWarpCommand;
import de.themoep.globalwarps.commands.UpdateWarpCommand;
import de.themoep.globalwarps.commands.WarpCommand;
import de.themoep.globalwarps.commands.WarpsCommand;
import de.themoep.minedown.adventure.MineDown;
import de.themoep.utils.lang.LangLogger;
import de.themoep.utils.lang.velocity.LanguageManager;
import de.themoep.utils.lang.velocity.Languaged;
import net.kyori.adventure.text.Component;
import ninja.leaping.configurate.ConfigurationNode;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Map;
import java.util.logging.Level;
import java.util.stream.Collectors;

public final class GlobalWarps implements GlobalWarpsPlugin<CommandSource>, Languaged {

    private final ProxyServer proxy;
    private final File dataFolder;
    private final Logger logger;
    private LanguageManager lang;
    private WarpManager warpManager;
    private VelocityConnectorPlugin connector;

    private PluginConfig config;
    private PluginConfig warpsConfig;

    @Inject
    public GlobalWarps(ProxyServer proxy, Logger logger, @DataDirectory Path dataFolder) {
        this.proxy = proxy;
        this.logger = logger;
        this.dataFolder = dataFolder.toFile();
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        connector = (VelocityConnectorPlugin) proxy.getPluginManager().getPlugin("connectorplugin").get().getInstance().get();

        connector.getBridge().registerServerCommand(new WarpsCommand<>(this));
        connector.getBridge().registerServerCommand(new WarpCommand<>(this));
        connector.getBridge().registerServerCommand(new SetWarpCommand<>(this));
        connector.getBridge().registerServerCommand(new DelWarpCommand<>(this));
        connector.getBridge().registerServerCommand(new UpdateWarpCommand<>(this));

        loadConfig();
    }

    public void loadConfig() {
        config = new PluginConfig(this, new File(dataFolder, "config.yml"), "velocity-config.yml");
        try {
            config.createDefaultConfig();
        } catch (IOException e) {
            logger.error("Could not created default config! " + e.getMessage());
            return;
        }
        if (!config.load()) {
            return;
        }

        lang = new LanguageManager(this, getConfig().getString("default-language"));

        warpManager = new WarpManager();

        try {
            warpsConfig = new PluginConfig(this, new File(dataFolder, "warps.yml"));
            ConfigurationNode warpsSection = warpsConfig.getRawConfig("warps");
            if (warpsSection.isMap()) {
                for (Map.Entry<Object, ? extends ConfigurationNode> warpEntry : warpsSection.getChildrenMap().entrySet()) {
                    ConfigurationNode warpSection = warpEntry.getValue();
                    if (warpsSection.isMap()) {
                        Map<Object, ? extends ConfigurationNode> warpConfig = warpSection.getChildrenMap();
                        Warp warp = new Warp((String) warpEntry.getKey(), new LocationInfo(
                                warpConfig.get("server").getString(),
                                warpConfig.get("world").getString(),
                                warpConfig.get("x").getDouble(),
                                warpConfig.get("y").getDouble(),
                                warpConfig.get("z").getDouble(),
                                warpConfig.get("yaw").getFloat(),
                                warpConfig.get("pitch").getFloat()
                        ));

                        warpManager.addWarp(warp);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private PluginConfig getConfig() {
        return config;
    }

    @Override
    public boolean serverExists(String serverName) {
        return proxy.getServer(serverName).isPresent();
    }

    @Override
    public void saveWarps() {
        for (Warp warp : getWarpManager().getWarps()) {
            warpsConfig.set("warps." + warp.getName() + ".server", warp.getServer());
            warpsConfig.set("warps." + warp.getName() + ".world", warp.getWorld());
            warpsConfig.set("warps." + warp.getName() + ".x", warp.getX());
            warpsConfig.set("warps." + warp.getName() + ".y", warp.getY());
            warpsConfig.set("warps." + warp.getName() + ".z", warp.getZ());
            warpsConfig.set("warps." + warp.getName() + ".yaw", warp.getYaw());
            warpsConfig.set("warps." + warp.getName() + ".pitch", warp.getPitch());
        }
        warpsConfig.save();
    }

    @Override
    public void removeWarp(String warpName) {
        warpsConfig.remove("warps." + warpName);
        warpsConfig.save();
    }

    public Component getLang(CommandSource sender, String key, String... replacements) {
        return MineDown.parse(lang.getConfig(sender).get(key), replacements);
    }

    public void sendLang(GlobalCommandSender<CommandSource> sender, String key, String... replacements) {
        sender.getSender().sendMessage(getLang(sender.getSender(), key, replacements));
    }

    @Override
    public Collection<String> getServers() {
        return proxy.getAllServers().stream().map(s -> s.getServerInfo().getName()).collect(Collectors.toList());
    }

    @Override
    public Collection<String> getOnlinePlayerNames() {
        return proxy.getAllPlayers().stream().map(Player::getUsername).collect(Collectors.toSet());
    }

    @Override
    public GlobalCommandSender<CommandSource> getPlayer(String playerName) {
        return proxy.getPlayer(playerName).map(this::getSender).orElse(null);
    }

    @Override
    public GlobalCommandSender<CommandSource> getSender(CommandSource sender) {
        return new VelocityCommandSender(sender);
    }

    @Override
    public ProxyBridgeCommon<VelocityConnectorPlugin, Player> getBridge() {
        return connector.getBridge();
    }

    @Override
    public String getName() {
        return "GlobalWarps";
    }

    @Override
    public WarpManager getWarpManager() {
        return warpManager;
    }

    @Override
    public File getDataFolder() {
        return dataFolder;
    }

    @Override
    public LangLogger getLangLogger() {
        return new LangLogger() {
            @Override
            public void log(Level level, String message) {
                log(level, message, null);
            }

            @Override
            public void log(Level level, String message, Throwable throwable) {
                if (level.intValue() < Level.FINER.intValue()) {
                    logger.trace(message, throwable);
                } else if (level.intValue() < Level.INFO.intValue()) {
                    logger.debug(message, throwable);
                } else if (level.intValue() < Level.WARNING.intValue()) {
                    logger.info(message, throwable);
                } else if (level.intValue() < Level.SEVERE.intValue()) {
                    logger.warn(message, throwable);
                } else {
                    logger.error(message, throwable);
                }
            }
        };
    }

    public InputStream getResourceAsStream(String file) {
        return getClass().getClassLoader().getResourceAsStream(file);
    }

    public Logger getLogger() {
        return logger;
    }
}
