package de.themoep.globalwarps.bungee;

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

import de.themoep.bungeeplugin.BungeePlugin;
import de.themoep.bungeeplugin.FileConfiguration;
import de.themoep.connectorplugin.ConnectorPlugin;
import de.themoep.connectorplugin.LocationInfo;
import de.themoep.connectorplugin.bungee.BungeeConnectorPlugin;
import de.themoep.globalwarps.GlobalWarpsPlugin;
import de.themoep.globalwarps.Warp;
import de.themoep.globalwarps.WarpManager;
import de.themoep.globalwarps.bungee.commands.DelWarpCommand;
import de.themoep.globalwarps.bungee.commands.SetWarpCommand;
import de.themoep.globalwarps.bungee.commands.UpdateWarpCommand;
import de.themoep.globalwarps.bungee.commands.WarpCommand;
import de.themoep.globalwarps.bungee.commands.WarpsCommand;
import de.themoep.minedown.MineDown;
import de.themoep.utils.lang.bungee.LanguageManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.config.Configuration;

import java.io.IOException;

public final class GlobalWarps extends BungeePlugin implements GlobalWarpsPlugin {

    private LanguageManager lang;
    private WarpManager warpManager;
    private BungeeConnectorPlugin connector;

    private FileConfiguration warpsConfig;

    @Override
    public void onEnable() {
        connector = (BungeeConnectorPlugin) getProxy().getPluginManager().getPlugin("ConnectorPlugin");

        connector.getBridge().registerServerCommand(new WarpsCommand(this));
        connector.getBridge().registerServerCommand(new WarpCommand(this));
        connector.getBridge().registerServerCommand(new SetWarpCommand(this));
        connector.getBridge().registerServerCommand(new DelWarpCommand(this));
        connector.getBridge().registerServerCommand(new UpdateWarpCommand(this));

        loadConfig();
    }

    private void loadConfig() {
        try {
            getConfig().loadConfig();
        } catch (IOException e) {
            e.printStackTrace();
        }

        lang = new LanguageManager(this, getConfig().getString("default-language"));

        warpManager = new WarpManager();

        try {
            warpsConfig = new FileConfiguration(this, "warps.yml");
            Configuration warpsSection = warpsConfig.getSection("warps");
            for (String warpName : warpsSection.getKeys()) {
                Configuration warpSection = warpsSection.getSection(warpName);
                Warp warp = new Warp(warpName, new LocationInfo(
                    warpSection.getString("server"),
                    warpSection.getString("world"),
                    warpSection.getDouble("x"),
                    warpSection.getDouble("y"),
                    warpSection.getDouble("z"),
                    warpSection.getFloat("yaw"),
                    warpSection.getFloat("pitch")
                ));

                warpManager.addWarp(warp);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveWarps() {
        Configuration warpsSection = warpsConfig.getSection("warps");
        for (Warp warp : getWarpManager().getWarps()) {
            Configuration warpSection = warpsSection.getSection(warp.getName());
            warpSection.set("server", warp.getServer());
            warpSection.set("world", warp.getWorld());
            warpSection.set("x", warp.getX());
            warpSection.set("y", warp.getY());
            warpSection.set("z", warp.getZ());
            warpSection.set("yaw", warp.getYaw());
            warpSection.set("pitch", warp.getPitch());
        }
        warpsConfig.saveConfig();
    }

    public BaseComponent[] getLang(CommandSender sender, String key, String... replacements) {
        return MineDown.parse(lang.getConfig(sender).get(key), replacements);
    }

    public void sendLang(CommandSender sender, String key, String... replacements) {
        sender.sendMessage(getLang(sender, key, replacements));
    }

    @Override
    public BungeeConnectorPlugin getConnector() {
        return connector;
    }

    @Override
    public String getName() {
        return getDescription().getName();
    }

    @Override
    public WarpManager getWarpManager() {
        return warpManager;
    }
}
