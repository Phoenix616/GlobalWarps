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

import de.themoep.connectorplugin.LocationInfo;

public class Warp extends LocationInfo implements Comparable<Warp> {
    private final String name;

    public Warp(String name, LocationInfo location) {
        super(location);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public int compareTo(Warp otherWarp) {
        return name.compareToIgnoreCase(otherWarp.getName());
    }

    public String[] getReplacements() {
        return new String[] {
                "server", getServer(),
                "world", getWorld(),
                "x", String.valueOf(Math.round(getX())),
                "y", String.valueOf(Math.round(getY())),
                "z", String.valueOf(Math.round(getZ())),
                "yaw", String.valueOf(Math.round(getYaw())),
                "pitch", String.valueOf(Math.round(getPitch()))
        };
    }
}
