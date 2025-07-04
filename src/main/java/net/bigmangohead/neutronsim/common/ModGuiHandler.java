package net.bigmangohead.neutronsim.common;

import net.bigmangohead.neutronsim.client.NeutronSimGui;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class ModGuiHandler implements IGuiHandler {
    public static final int NEUTRONSIM = 0;

    @Override
    public Container getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID == NEUTRONSIM) {
            return new NeutronSimContainer(player.inventory, (NeutronSimTileEntity) world.getTileEntity(new BlockPos(x, y, z)));
        } else {
            return null;
        }
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID == NEUTRONSIM) return new NeutronSimGui(getServerGuiElement(ID, player, world, x, y, z), player.inventory);
        else return null;
    }


}
