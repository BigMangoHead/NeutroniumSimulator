package net.bigmangohead.neutronsim.common;

import net.bigmangohead.neutronsim.NeutronSim;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.registries.IForgeRegistry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static org.apache.commons.lang3.math.NumberUtils.min;

public class NeutronSimTileEntity extends TileEntity implements ITickable {
    private ItemStackHandler inventoryNeutronium = new ItemStackHandler(1);
    private ItemStackHandler inventoryMachines = new BigItemStackHandler(2) {
        @Override
        @Nonnull
        public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
            return stack;
        }
    };

    public static final ResourceLocation NEUTRONIUM_COMPRESSOR_LOCATION = new ResourceLocation("avaritia", "neutronium_compressor");
    public static final ResourceLocation WATCH_OF_TIME_LOCATION = new ResourceLocation("projecte", "item.pe_time_watch");
    public static final ResourceLocation NEUTRONIUM_LOCATION = new ResourceLocation("avaritia", "resource");
    private static final ItemStack NEUTRONIUM_ITEM = new ItemStack(GameRegistry.findRegistry(Item.class).getValue(NEUTRONIUM_LOCATION), 1, 4);
    public static final int TICKS_PER_INGOT = 100;

    private int progress = 0;
    private int progressPerTick = 0;
    private int tickCounter = 0;

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setTag("inventoryNeutronium", inventoryNeutronium.serializeNBT());
        compound.setTag("inventoryMachines", inventoryMachines.serializeNBT());
        compound.setInteger("progress", progress);
        return super.writeToNBT(compound);

    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        inventoryNeutronium.deserializeNBT(compound.getCompoundTag("inventoryNeutronium"));
        inventoryMachines.deserializeNBT(compound.getCompoundTag("inventoryMachines"));
        this.progress = compound.getInteger("progress");
        super.readFromNBT(compound);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            if (facing == EnumFacing.UP) {
                return (T) inventoryMachines;
            } else {
                return (T) inventoryNeutronium;
            }
        } else {
            return super.getCapability(capability, facing);
        }
    }

    @Override
    public void update() {
        if (tickCounter % 20 == 0) {
            int neutroniumCompressorCount = 0;
            if (inventoryMachines.getStackInSlot(0).getItem().getRegistryName().equals(NEUTRONIUM_COMPRESSOR_LOCATION)) {
                neutroniumCompressorCount = inventoryMachines.getStackInSlot(0).getCount();
            }
            int watchOfTimeCount = 0;
            if (inventoryMachines.getStackInSlot(1).getItem().getRegistryName().equals(WATCH_OF_TIME_LOCATION)) {
                watchOfTimeCount = inventoryMachines.getStackInSlot(1).getCount();
            }
            progressPerTick = neutroniumCompressorCount*(1 + watchOfTimeCount*9);
        }
        tickCounter++;
        progress += progressPerTick;
        if (progress > TICKS_PER_INGOT) {
            int toProduce = progress / TICKS_PER_INGOT;
            progress -= toProduce * TICKS_PER_INGOT;

            if (!inventoryNeutronium.getStackInSlot(0).isEmpty() && !inventoryNeutronium.getStackInSlot(0).getItem().getRegistryName().equals(NEUTRONIUM_LOCATION)) return;
            int curNeutronium = inventoryNeutronium.getStackInSlot(0).getCount();
            ItemStack resultStack = NEUTRONIUM_ITEM.copy();
            resultStack.setCount(min(curNeutronium + toProduce, NEUTRONIUM_ITEM.getMaxStackSize()));
            inventoryNeutronium.setStackInSlot(0, resultStack);

        }
    }

    public int getSpeed() {
        return progressPerTick;
    }
}
