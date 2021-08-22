package dev.latvian.mods.recycler.block;

import dev.latvian.mods.recycler.block.entity.RecyclerEntity;
import dev.latvian.mods.recycler.gui.RecyclerMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

/**
 * @author LatvianModder
 */
public class RecyclerBlock extends Block implements SimpleWaterloggedBlock {
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
	public BlockEntity createTileEntity(BlockState state, BlockGetter world) {
		RecyclerEntity e = new RecyclerEntity();
		e.advanced = advanced;
		return e;
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return state.getValue(BlockStateProperties.HORIZONTAL_FACING).getAxis() == Direction.Axis.Z ? SHAPE_X : SHAPE_Z;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(RUNNING, BlockStateProperties.HORIZONTAL_FACING, BlockStateProperties.WATERLOGGED);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
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
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		if (!level.isClientSide()) {
			BlockEntity entity = level.getBlockEntity(pos);

			if (entity instanceof RecyclerEntity) {
				NetworkHooks.openGui((ServerPlayer) player, new MenuProvider() {
					@Override
					public Component getDisplayName() {
						return new TranslatableComponent(advanced ? "block.recycler.advanced_recycler" : "block.recycler.recycler");
					}

					@Override
					public AbstractContainerMenu createMenu(int id, Inventory playerInv, Player player1) {
						return new RecyclerMenu(id, playerInv, (RecyclerEntity) entity);
					}
				}, pos);
			}
		}

		return InteractionResult.SUCCESS;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState state, Level level, BlockPos p, Random random) {
		if (state.getValue(RUNNING)) {
			for (int i = 0; i < 10; i++) {
				float r = random.nextFloat() * 0.2F;
				float g = random.nextFloat() * 0.2F;
				float b = random.nextFloat() * 0.2F;
				double x = p.getX() + 0.2D + random.nextFloat() * 0.6D;
				double y = p.getY() + 1D;
				double z = p.getZ() + 0.2D + random.nextFloat() * 0.6D;
				level.addParticle(new DustParticleOptions(r, g, b, 1F), x, y, z, 0D, 0D, 0D);
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
	public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
		if (state.getValue(BlockStateProperties.WATERLOGGED)) {
			level.getLiquidTicks().scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
		}

		return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
	}

	public static void start(BlockState state, Level level, BlockPos pos) {
		if (!state.getValue(RUNNING)) {
			BlockEntity entity = level.getBlockEntity(pos);

			if (entity instanceof RecyclerEntity) {
				int nextTime = ((RecyclerEntity) entity).getNextTime();

				if (nextTime > 0) {
					level.setBlock(pos, state.setValue(RUNNING, true), Constants.BlockFlags.DEFAULT_AND_RERENDER);
					level.getBlockTicks().scheduleTick(pos, state.getBlock(), nextTime);
				}
			}
		}
	}

	public static void stop(BlockState state, Level level, BlockPos pos) {
		if (state.getValue(RUNNING)) {
			level.setBlock(pos, state.setValue(RUNNING, false), Constants.BlockFlags.DEFAULT_AND_RERENDER);
		}
	}

	@Override
	public void randomTick(BlockState state, ServerLevel level, BlockPos pos, Random random) {
	}

	@Override
	@Deprecated
	public void tick(BlockState state, ServerLevel level, BlockPos pos, Random random) {
		BlockEntity entity = level.getBlockEntity(pos);

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
	public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean b) {
		if (!state.is(newState.getBlock()) && !level.isClientSide()) {
			BlockEntity entity = level.getBlockEntity(pos);

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
