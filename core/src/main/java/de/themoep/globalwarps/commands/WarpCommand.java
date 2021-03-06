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

public class WarpCommand<P extends GlobalWarpsPlugin<S>, S> extends GlobalCommand<P, S> {

    public WarpCommand(P plugin) {
        super(plugin, "gwarp", "globalwarps.command.warp", null, "Warp command", "/<command> <warp> [<player>]", "warp");
    }

    @Override
    public boolean onCommand(GlobalCommandSender<S> sender, LocationInfo location, String label, String[] args) {
        if (args.length < 1) {
            if (!sender.hasPermission("globalwarps.command.warps")) {
                return false;
            }
            List<Warp> warps = getPlugin().getWarpManager().getWarps().stream().sorted().collect(Collectors.toList());
            getPlugin().sendLang(sender, "warps.head", "count", String.valueOf(warps.size()));
            for (Warp warp : warps) {
                getPlugin().sendLang(sender, "warps.entry", warp.getReplacements());
            }
            getPlugin().sendLang(sender, "warps.footer", "count", String.valueOf(warps.size()));
            return true;
        }

        Warp warp = getPlugin().getWarpManager().getWarp(args[0]);
        if (warp == null) {
            getPlugin().sendLang(sender, "error.invalid-warp", "warp", args[0]);
            return true;
        }

        GlobalCommandSender<S> target;
        if (args.length > 1 && sender.hasPermission(getPermission() + ".others")) {
            target = getPlugin().getPlayer(args[1]);
            if (target == null) {
                getPlugin().sendLang(sender, "error.player-not-found", "player", args[1]);
                return true;
            }
        } else if (sender.isPlayer()) {
            target = sender;
        } else {
            getPlugin().sendLang(sender, "error.console-missing-arguments");
            return false;
        }

        getPlugin().getBridge().teleport(target.getName(), warp, s -> {
            if (target != sender) {
                getPlugin().sendLang(sender, "reply", "value", s);
            }
        }).thenAccept(success -> {
            if (success) {
                getPlugin().sendLang(sender, "warped", "warp", warp.getName());
            } else {
                getPlugin().sendLang(sender, "error.warping", "warp", warp.getName());
            }
        });
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
        } else if (args.length == 2 && sender.hasPermission(getPermission() + ".others")) {
            return getPlugin().getOnlinePlayerNames().stream()
                    .filter(s -> s.toLowerCase(Locale.ROOT).startsWith(args[0].toLowerCase(Locale.ROOT)))
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}
