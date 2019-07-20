package party.lemons.liquidmobs.block.tile;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.opengl.GL11;
import party.lemons.liquidmobs.FluidMob;
import party.lemons.liquidmobs.LiquidMobs;

import java.util.Random;

public class ReanimatorRender extends LiquidRenderBase<TileEntityReanimator>
{
	Random rnd = new Random();

	public void render(TileEntityReanimator te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
	{
		super.render(te, x, y, z, partialTicks, destroyStage, alpha);

		FluidStack stack = te.getTank().getFluid();
		if(stack == null)
			return;

		if(!(stack.getFluid() instanceof FluidMob))
		{
			return;
		}

		EntityLivingBase living = null;
		try
		{
			living	 =  LiquidMobs.MOBS.get(stack.getFluid()).getConstructor(World.class).newInstance(Minecraft.getMinecraft().world);
		}
		catch (Exception e)
		{

		}
		if(living == null)
			return;

		if(stack.tag != null)
		{
			living.readFromNBT(stack.tag);
		}

		RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
		Render render = rendermanager.getEntityRenderObject(living);
		living.setHealth(living.getMaxHealth());

		GlStateManager.pushMatrix();
		float scale = 6F;

		float offsetX = (rnd.nextFloat() / 10) * (rnd.nextBoolean() ? -1 : 1);
		float offsetY = (rnd.nextFloat() / 10) * (rnd.nextBoolean() ? -1 : 1);
		float offsetZ = (rnd.nextFloat() / 10) * (rnd.nextBoolean() ? -1 : 1);

		if(te.time % 10 == 0)
		{
			living.rotationPitch = rnd.nextInt(360);
			living.rotationYaw = rnd.nextInt(360);
			living.rotationYawHead = rnd.nextInt(360);

			living.prevRotationPitch = rnd.nextInt(360);
			living.prevRotationYaw = rnd.nextInt(360);
			living.prevRotationYawHead= rnd.nextInt(360);
		}

		render.doRender(living, x + 0.5 + offsetX, y + 0.6  + offsetY, z + 0.5 + offsetZ, 90, partialTicks);
		GlStateManager.disableLighting();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.DST_ALPHA);


		GlStateManager.color(1.0F, 1.0F, 1.0F, partialTicks);

		render.doRender(living, x + 0.5, y + 0.6  , z + 0.5, living.rotationYaw, partialTicks);


		GlStateManager.popMatrix();
	}
}
