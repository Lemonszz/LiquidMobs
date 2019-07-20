package party.lemons.liquidmobs.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import party.lemons.liquidmobs.LiquidMobs;
import party.lemons.liquidmobs.network.MessageUpdateSaw;
import party.lemons.liquidmobs.block.tile.TileEntitySaw;

import javax.annotation.Nullable;

public class BlockSaw extends Block
{
	public static PropertyBool ROTATED = PropertyBool.create("rotated");
	protected static final AxisAlignedBB AABB = new AxisAlignedBB(0.0D, 0.0D, 0, 1.0D, 0.6D, 1);

	public BlockSaw()
	{
		super(Material.IRON, MapColor.IRON);
		this.setDefaultState(this.blockState.getBaseState().withProperty(ROTATED, false));
	}

	@Override
	public void onEntityCollision(World worldIn, BlockPos pos, IBlockState state, Entity entityIn)
	{
		if(worldIn.isRemote)
		{
			super.onEntityCollision(worldIn, pos, state, entityIn);
			return;
		}

		TileEntity te = worldIn.getTileEntity(pos);
		if(!(te instanceof TileEntitySaw))
		{
			super.onEntityCollision(worldIn, pos, state, entityIn);
			return;
		}
		entityIn.attackEntityFrom(DamageSource.GENERIC, 2);
		if(LiquidMobs.FLUIDS.keySet().contains(entityIn.getClass()) && entityIn instanceof EntityLivingBase)
		{
			EntityLivingBase living = (EntityLivingBase) entityIn;
			if (living.getHealth() <= 0)
			{
				TileEntitySaw saw = (TileEntitySaw) te;
				FluidStack stack = new FluidStack(LiquidMobs.FLUIDS.get(living.getClass()), 1000, living.writeToNBT(new NBTTagCompound()));
				if(saw.getTank().canFillFluidType(stack))
				{
					saw.getTank().fill(stack, true);
					saw.markDirty();
					LiquidMobs.NETWORK.sendToAllAround(new MessageUpdateSaw(saw.getPos(), saw.writeToNBT(new NBTTagCompound())),
								new NetworkRegistry.TargetPoint(saw.getWorld().provider.getDimension(), (double)saw.getPos().getX(), (double)saw.getPos().getY(), (double)saw.getPos().getZ(), 128));
				}
			}

		}
		super.onEntityCollision(worldIn, pos, state, entityIn);
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		return !player.isSneaking() && FluidUtil.interactWithFluidHandler(player, hand, world, pos, side);
	}

	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}

	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		return AABB;
	}

	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getRenderLayer()
	{
		return BlockRenderLayer.CUTOUT;
	}

	public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
	{
		EnumFacing enumfacing = placer.getHorizontalFacing().rotateY();
		return super.getStateForPlacement(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer).withProperty(ROTATED, enumfacing == EnumFacing.EAST || enumfacing == EnumFacing.WEST ? false : true);
	}

	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
	{
		return true;
	}

	public IBlockState getStateFromMeta(int meta)
	{
		return meta == 0 ? getDefaultState() : getDefaultState().withProperty(ROTATED, true);
	}

	public int getMetaFromState(IBlockState state)
	{
		return state.getValue(ROTATED) ? 1 : 0;
	}

	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, ROTATED);
	}

	@Override
	public boolean hasTileEntity(IBlockState state)
	{
		return true;
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(World world, IBlockState state)
	{
		return new TileEntitySaw();
	}
}
