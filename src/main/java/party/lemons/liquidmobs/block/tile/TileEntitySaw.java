package party.lemons.liquidmobs.block.tile;

import party.lemons.liquidmobs.FluidTankTileBase;

public class TileEntitySaw extends TileEntityFluidHandlerBase
{
	public TileEntitySaw()
	{
		tank = new FluidTankTileBase(this, 1000);

	}

	@Override
	public void setCooldown(int cooldown)
	{
	}

	@Override
	public int getMaxTransferRate()
	{
		return 0;
	}

	@Override
	public int getCooldownLength()
	{
		return 0;
	}
}
