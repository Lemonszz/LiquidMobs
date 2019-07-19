package party.lemons.liquidmobs;

import net.minecraft.block.Block;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.HashMap;

@Mod(modid = LiquidMobs.MODID, name = "Liquid Mobs", version = "1.0.0")
@Mod.EventBusSubscriber(modid = LiquidMobs.MODID)
public class LiquidMobs
{
	public static final String MODID = "liquidmobs";

	static {
		FluidRegistry.enableUniversalBucket();
}

	@SubscribeEvent
	public static void onRegisterBlock(RegistryEvent.Register<Block> event)
	{
		EnchantmentHelper.getEnchantments()
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


		Fluid fluid = new Fluid(EntityList.getTranslationName(egg.spawnedID), new ResourceLocation(MODID,"blocks/water_still"), new ResourceLocation(MODID,"blocks/water_overlay"), egg.primaryColor)
		{
			@Override
			public String getLocalizedName(FluidStack fs)
			{
				String en = EntityList.getTranslationName(egg.spawnedID);
				String s = I18n.translateToLocal("entity." + en + ".name");

				return s + " " + I18n.translateToLocal("tile.mobwater.name");
			}
		};
		FluidRegistry.registerFluid(fluid);

		FLUIDS.put(entityClass, fluid);
	}


	public static final HashMap<Class<? extends EntityLiving>, Fluid> FLUIDS = new HashMap<>();
}
