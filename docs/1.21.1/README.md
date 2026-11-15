# KubeJS Thaumaturge for Minecraft 1.21.1

This addon connects [Thaumaturge](https://github.com/Leclowndu93150/Thaumaturge) to KubeJS. If you can write a KubeJS script, you can add jars, wand parts, golem materials, recipes, aspects and research to Thaumaturge.

Everything on this site is a working example. Copy it, change the ids, and go.

## What goes where

| You want to add | Script folder | Page |
|---|---|---|
| Jars, wand parts, golem content | `startup_scripts` | [Jars](jars.md), [Wands](wand-parts.md), [Golems](golems.md), [Accessories](golem-accessories.md) |
| Infusion stabilisers | `startup_scripts` | [Infusion stabilisers](infusion.md) |
| Recipes | `server_scripts` | [Recipes](recipes.md) |
| Aspects, research, scans, blueprints, pech trades, the labyrinth | `server_scripts` | [Registries](registries.md) |
| Spell parts, aspect affinities, spell hooks | `server_scripts` | [Spells](spells.md) |
| Aspects and warp on items, mobs and biomes | `server_scripts` | [Data maps](data-maps.md) |
| Game logic (aura, warp, essentia, research, research points) | `server_scripts` | [Bindings](bindings.md), [Events](events.md) |

## Install

Drop these three mods in your instance, all NeoForge:

| Mod | Version |
|---|---|
| KubeJS | 2101.7.2 or newer |
| Thaumaturge | 1.1.0 or newer |
| KubeJS Thaumaturge | the 1.21.1 build |

On Minecraft 26.1.2? Use the [26.1.2 docs](/26.1.2/) instead.

Nothing to configure. If the addon loaded you will see `Found plugin source kubejs_thaumaturge` in the log.
