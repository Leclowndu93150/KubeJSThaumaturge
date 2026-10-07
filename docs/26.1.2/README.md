# KubeJS Thaumaturge for Minecraft 26.1.2

This addon connects [Thaumaturge](https://github.com/Leclowndu93150/Thaumaturge) to KubeJS. If you can write a KubeJS script, you can add jars, wand parts, golem materials, recipes, aspects and research to Thaumaturge.

Everything on this site is a working example. Copy it, change the ids, and go.

## What goes where

| You want to add | Script folder | Page |
|---|---|---|
| Jars, wand parts, golem content | `startup_scripts` | [Jars](jars.md), [Wands](wand-parts.md), [Golems](golems.md), [Accessories](golem-accessories.md) |
| Infusion stabilisers | `startup_scripts` | [Infusion stabilisers](infusion.md) |
| Recipes | `server_scripts` | [Recipes](recipes.md) |
| Aspects, research, scans, blueprints, pech trades | `server_scripts` | [Registries](registries.md) |
| Aspects and warp on items, mobs and biomes | `server_scripts` | [Data maps](data-maps.md) |
| Game logic (aura, warp, essentia, research points) | `server_scripts` | [Bindings](bindings.md), [Events](events.md) |

## Install

Drop these three mods in your instance, all NeoForge:

| Mod | Version |
|---|---|
| KubeJS | 8.0.6 or newer |
| Thaumaturge | 0.2.1 or newer |
| KubeJS Thaumaturge | the 26.1.2 build |

On Minecraft 1.21.1? Use the [1.21.1 docs](/1.21.1/) instead.

Nothing to configure. If the addon loaded you will see `Found plugin source kubejs_thaumaturge` in the log.
