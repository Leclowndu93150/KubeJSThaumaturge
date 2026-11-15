# Golems

A Thaumaturge golem is a combination, not a single entity: one material plus a head, arms, legs and an addon. The material decides what the golem is made of (body colour, health, armor, damage). The other parts are functional variants like scout heads or flyer legs and work with every material.

Every golem is drawn with Minecraft's copper golem model. Parts add fittings on top of it: a scout lens on the head, claws on the arms, wheels under the body.

Because of that, adding one material gives you a full new golem line. Every existing part combines with it, the golem press offers it, and the creative tab gets a placer for it. All automatic.

## A material

```js
StartupEvents.registry('thaumaturge:golem_material', event => {
    event.create('mypack:diamond')
        .health(26)
        .armor(11)
        .damage(4)
        .itemColor(0x4AEDD9)
        .base('minecraft:diamond')
        .mechanism('thaumaturge:mechanism_simple')
        .traits(['thaumaturge:heavy', 'thaumaturge:armored'])
        .texture('minecraft:textures/block/diamond_block.png')
        .partName('Diamond')
})
```

The material texture is a swatch, not a golem skin. Its colours are mapped onto the copper golem's shading, darkest to lightest, so any block texture works and the golem comes out looking like it was built from that block. Fittings that sample the material (part pieces named `bm...`) draw the texture directly, so a tiling block texture looks best. The mod's own materials point at `minecraft:textures/block/iron_block.png`, `minecraft:textures/block/terracotta.png` and so on.

No texture to hand? Give it a colour instead:

```js
event.create('mypack:amethyst').base('minecraft:amethyst_shard').recolor(0x9A5CC6)
```

`.recolor(...)` builds the swatch by recolouring the mod's thaumium plate: shading kept, hue and saturation swapped. It also becomes the placer tint unless you set `.itemColor(...)`.

With neither `.texture(...)` nor `.recolor(...)` the swatch is read from `kubejs/assets/mypack/textures/block/diamond.png`.

## Material methods

| Method | What it does |
|---|---|
| `.health(int)` | Added to the golem base of 10 |
| `.armor(int)` | Armor rating |
| `.damage(int)` | Base melee damage |
| `.itemColor(rgb)` | Tint of the placer item |
| `.base(item)` | The material's base crafting component |
| `.mechanism(item)` | The material's mechanism component |
| `.traits([ids])` | Traits every golem of this material carries |
| `.research([ids])` | Research entries gating it in the press. Empty means always available |
| `.texture(id)` | The colour swatch, a full texture path. Defaults to `<namespace>:textures/block/<name>.png` |
| `.recolor(color)` | Generates the swatch from this colour, see above |
| `.antenna(bool)` | Draw the copper golem's head antenna. Off by default, and always hidden while the golem wears a hat accessory |
| `.partName(text)` / `.partText(text)` | Name and description in the press and tooltips |

## Parts

Heads, arms, legs and addons work the same way with their own registries:

```js
StartupEvents.registry('thaumaturge:golem_head', event => {
    event.create('mypack:diving')
        .traits(['thaumaturge:smart'])
        .component('minecraft:glass')
        .materialBase()
        .partName('Diving Helm')
        .partText('Lets the golem think under water. Allegedly.')
})
```

`component(item)` adds a fixed crafting component. `materialBase()` and `materialMechanism()` add slots that cost whatever the golem's material uses, so an iron golem pays iron plates and a diamond one pays diamonds.

Registries: `thaumaturge:golem_head`, `thaumaturge:golem_arm`, `thaumaturge:golem_leg`, `thaumaturge:golem_addon`.

## Part models

A part without a model is stat only and the golem keeps the plain copper golem look, which is what the basic head, arms and legs do. To draw a fitting, give the part a mesh:

```js
StartupEvents.registry('thaumaturge:golem_head', event => {
    event.create('mypack:lantern')
        .model('mypack:models/mesh/golem_head_lantern.ttmesh')
        .modelTexture('minecraft:textures/block/lantern.png')
})
```

Meshes use Thaumaturge's open `.ttmesh` JSON format. The quickest start is to copy one from the jar's `assets/thaumaturge/models/mesh/` folder (`golem_head_scout.ttmesh`, `golem_arms_claws.ttmesh`, `golem_hauler.ttmesh` and so on); the format spec ships with the Thaumaturge source as `docs/ttmesh-format.md`, along with `obj2mesh.py` to convert from Blender.

| Method | Default | What it does |
|---|---|---|
| `.model(id)` | none | The mesh, a full path ending in `.ttmesh` |
| `.modelTexture(id)` | the material swatch | Texture for mesh pieces that do not name their own |
| `.attachPoint(point)` | the part's own slot | Where the mesh is drawn: `'head'`, `'body'`, `'arms'` or `'legs'` |

Where each attach point puts the mesh origin, in model units (1 = one block of the copper golem model before the golem's 0.6 scale):

| Point | Origin | Follows |
|---|---|---|
| `head` | The copper golem's head pivot. The head reaches 5/16 up, 4/16 to each side and 5/16 forward | Head turn and the body's lean |
| `body` | The bottom of the torso. The chest reaches 6/16 up, 4/16 to each side and 3/16 forward | The body's lean |
| `arms` | Each arm, drawn once per side | The arm swing |
| `legs` | Each leg, drawn once per side | The leg swing |

`+Y` is up and `-Z` is the golem's front. Heads default to `head`, arms to `arms`, legs to `legs` and addons to `body`. Only the mod's roller and floater legs hide the copper golem's own legs, so custom legs are drawn on top of them.

Mesh pieces pick their texture in this order: a full texture id in the piece's own `texture_slot`, then the material swatch for pieces whose name starts with `bm`, then `.modelTexture(...)`, then the material swatch.

Addons can draw on several points at once, like the mod's armor plating with its pauldrons:

```js
StartupEvents.registry('thaumaturge:golem_addon', event => {
    event.create('mypack:plating')
        .model('mypack:models/mesh/golem_plating.ttmesh')
        .extraModel('mypack:models/mesh/golem_plating_shoulders.ttmesh', 'arms')
        .materialBase()
        .materialBase()
        .traits(['thaumaturge:armored'])
})
```

`.extraModel(mesh, point)` and `.extraModel(mesh, texture, point)` can be called as often as you like.

## Part behaviour

Parts can run script code on the golem that carries them. Every part kind takes `.onTick`, which runs once per golem tick on both the server and the client:

```js
StartupEvents.registry('thaumaturge:golem_leg', event => {
    event.create('mypack:spring')
        .materialBase()
        .onTick(golem => {
            const self = golem.asEntity()
            if (!golem.level().isClientSide() && self.onGround() && self.tickCount % 40 == 0) {
                self.setDeltaMovement(self.deltaMovement.add(0, 0.6, 0))
            }
        })
})
```

Arms also take combat hooks, which run on the server:

```js
StartupEvents.registry('thaumaturge:golem_arm', event => {
    event.create('mypack:ember_fists')
        .traits(['thaumaturge:fighter'])
        .onMeleeHit((golem, target) => target.setRemainingFireTicks(60))

    event.create('mypack:snowball_launcher')
        .traits(['thaumaturge:fighter', 'thaumaturge:ranged'])
        .onRangedAttack((golem, target, power) => {
            const self = golem.asEntity()
            const ball = self.level().createEntity('minecraft:snowball')
            ball.setOwner(self)
            ball.setPos(self.x, self.eyeY - 0.1, self.z)
            ball.shoot(target.x - self.x, target.eyeY - 0.5 - ball.y, target.z - self.z, 1.4, 4)
            ball.spawn()
        })
        .rangedTiming(15, 25, 12)
})
```

| Method | Parts | What it does |
|---|---|---|
| `.onTick(golem => ...)` | all | Every golem tick, both sides |
| `.onMeleeHit((golem, target) => ...)` | arms | After a melee hit lands |
| `.onRangedAttack((golem, target, power) => ...)` | arms | Fires a ranged attack. `power` runs from 0.1 point blank to 1 at the edge of range |
| `.rangedTiming(min, max, range)` | arms | Ticks between shots and attack range. Defaults 20, 25 and 16, like the mod's dart launchers |

Ranged attacks only happen when the golem has the `thaumaturge:ranged` trait, so give it to the arms. `golem` is the golem API object: `golem.asEntity()` for the entity, plus `golem.level()`, `golem.properties()`, `golem.hands()` and `golem.isInCombat()`. These run for every golem built with the part, so keep `onTick` cheap.

## Traits

Traits are behavior tags. The mod ships nineteen (heavy, light, fragile, armored, smart, fireproof and so on) and you can add your own:

```js
StartupEvents.registry('thaumaturge:golem_trait', event => {
    event.create('mypack:aquatic').partName('Aquatic')
    event.create('mypack:landlocked').opposite('mypack:aquatic').partName('Landlocked')
})
```

Opposite traits cancel each other when a golem would have both, like heavy and light do. Trait icons go at `kubejs/assets/mypack/textures/misc/golem/tag_aquatic.png`.

## Testing

Grab a placer straight from the creative tab, or:

```
/give @s thaumaturge:golem[thaumaturge:golem_properties={material:"mypack:diamond",head:"thaumaturge:basic",arms:"thaumaturge:basic",legs:"thaumaturge:walker",addon:"thaumaturge:none"}]
```
