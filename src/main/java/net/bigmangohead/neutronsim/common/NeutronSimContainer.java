package net.bigmangohead.neutronsim.common;

import net.bigmangohead.neutronsim.NeutronSim;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

import static org.apache.commons.lang3.ObjectUtils.min;

public class NeutronSimContainer extends Container {
    public NeutronSimContainer(InventoryPlayer playerInv, final NeutronSimTileEntity tileEntity) {
        IItemHandler inventoryMachines = tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP);
        IItemHandler inventoryNeutronium = tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.DOWN);
        addSlotToContainer(new SlotItemHandler(inventoryMachines, 0, 71, 35) {
            @Override
            public void onSlotChanged() {
                tileEntity.markDirty();
            }

            @Override
            public int getItemStackLimit(@Nonnull ItemStack stack) {
                return 100000000;
            }
        });
        addSlotToContainer(new SlotItemHandler(inventoryMachines, 1, 89, 35) {
            @Override
            public void onSlotChanged() {
                tileEntity.markDirty();
            }

            @Override
            public int getItemStackLimit(@Nonnull ItemStack stack) {
                return 100000000;
            }
        });
        addSlotToContainer(new SlotItemHandler(inventoryNeutronium, 0, 89, 55) {
            @Override
            public void onSlotChanged() {
                tileEntity.markDirty();
            }

            @Override
            public boolean isItemValid(@Nonnull ItemStack stack) {
                return false;
            }
        });

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                addSlotToContainer(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (int k = 0; k < 9; k++) {
            addSlotToContainer(new Slot(playerInv, k, 8 + k * 18, 142));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return true;
    }

    // Courtesy of user shadowfacts on GitHub
    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            int containerSlots = inventorySlots.size() - player.inventory.mainInventory.size();

            if (index < containerSlots) {
                if (!this.mergeItemStack(itemstack1, containerSlots, inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(itemstack1, 0, containerSlots, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.getCount() == 0) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, itemstack1);
        }

        return itemstack;
    }

    public ItemStack slotClick2(int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player) {
        NeutronSim.LOGGER.info("Running slotClick attempt");
        NeutronSim.LOGGER.info(clickTypeIn);
        NeutronSim.LOGGER.info(slotId);
        NeutronSim.LOGGER.info(dragType);
        ItemStack res = super.slotClick(slotId, dragType, clickTypeIn, player);
        NeutronSim.LOGGER.info(res.getCount());
        return res;
    }

    @Override
    public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player) {
        // Only do something different when interacting with machine slot
        if (0 <= slotId && slotId <= 1) {
            if (clickTypeIn == ClickType.PICKUP) {
                Slot hoveredSlot = this.inventorySlots.get(slotId);
                if (hoveredSlot == null) return ItemStack.EMPTY;

                ItemStack clickedItemStack = hoveredSlot.getStack();

                InventoryPlayer inventoryPlayer = player.inventory;
                ItemStack heldStack = inventoryPlayer.getItemStack();

                if (hoveredSlot.getStack().isEmpty()) {
                    hoveredSlot.putStack(heldStack);
                    inventoryPlayer.setItemStack(ItemStack.EMPTY);
                    hoveredSlot.onSlotChanged();
                } else if (hoveredSlot.getStack().isItemEqual(heldStack)) {
                    hoveredSlot.getStack().grow(heldStack.getCount());
                    inventoryPlayer.setItemStack(ItemStack.EMPTY);
                    hoveredSlot.onSlotChanged();
                } else if (heldStack.isEmpty()) {
                    int amountTaken = min(hoveredSlot.getStack().getCount(), hoveredSlot.getStack().getMaxStackSize());
                    ItemStack hoveredItemStackCopy = hoveredSlot.getStack().copy();
                    hoveredItemStackCopy.setCount(amountTaken);
                    inventoryPlayer.setItemStack(hoveredItemStackCopy);
                    hoveredSlot.decrStackSize(amountTaken);
                    NeutronSim.LOGGER.info(amountTaken);
                    hoveredSlot.onSlotChanged();
                }
            }
        } else {
            return super.slotClick(slotId, dragType, clickTypeIn, player);
        }
        return ItemStack.EMPTY;
    }
}
