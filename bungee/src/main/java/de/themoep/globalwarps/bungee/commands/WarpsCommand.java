package de.themoep.globalwarps.bungee.commands;

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

import de.themoep.connectorplugin.BridgedCommand;
import de.themoep.connectorplugin.LocationInfo;
import de.themoep.globalwarps.Warp;
import de.themoep.globalwarps.bungee.GlobalWarps;
import net.md_5.bungee.api.CommandSender;

import java.util.List;
import java.util.stream.Collectors;

public class WarpsCommand extends BridgedCommand<GlobalWarps, CommandSender> {

    public WarpsCommand(GlobalWarps plugin) {
        super(plugin, "gwarps", "globalwarps.command.warps", null, "Warp command", "/<command> <warp> [<player>]", "warps", "globalwarps");
    }

    @Override
    public boolean onCommand(CommandSender sender, LocationInfo location, String label, String[] args) {
        if (args.length > 0 && "reload".equalsIgnoreCase(args[0]) && sender.hasPermission("globalwarps.command.reload")) {
            getPlugin().loadConfig();
            sender.sendMessage("Config reloaded!");
            return true;
        }
        List<Warp> warps = getPlugin().getWarpManager().getWarps().stream().sorted().collect(Collectors.toList());
        getPlugin().sendLang(sender, "warps.head", "count", String.valueOf(warps.size()));
        for (Warp warp : warps) {
            getPlugin().sendLang(sender, "warps.entry", warp.getReplacements());
        }
        getPlugin().sendLang(sender, "warps.footer", "count", String.valueOf(warps.size()));
        return true;
    }
}
