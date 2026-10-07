// Thaumaturge events and the newer bindings. Server scripts reload with /reload.

ThaumaturgeEvents.infusionCrafted(event => {
    if (event.result.id == 'minecraft:netherite_ingot' && Math.random() < 0.05) {
        event.result = Item.of('minecraft:netherite_ingot', 2)
    }
})

ThaumaturgeEvents.crucibleDecompose(event => {
    if (event.item.id == 'minecraft:rotten_flesh') {
        event.aspects = [{ aspect: 'thaumaturge:mortuus', amount: 4 }]
    }
})

ThaumaturgeEvents.crucibleCrafted(event => {
    console.info(`${event.player.username} brewed ${event.result.id}`)
})

ThaumaturgeEvents.knowledgeGained(event => {
    console.info(`${event.player.username} gained ${event.amount} ${event.type} in ${event.category}`)
})

// Also in client_scripts/thaumaturge_arcane_cost.js so the workbench screen agrees.
ThaumaturgeEvents.arcaneCraftCost(event => {
    if (event.player.stages.has('arcane_adept')) {
        event.auraVis = Math.floor(event.auraVis / 2)
    }
})

// Essentia: top up any jar a player right clicks with a stick.
BlockEvents.rightClicked(event => {
    if (event.item.id != 'minecraft:stick') return
    const level = event.level
    const pos = event.block.pos
    if (!Essentia.isStorage(level, pos, 'up')) return
    const added = Essentia.insert(level, pos, 'up', 'thaumaturge:aqua', 10)
    event.player.tell(`Added ${added} aqua, jar now holds ${Essentia.amount(level, pos, 'up', 'thaumaturge:aqua')}`)
})

// Hand out some ordo crystals on first join.
PlayerEvents.loggedIn(event => {
    const player = event.player
    if (player.stages.has('thaumaturge_starter')) return
    player.stages.add('thaumaturge_starter')
    player.give(Essentia.crystal('thaumaturge:ordo', 4))
})

// Warp from a vanilla item.
ServerEvents.generateData('before_mods', event => {
    ThaumaturgeData.itemWarp(event, {
        'minecraft:wither_rose': 2
    })
})
