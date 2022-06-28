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

public class UpdateWarpCommand<P extends GlobalWarpsPlugin<S>, S> extends GlobalCommand<P, S> {

    public UpdateWarpCommand(P plugin) {
        super(plugin, "gupdatewarp", "globalwarps.command.updatewarp", null, "Command to update the location of a warp", "/<command> <warp> [<server> <world> <x> <y> <z> [<yaw> [<pitch>]]", "updatewarp");
    }

    @Override
    public boolean onCommand(GlobalCommandSender<S> sender, LocationInfo location, String label, String[] args) {
        if (args.length < 1) {
            return false;
        }

        if (getPlugin().getWarpManager().getWarp(args[0]) == null) {
            getPlugin().sendLang(sender, "error.invalid-warp", "warp", args[0]);
            return true;
        }

        LocationInfo warpLocation;
        if (args.length > 5 && sender.hasPermission(getPermission() + ".location")) {
            if (!getPlugin().serverExists(args[1])) {
                getPlugin().sendLang(sender, "error.invalid-server", "server", args[0]);
                return true;
            }
            float yaw = 0;
            float pitch = 0;
            try {
                if (args.length > 6) {
                    yaw = Float.parseFloat(args[6]);
                    if (args.length > 7) {
                        pitch = Float.parseFloat(args[7]);
                    }
                }
                warpLocation = new LocationInfo(
                        args[1],
                        args[2],
                        Double.parseDouble(args[3]),
                        Double.parseDouble(args[4]),
                        Double.parseDouble(args[5]),
                        yaw,
                        pitch
                );
            } catch (NumberFormatException e) {
                getPlugin().sendLang(sender, "error.invalid-input", "error", e.getMessage());
                return true;
            }

        } else if (location == null) {
            getPlugin().sendLang(sender, "error.console-missing-arguments");
            return false;
        } else {
            warpLocation = location;
        }

        Warp warp = new Warp(args[0], warpLocation);

        getPlugin().getWarpManager().addWarp(warp);
        getPlugin().saveWarps();
        getPlugin().sendLang(sender, "warp-updated", "warp", warp.getName());

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
        } else if (args.length == 2 && sender.hasPermission(getPermission() + ".location")) {
            return getPlugin().getServers().stream()
                    .filter(s -> s.toLowerCase(Locale.ROOT).startsWith(args[0].toLowerCase(Locale.ROOT)))
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}
