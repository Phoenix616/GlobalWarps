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

public class DelWarpCommand extends BridgedCommand<GlobalWarps, CommandSender> {

    public DelWarpCommand(GlobalWarps plugin) {
        super(plugin, "delwarp", "globalwarps.command.delwarp", null, "Command to remove a warp", "/<command> <warp>", "gdelwarp", "removewarp", "gremovewarp");
    }

    @Override
    public boolean onCommand(CommandSender sender, LocationInfo location, String label, String[] args) {
        if (args.length < 1) {
            return false;
        }

        Warp warp = getPlugin().getWarpManager().removeWarp(args[0]);
        if (warp == null) {
            getPlugin().sendLang(sender, "error.invalid-warp", "warp", args[0]);
            return true;
        }
        getPlugin().saveWarps();
        getPlugin().sendLang(sender, "warp-removed", "warp", warp.getName());
        return true;
    }
}
