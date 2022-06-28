package de.themoep.globalwarps.commands;

/*
 * GlobalWarps
 * Copyright (C) 2022 Max Lee aka Phoenix616 (max@themoep.de)
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

import de.themoep.connectorplugin.LocationInfo;
import de.themoep.globalwarps.GlobalWarpsPlugin;
import de.themoep.globalwarps.Warp;

import java.util.List;
import java.util.stream.Collectors;

public class WarpsCommand<P extends GlobalWarpsPlugin<S>, S> extends GlobalCommand<P, S> {

    public WarpsCommand(P plugin) {
        super(plugin, "gwarps", "globalwarps.command.warps", null, "Warp command", "/<command> <warp> [<player>]", "warps", "globalwarps");
    }

    @Override
    public boolean onCommand(GlobalCommandSender<S> sender, LocationInfo location, String label, String[] args) {
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
