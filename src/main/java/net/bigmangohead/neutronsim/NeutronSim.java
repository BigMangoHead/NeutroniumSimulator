package net.bigmangohead.neutronsim;

import net.bigmangohead.neutronsim.common.ModGuiHandler;
import net.bigmangohead.neutronsim.common.NeutronSimBlock;
import net.bigmangohead.neutronsim.common.NeutronSimTileEntity;
import net.bigmangohead.neutronsim.common.WatchPedestalItem;
import net.bigmangohead.neutronsim.neutronsim.Tags;
import net.bigmangohead.neutronsim.proxy.CommonProxy;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = Tags.MOD_ID, name = Tags.MOD_NAME, version = Tags.VERSION)
public class NeutronSim {

    public static final Logger LOGGER = LogManager.getLogger(Tags.MOD_NAME);

    @Mod.Instance(Tags.MOD_ID)
    public static NeutronSim instance;

    @SidedProxy(serverSide = "net.bigmangohead.neutronsim.proxy.CommonProxy", clientSide = "net.bigmangohead.neutronsim.proxy.ClientProxy")
    public static CommonProxy proxy;

    /**
     * <a href="https://cleanroommc.com/wiki/forge-mod-development/event#overview">
     *     Take a look at how many FMLStateEvents you can listen to via the @Mod.EventHandler annotation here
     * </a>
     */
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        LOGGER.info("Pre-initializing {}", Tags.MOD_NAME);
        NetworkRegistry.INSTANCE.registerGuiHandler(this, new ModGuiHandler());
    }

    public static NeutronSimBlock neutronSimBlock = new NeutronSimBlock().setCreativeTab(CreativeTabs.SEARCH);
    public static WatchPedestalItem watchPedestalItem = new WatchPedestalItem();

    @Mod.EventBusSubscriber
    public static class RegistrationHandler {
        @SubscribeEvent
        public static void registerBlocks(RegistryEvent.Register<Block> event) {
            IForgeRegistry<Block> registry = event.getRegistry();
            registry.register(neutronSimBlock);

            GameRegistry.registerTileEntity(NeutronSimTileEntity.class, neutronSimBlock.getRegistryName().toString());
        }

        @SubscribeEvent
        public static void registerItems(RegistryEvent.Register<Item> event) {
            IForgeRegistry<Item> registry = event.getRegistry();
            registry.register(neutronSimBlock.createItemBlock());
            registry.register(watchPedestalItem);
            watchPedestalItem.registerItemModel();
        }

        @SubscribeEvent
        public static void registerModels(ModelRegistryEvent event) {
            neutronSimBlock.registerItemModel(Item.getItemFromBlock(neutronSimBlock));
        }
    }

}
