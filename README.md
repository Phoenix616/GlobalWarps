# GlobalWarps

Simple global network warp plugin depending on [ConnectorPlugin](https://github.com/Phoenix616/ConnectorPlugin).

## Commands

> `/warps`  
> *Permission:* `globalwarps.command.warps`  
> *Aliases:* `gwarps`, `listwarps`, `glistwarps`, `warplist`, `gwarplist`

> `/warp <warp> [<player>]`  
> *Permission:* `globalwarps.command.warp`, `globalwarps.command.warp.others`    
> *Aliases:* `gwarp` 

> `/setwarp <warp> [<server> <world> <x> <y> <z> [<yaw> [<pitch>]]`  
> *Permission:* `globalwarps.command.setwarp`, `globalwarps.command.setwarp.location`  
> *Aliases:* `gsetwarp`, `createwarp`, `gcreatewarp`, `addwarp`, `gaddwarp`
 
> `/updatewarp <warp> [<server> <world> <x> <y> <z> [<yaw> [<pitch>]]`  
> *Permission:* `globalwarps.command.updatewarp`, `globalwarps.command.updatewarp.location`  
> *Aliases:* `gupdatewarp`

> `/delwarp <warp>`  
> *Permission:* `globalwarps.command.delwarp`  
> *Aliases:* `gdelwarp`, `removewarp`, `gremovewarp`

Permissions for tab completion:  
`globalwarps.command.%command name%.tabcomplete.%command alias%`  
This is an automatic function of ConnectorPlugin's BridgedCommand in order to provide compatibility with Bukkit plugins that register conflicting commands.

E.g. `globalwarps.command.delwarp.tabcomplete.removewarp` or simply `globalwarps.command.delwarp.tabcomplete.*`

## Setup

Install GlobalWarps on your proxy and ConnectorPlugin on both your proxy and your Minecraft servers.

GlobalWarps commands will be available on the **Minecraft server** in order to offer support for command blocking via WorldGuard region and similar plugins!

## Download

Latest development builds can be found on the Minebench.de Jenkins: https://ci.minebench.de/job/GlobalWarps/

## License

This project is [licensed](LICENSE) under the AGPLv3:

```
 ConnectorPlugin
 Copyright (C) 2021 Max Lee aka Phoenix616 (max@themoep.de)

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU Affero General Public License as published
 by the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU Affero General Public License for more details.

 You should have received a copy of the GNU Affero General Public License
 along with this program.  If not, see <https://www.gnu.org/licenses/>.
```
