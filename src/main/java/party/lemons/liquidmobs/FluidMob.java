package party.lemons.liquidmobs;

import net.minecraft.entity.EntityList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import java.awt.*;

import static party.lemons.liquidmobs.LiquidMobs.MODID;

public class FluidMob extends Fluid
{
	private EntityList.EntityEggInfo egg;

	public FluidMob( EntityList.EntityEggInfo egg)
	{
		super((EntityList.getTranslationName(egg.spawnedID)), new ResourceLocation(MODID,"blocks/water_still"), new ResourceLocation(MODID,"blocks/water_overlay"), egg.primaryColor);

		this.egg = egg;
	}

	@Override
	public String getLocalizedName(FluidStack fs)
	{
		String en = EntityList.getTranslationName(egg.spawnedID);
		String s = I18n.translateToLocal("entity." + en + ".name");

		return s + " " + I18n.translateToLocal("tile.mobwater.name");
	}

	@Override
	public int getColor()
	{
		return color;
	}
}
