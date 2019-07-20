package party.lemons.liquidmobs;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.TileFluidHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import party.lemons.liquidmobs.network.MessageUpdateSaw;

public class FluidTankTileBase extends FluidTank
{
	public FluidTankTileBase(final TileFluidHandler tileEntity, final int capacity) {
		super(capacity);
		tile = tileEntity;
	}

	public FluidTankTileBase(final TileFluidHandler tileEntity, final FluidStack stack, final int capacity) {
		super(stack, capacity);
		tile = tileEntity;
	}

	public FluidTankTileBase(final TileFluidHandler tileEntity, final Fluid fluid, final int amount, final int capacity) {
		super(fluid, amount, capacity);
		tile = tileEntity;
	}

	protected void onContentsChanged()
	{
		System.out.println("Contents " + tile.getWorld().isRemote);
		if(!tile.getWorld().isRemote)
			LiquidMobs.NETWORK.sendToAllAround(new MessageUpdateSaw(tile.getPos(), tile.writeToNBT(new NBTTagCompound())),
					new NetworkRegistry.TargetPoint(tile.getWorld().provider.getDimension(), (double)tile.getPos().getX(), (double)tile.getPos().getY(), (double)tile.getPos().getZ(), 128));
	}
}