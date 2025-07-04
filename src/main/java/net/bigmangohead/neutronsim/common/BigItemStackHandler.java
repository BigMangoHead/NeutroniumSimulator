package net.bigmangohead.neutronsim.common;

import net.bigmangohead.neutronsim.NeutronSim;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class BigItemStackHandler extends ItemStackHandler {
    public BigItemStackHandler(int size) {
        super(size);
    }

    @Override
    public int getSlotLimit(int slot) {
        return 100000000;
    }

    @Override
    protected int getStackLimit(int slot, @Nonnull ItemStack stack) {
        return 100000000;
    }
}
