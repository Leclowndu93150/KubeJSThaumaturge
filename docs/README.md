# KubeJS Thaumaturge

This addon connects [Thaumaturge](https://github.com/Leclowndu93150/Thaumcraft) to KubeJS. If you can write a KubeJS script, you can add jars, wand parts, golem materials, recipes, aspects and research to Thaumaturge.

Everything on this site is a working example. Copy it, change the ids, and go.

## What goes where

| You want to add | Script folder | Page |
|---|---|---|
| Jars, wand parts, golem content | `startup_scripts` | [Jars](jars.md), [Wands](wand-parts.md), [Golems](golems.md) |
| Recipes | `server_scripts` | [Recipes](recipes.md) |
| Aspects, research, scans, blueprints | `server_scripts` | [Registries](registries.md) |
| Aspects on items and mobs | `server_scripts` | [Data maps](data-maps.md) |
| Game logic (aura, warp, research points) | `server_scripts` | [Bindings](bindings.md), [Events](events.md) |

## Install

Drop these three mods in your instance:

1. KubeJS 8.0 or newer
2. Thaumaturge 0.1.4 or newer
3. This addon

Nothing to configure. If the addon loaded you will see `Found plugin source kubejs_thaumaturge` in the log.
