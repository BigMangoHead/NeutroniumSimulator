package net.bigmangohead.neutronsim.common;

import net.bigmangohead.neutronsim.NeutronSim;
import net.minecraft.item.Item;

public class WatchPedestalItem extends Item {
    protected String name;

    public WatchPedestalItem() {
        this.name = "watch_pedestal_item";
        setTranslationKey("watch_pedestal_item");
        setRegistryName(name);
    }

    public void registerItemModel() {
        NeutronSim.proxy.registerItemRenderer(this, 0, name);
    }


}
