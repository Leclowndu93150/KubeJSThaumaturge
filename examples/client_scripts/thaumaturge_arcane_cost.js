// arcaneCraftCost runs on both sides. This mirrors the server script so the
// arcane workbench shows the same cost the server charges.
ThaumaturgeEvents.arcaneCraftCost(event => {
    if (event.player.stages.has('arcane_adept')) {
        event.auraVis = Math.floor(event.auraVis / 2)
    }
})
