// Reading and changing research from scripts. Server scripts reload with /reload.

// A crafted tome that hands over the whole alchemy tree.
ItemEvents.rightClicked('minecraft:enchanted_book', event => {
    const player = event.player
    if (Research.isComplete(player, 'thaumaturge:unlock_alchemy')) return
    const granted = Research.grant(player, 'thaumaturge:unlock_alchemy')
    player.tell(`The tome teaches you ${granted} things.`)
    event.item.count--
})

// Finishing eldritch research costs you a level of alchemy theory.
ThaumaturgeEvents.researchCompleted(event => {
    if (event.research != 'thaumaturge:unlock_eldritch') return
    Research.addKnowledge(event.player, 'THEORY', 'thaumaturge:alchemy', -32)
})

// Dying sets the primal staff research back to its first stage.
EntityEvents.death('minecraft:player', event => {
    const player = event.entity
    if (Research.isKnown(player, 'thaumaturge:staff_primal') && !Research.isComplete(player, 'thaumaturge:staff_primal')) {
        Research.forget(player, 'thaumaturge:staff_primal')
        Research.unlock(player, 'thaumaturge:staff_primal')
    }
})

// One in ten zombies hits with warp.
EntityEvents.spawned('minecraft:zombie', event => {
    if (Math.random() < 0.1) {
        MobTraits.add(event.entity, 'thaumaturge:warping_strike')
    }
})

// Data maps for the tainted mob and armor trim systems.
ServerEvents.generateData('before_mods', event => {
    ThaumaturgeData.taintedProfile(event, {
        'minecraft:goat': { traits: ['thaumaturge:leaping'], hunts_animals: true, natural_spawns: true }
    })
    ThaumaturgeData.trimBonus(event, {
        'minecraft:netherite': { vis_discount: 2, warp: 1 }
    })
})
