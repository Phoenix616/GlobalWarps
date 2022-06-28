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

import de.themoep.connectorplugin.BridgedCommand;
import de.themoep.connectorplugin.BridgedSuggestions;
import de.themoep.connectorplugin.LocationInfo;
import de.themoep.globalwarps.GlobalWarpsPlugin;

import java.util.Collections;
import java.util.List;

public abstract class GlobalCommand<P extends GlobalWarpsPlugin<S>, S> extends BridgedCommand<P, S> implements BridgedSuggestions<S> {

    public GlobalCommand(P plugin, String name, String permission, String permissionMessage, String description, String usage, String... aliases) {
        super(plugin, name, permission, permissionMessage, description, usage, aliases);
    }

    @Override
    public boolean onCommand(S sender, LocationInfo location, String label, String[] args) {
        return onCommand(getPlugin().getSender(sender), location, label, args);
    }

    public List<String> suggest(S sender, String label, String[] args) {
        return onTabComplete(getPlugin().getSender(sender), label, args);
    }

    public abstract boolean onCommand(GlobalCommandSender<S> sender, LocationInfo location, String label, String[] args);

    public List<String> onTabComplete(GlobalCommandSender<S> sender, String label, String[] args) {
        return Collections.emptyList();
    }
}
