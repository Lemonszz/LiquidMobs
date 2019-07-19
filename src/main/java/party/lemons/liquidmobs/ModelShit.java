package party.lemons.liquidmobs;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

import static party.lemons.liquidmobs.LiquidMobs.MODID;

@Mod.EventBusSubscriber(modid = MODID, value = Side.CLIENT)
public class ModelShit
{
	@SubscribeEvent
	public static void onStitch(TextureStitchEvent.Pre event)
	{
		event.getMap().registerSprite(new ResourceLocation(MODID,"blocks/water_still"));
		event.getMap().registerSprite(new ResourceLocation(MODID,"blocks/water_overlay"));
	}

}
