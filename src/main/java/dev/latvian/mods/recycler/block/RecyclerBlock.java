package dev.latvian.mods.recycler.block;

import dev.latvian.mods.recycler.block.entity.RecyclerEntity;
import dev.latvian.mods.recycler.gui.RecyclerMenu;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.Random;

/**
 * @author LatvianModder
 */
public class RecyclerBlock extends Block implements IWaterLoggable {
	public static final VoxelShape SHAPE_X = box(0, 0, 2, 16, 16, 14);
	public static final VoxelShape SHAPE_Z = box(2, 0, 0, 14, 16, 16);
	public static final BooleanProperty RUNNING = BooleanProperty.create("running");
	public final boolean advanced;

	public RecyclerBlock(boolean a) {
		super(Block.Properties.of(Material.METAL).strength(5F, 6F).sound(SoundType.METAL).dynamicShape().noOcclusion());
		registerDefaultState(stateDefinition.any()
				.setValue(RUNNING, false)
				.setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.SOUTH)
				.setValue(BlockStateProperties.WATERLOGGED, false)
		);

		advanced = a;
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		RecyclerEntity e = new RecyclerEntity();
		e.advanced = advanced;
		return e;
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader level, BlockPos pos, ISelectionContext context) {
		return state.getValue(BlockStateProperties.HORIZONTAL_FACING).getAxis() == Direction.Axis.Z ? SHAPE_X : SHAPE_Z;
	}

	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(RUNNING, BlockStateProperties.HORIZONTAL_FACING, BlockStateProperties.WATERLOGGED);
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		FluidState lv = context.getLevel().getFluidState(context.getClickedPos());
		return defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, context.getHorizontalDirection()).setValue(BlockStateProperties.WATERLOGGED, lv.getType() == Fluids.WATER);
	}

	@Override
	@Deprecated
	public BlockState rotate(BlockState state, Rotation rotation) {
		return state.setValue(BlockStateProperties.HORIZONTAL_FACING, rotation.rotate(state.getValue(BlockStateProperties.HORIZONTAL_FACING)));
	}

	@Override
	@Deprecated
	public BlockState mirror(BlockState state, Mirror mirror) {
		return state.rotate(mirror.getRotation(state.getValue(BlockStateProperties.HORIZONTAL_FACING)));
	}

	@Override
	@Deprecated
	public ActionResultType use(BlockState state, World level, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
		if (!level.isClientSide()) {
			TileEntity entity = level.getBlockEntity(pos);

			if (entity instanceof RecyclerEntity) {
				NetworkHooks.openGui((ServerPlayerEntity) player, new INamedContainerProvider() {
					@Override
					public ITextComponent getDisplayName() {
						return new TranslationTextComponent(advanced ? "block.recycler.advanced_recycler" : "block.recycler.recycler");
					}

					@Override
					public Container createMenu(int id, PlayerInventory playerInv, PlayerEntity player1) {
						return new RecyclerMenu(id, playerInv, (RecyclerEntity) entity);
					}
				}, pos);
			}
		}

		return ActionResultType.SUCCESS;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState state, World level, BlockPos p, Random random) {
		if (state.getValue(RUNNING)) {
			for (int i = 0; i < 10; i++) {
				float r = random.nextFloat() * 0.2F;
				float g = random.nextFloat() * 0.2F;
				float b = random.nextFloat() * 0.2F;
				double x = p.getX() + 0.2D + random.nextFloat() * 0.6D;
				double y = p.getY() + 1D;
				double z = p.getZ() + 0.2D + random.nextFloat() * 0.6D;
//				level.addParticle(new DustParticleOptions(r, g, b, 1F), x, y, z, 0D, 0D, 0D);
			}
		}
	}

	@Override
	@Deprecated
	public FluidState getFluidState(BlockState arg) {
		return arg.getValue(BlockStateProperties.WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(arg);
	}

	@Override
	@Deprecated
	public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, IWorld level, BlockPos pos, BlockPos neighborPos) {
		if (state.getValue(BlockStateProperties.WATERLOGGED)) {
			level.getLiquidTicks().scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
		}

		return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
	}

	public static void start(BlockState state, World level, BlockPos pos) {
		if (!state.getValue(RUNNING)) {
			TileEntity entity = level.getBlockEntity(pos);

			if (entity instanceof RecyclerEntity) {
				int nextTime = ((RecyclerEntity) entity).getNextTime();

				if (nextTime > 0) {
					level.setBlock(pos, state.setValue(RUNNING, true), Constants.BlockFlags.DEFAULT_AND_RERENDER);
					level.getBlockTicks().scheduleTick(pos, state.getBlock(), nextTime);
				}
			}
		}
	}

	public static void stop(BlockState state, World level, BlockPos pos) {
		if (state.getValue(RUNNING)) {
			level.setBlock(pos, state.setValue(RUNNING, false), Constants.BlockFlags.DEFAULT_AND_RERENDER);
		}
	}

	@Override
	public void randomTick(BlockState state, ServerWorld level, BlockPos pos, Random random) {
	}

	@Override
	@Deprecated
	public void tick(BlockState state, ServerWorld level, BlockPos pos, Random random) {
		TileEntity entity = level.getBlockEntity(pos);

		if (entity instanceof RecyclerEntity) {
			int nextTime = ((RecyclerEntity) entity).recycle();

			if (nextTime > 0) {
				level.getBlockTicks().scheduleTick(pos, state.getBlock(), nextTime);
			} else {
				stop(state, level, pos);
			}
		} else {
			stop(state, level, pos);
		}
	}

	@Override
	@Deprecated
	public void onRemove(BlockState state, World level, BlockPos pos, BlockState newState, boolean b) {
		if (!state.is(newState.getBlock()) && !level.isClientSide()) {
			TileEntity entity = level.getBlockEntity(pos);

			if (entity instanceof RecyclerEntity) {
				RecyclerEntity r = (RecyclerEntity) entity;

				for (int i = 0; i < r.input.getSlots(); i++) {
					popResource(level, pos, r.input.getStackInSlot(i));
				}

				for (int i = 0; i < r.output.getSlots(); i++) {
					popResource(level, pos, r.output.getStackInSlot(i));
				}
			}
		}

		super.onRemove(state, level, pos, newState, b);
	}
}
