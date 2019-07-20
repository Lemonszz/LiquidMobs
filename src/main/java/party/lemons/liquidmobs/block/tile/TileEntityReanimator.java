package party.lemons.liquidmobs.block.tile;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import party.lemons.liquidmobs.FluidMob;
import party.lemons.liquidmobs.FluidTankTileBase;
import party.lemons.liquidmobs.LiquidMobs;
import party.lemons.liquidmobs.network.MessageUpdateSaw;

public class TileEntityReanimator extends TileEntityFluidHandlerBase implements ITickable
{
	private int cooldown = 0;
	private State state = State.IDLE;
	public int time = 0;

	public TileEntityReanimator()
	{
		tank = new FluidTankTileBase(this, 1000);
	}

	@Override
	public void update()
	{
		time++;
		if(cooldown > 0)
		{
			cooldown--;

			if(cooldown <= 0 && state == State.SPAWNING)
			{
				FluidStack stack = getTank().getFluid();

				//Spawn
				if((stack.getFluid() instanceof FluidMob)  && !world.isRemote)
				{
					EntityLivingBase living = null;
					try
					{
						living	 =  LiquidMobs.MOBS.get(stack.getFluid()).getConstructor(World.class).newInstance(world);
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
					living.setPosition(getPos().getX() + 0.5, getPos().getY() + 0.6, getPos().getZ() + 0.5);
					world.spawnEntity(living);
					living.setHealth(living.getMaxHealth());
				}
				getTank().drain(10000, true);
				state = State.IDLE;
			}
		}

	}

	public boolean onCooldown()
	{
		return cooldown > 0;
	}

	public void startSpawning()
	{
		this.cooldown = 6 * 20;
		state = State.SPAWNING;
		this.markDirty();

		if(!world.isRemote)
			LiquidMobs.NETWORK.sendToAllAround(new MessageUpdateSaw(getPos(), writeToNBT(new NBTTagCompound())),
					new NetworkRegistry.TargetPoint(getWorld().provider.getDimension(), (double)getPos().getX(), (double)getPos().getY(), (double)getPos().getZ(), 128));
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		compound.setInteger("cooldown", cooldown);
		compound.setInteger("state", state.ordinal());

		return super.writeToNBT(compound);
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

	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		cooldown = compound.getInteger("cooldown");
		state = State.values()[compound.getInteger("state")];

		super.readFromNBT(compound);
	}

	@SideOnly(Side.CLIENT)
	public double getMaxRenderDistanceSquared()
	{
		return 65536.0D;
	}

	private enum State
	{
		SPAWNING,
		IDLE
	}
}
