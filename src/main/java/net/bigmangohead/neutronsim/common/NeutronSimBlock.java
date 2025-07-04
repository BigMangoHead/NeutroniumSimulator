package net.bigmangohead.neutronsim.common;

import net.bigmangohead.neutronsim.NeutronSim;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.ObjectUtils.min;

public class NeutronSimBlock extends Block {
    protected String name;

    public NeutronSimBlock() {
        super(Material.IRON);

        this.name = "neutronium_simulator";

        setTranslationKey("neutronium_simulator");

        setRegistryName(name);
        setHardness(10f);
        setResistance(10f);
        setHarvestLevel("pickaxe", 3);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!world.isRemote) {
            NeutronSimTileEntity tile = getTileEntity(world, pos);
            player.sendMessage(new TextComponentString("Neutronium ingots per minute: " + 60*20*tile.getSpeed() / ((float)NeutronSimTileEntity.TICKS_PER_INGOT)));
            player.openGui(NeutronSim.instance, ModGuiHandler.NEUTRONSIM, world, pos.getX(), pos.getY(), pos.getZ());
        }
        return true;
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        NeutronSimTileEntity tile = getTileEntity(world, pos);
        IItemHandler itemHandlerMachines = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP);
        IItemHandler itemHandlerNeutronium = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.DOWN);

        List<ItemStack> itemStacks = new ArrayList<>();
        itemStacks.add(itemHandlerNeutronium.getStackInSlot(0));
        itemStacks.add(itemHandlerMachines.getStackInSlot(0));
        itemStacks.add(itemHandlerMachines.getStackInSlot(1));

        for (ItemStack stack : itemStacks) {
            while (!stack.isEmpty()) {
                int amountSpawned = min(stack.getMaxStackSize(), stack.getCount());
                ItemStack newStack = stack.copy();
                newStack.setCount(amountSpawned);
                EntityItem item = new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), newStack);
                world.spawnEntity(item);
                stack.setCount(stack.getCount() - amountSpawned);
            }
        }
        super.breakBlock(world, pos, state);
    }

    public void registerItemModel(Item itemBlock) {
        NeutronSim.proxy.registerItemRenderer(itemBlock, 0, name);
    }

    public Item createItemBlock() {
        return new ItemBlock(this).setRegistryName(getRegistryName());
    }

    @Override
    public NeutronSimBlock setCreativeTab(CreativeTabs tab) {
        super.setCreativeTab(tab);
        return this;
    }

    public NeutronSimTileEntity getTileEntity(IBlockAccess world, BlockPos pos) {
        return (NeutronSimTileEntity) world.getTileEntity(pos);
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new NeutronSimTileEntity();
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        tooltip.add("Compresses neutron collectors and watches");
        tooltip.add("of flowing time into a single block.");
        tooltip.add("Decreases the effectiveness of watches");
        tooltip.add("by a factor of two.");
    }
}
