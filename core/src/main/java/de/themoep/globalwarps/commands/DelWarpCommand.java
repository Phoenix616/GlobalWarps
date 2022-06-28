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

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class DelWarpCommand<P extends GlobalWarpsPlugin<S>, S> extends GlobalCommand<P, S> {

    public DelWarpCommand(P plugin) {
        super(plugin, "gdelwarp", "globalwarps.command.delwarp", null, "Command to remove a warp", "/<command> <warp>", "delwarp", "gdeletewarp", "deletewarp", "gremovewarp", "removewarp");
    }

    @Override
    public boolean onCommand(GlobalCommandSender<S> sender, LocationInfo location, String label, String[] args) {
        if (args.length < 1) {
            return false;
        }

        Warp warp = getPlugin().getWarpManager().removeWarp(args[0]);
        if (warp == null) {
            getPlugin().sendLang(sender, "error.invalid-warp", "warp", args[0]);
            return true;
        }
        getPlugin().removeWarp(warp.getName());
        getPlugin().sendLang(sender, "warp-removed", "warp", warp.getName());
        return true;
    }

    @Override
    public List<String> onTabComplete(GlobalCommandSender<S> sender, String label, String[] args) {
        if (args.length == 0) {
            return getPlugin().getWarpManager().getWarps().stream().map(Warp::getName).collect(Collectors.toList());
        } else if (args.length == 1) {
            return getPlugin().getWarpManager().getWarps().stream()
                    .map(Warp::getName)
                    .filter(s -> s.toLowerCase(Locale.ROOT).startsWith(args[0].toLowerCase(Locale.ROOT)))
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}
