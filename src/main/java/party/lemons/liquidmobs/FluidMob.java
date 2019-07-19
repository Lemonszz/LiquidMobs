package party.lemons.liquidmobs;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;

import java.awt.*;

public class FluidMob extends Fluid
{
	public FluidMob(String fluidName, ResourceLocation still, ResourceLocation flowing, Color color)
	{
		super(fluidName, still, flowing, color);
	}

	@Override
	public int getColor()
	{
		return color;
	}
}
