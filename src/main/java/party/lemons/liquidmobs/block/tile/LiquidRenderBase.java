package party.lemons.liquidmobs.block.tile;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.FluidStack;
import party.lemons.liquidmobs.RenderUtil;

/**
 * Created by Sam on 22/03/2018.
 */
public abstract class LiquidRenderBase<T extends TileEntityFluidHandlerBase> extends TileEntitySpecialRenderer<T>
{
	@Override
	public void render(TileEntityFluidHandlerBase te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
	{
		FluidStack fluid = te.getTank().getFluid();

		if(fluid == null)
			return;
		GlStateManager.translate(x, y, z);
		RenderUtil.renderFluidCuboid(fluid, new BlockPos(x,y,z), 0, 0, 0, 0.1,  0.1,  0.1,  0.9, ( (float)fluid.amount / (float)1000) * 0.6,  0.9);
		GlStateManager.translate(-x, -y, -z);

	}
}