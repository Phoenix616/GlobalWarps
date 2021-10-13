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

public class UpdateWarpCommand extends BridgedCommand<GlobalWarps, CommandSender> {

    public UpdateWarpCommand(GlobalWarps plugin) {
        super(plugin, "updatewarp", "globalwarps.command.updatewarp", null, "Command to update the location of a warp", "/<command> <warp> [<server> <world> <x> <y> <z> [<yaw> [<pitch>]]", "gupdatewarp");
    }

    @Override
    public boolean onCommand(CommandSender sender, LocationInfo location, String label, String[] args) {
        if (args.length < 1) {
            return false;
        }

        if (getPlugin().getWarpManager().getWarp(args[0]) == null) {
            getPlugin().sendLang(sender, "error.invalid-warp", "warp", args[0]);
            return true;
        }

        LocationInfo warpLocation;
        if (args.length > 5) {
            if (getPlugin().getProxy().getServerInfo(args[1]) == null) {
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
        getPlugin().sendLang(sender, "warp-added", "warp", warp.getName());

        return true;
    }
}
