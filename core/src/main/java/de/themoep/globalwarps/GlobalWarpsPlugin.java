package de.themoep.globalwarps;

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

import de.themoep.connectorplugin.ProxyBridgeCommon;
import de.themoep.connectorplugin.connector.ConnectingPlugin;
import de.themoep.globalwarps.commands.GlobalCommandSender;

import java.util.Collection;

public interface GlobalWarpsPlugin<S> extends ConnectingPlugin {

    void saveWarps();

    void removeWarp(String warpName);

    WarpManager getWarpManager();

    void sendLang(GlobalCommandSender<S> sender, String key, String... replacements);

    Collection<String> getServers();

    Collection<String> getOnlinePlayerNames();

    GlobalCommandSender<S> getPlayer(String playerName);

    GlobalCommandSender<S> getSender(S sender);

    ProxyBridgeCommon<?, ?> getBridge();

    void loadConfig();

    boolean serverExists(String serverName);
}
