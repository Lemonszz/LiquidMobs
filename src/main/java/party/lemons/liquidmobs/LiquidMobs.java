package party.lemons.liquidmobs;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;
import party.lemons.liquidmobs.block.BlockReanimator;
import party.lemons.liquidmobs.block.BlockSaw;
import party.lemons.liquidmobs.block.tile.ReanimatorRender;
import party.lemons.liquidmobs.block.tile.TileEntityReanimator;
import party.lemons.liquidmobs.block.tile.SawTankRender;
import party.lemons.liquidmobs.block.tile.TileEntitySaw;
import party.lemons.liquidmobs.network.MessageUpdateSaw;

import java.util.HashMap;

@Mod(modid = LiquidMobs.MODID, name = "Liquid Mobs", version = "1.0.0")
@Mod.EventBusSubscriber(modid = LiquidMobs.MODID)
public class LiquidMobs
{
	public static final String MODID = "liquidmobs";
	public static final SimpleNetworkWrapper NETWORK = NetworkRegistry.INSTANCE.newSimpleChannel(MODID);

	@Mod.EventHandler
	public void onInit(FMLInitializationEvent event)
	{
		NETWORK.registerMessage(MessageUpdateSaw.Handler.class, MessageUpdateSaw.class, 0, Side.CLIENT);
	}

	@Mod.EventBusSubscriber(modid = MODID)
	@GameRegistry.ObjectHolder(MODID)
	public static class ModBlocks
	{
		public static final Block SAW = Blocks.AIR;
		public static final Block REANIMATOR = Blocks.AIR;

		@SubscribeEvent
		public static void onRegisterBlock(RegistryEvent.Register<Block> event)
		{
			event.getRegistry().registerAll(
					new BlockSaw().setCreativeTab(CreativeTabs.DECORATIONS).setTranslationKey(MODID + ".saw").setRegistryName(MODID, "saw"),
					new BlockReanimator().setCreativeTab(CreativeTabs.DECORATIONS).setTranslationKey(MODID + ".reanimator").setRegistryName(MODID, "reanimator")
			);

			GameRegistry.registerTileEntity(TileEntitySaw.class, new ResourceLocation(MODID, "saw"));
			GameRegistry.registerTileEntity(TileEntityReanimator.class, new ResourceLocation(MODID, "reanimator"));
		}

		@SubscribeEvent
		public static void onRegisterItem(RegistryEvent.Register<Item> event)
		{
			event.getRegistry().registerAll(
					new ItemBlock(SAW).setRegistryName(SAW.getRegistryName()),
					new ItemBlock(REANIMATOR).setRegistryName(REANIMATOR.getRegistryName())
			);
		}

		@SubscribeEvent
		@SideOnly(Side.CLIENT)
		public static void onRegisterModel(ModelRegistryEvent event)
		{
			ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(SAW), 0, new ModelResourceLocation(SAW.getRegistryName(), "inventory"));
			ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(REANIMATOR), 0, new ModelResourceLocation(REANIMATOR.getRegistryName(), "inventory"));
			ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySaw.class, new SawTankRender());
			ClientRegistry.bindTileEntitySpecialRenderer(TileEntityReanimator.class, new ReanimatorRender());

		}
	}

	static {
		FluidRegistry.enableUniversalBucket();
	}

	@SubscribeEvent
	public static void onRegisterBlock(RegistryEvent.Register<Block> event)
	{
		EntityList.ENTITY_EGGS.entrySet().stream().forEach(m -> createFluid(event.getRegistry(), m.getValue()));
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void registerItems(RegistryEvent.Register<Item> event)
	{
		final IForgeRegistry<Item> registry = event.getRegistry();
		FLUIDS.values().forEach(f->FluidRegistry.addBucketForFluid(f));
	}

	private static void createFluid(IForgeRegistry<Block> blReg, EntityList.EntityEggInfo egg)
	{
		Class<? extends EntityLiving> entityClass = (Class<? extends EntityLiving>) EntityList.getClass(egg.spawnedID);


		Fluid fluid = new FluidMob(egg);
		FluidRegistry.registerFluid(fluid);

		FLUIDS.put(entityClass, fluid);
		MOBS.put(fluid, entityClass);
	}


	public static final HashMap<Class<? extends EntityLiving>, Fluid> FLUIDS = new HashMap<>();
	public static final HashMap<Fluid, Class<? extends EntityLiving>> MOBS = new HashMap<>();
}
