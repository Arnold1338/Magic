package hellfirepvp.astralsorcery.common.tile;

import net.minecraft.world.level.LightType;
import net.minecraft.world.level.level.Level;
import hellfirepvp.astralsorcery.common.util.block.BlockPredicate;
import net.minecraft.nbt.CompoundTag;
import hellfirepvp.astralsorcery.common.item.wand.ItemIlluminationWand;
import net.minecraft.world.level.item.ItemStack;
import net.minecraft.world.level.level.LevelReader;
import hellfirepvp.astralsorcery.common.entity.EntityFlare;
import net.minecraft.world.level.block.state.Property;
import hellfirepvp.astralsorcery.common.block.tile.BlockFlareLight;
import hellfirepvp.astralsorcery.common.lib.BlocksAS;
import net.minecraft.world.level.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import java.util.Iterator;
import java.util.Collection;
import hellfirepvp.astralsorcery.client.effect.function.VFXColorFunction;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import java.awt.Color;
import hellfirepvp.astralsorcery.common.util.ColorUtils;
import net.minecraft.world.level.level.block.entity.BlockEntity;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import hellfirepvp.astralsorcery.client.effect.handler.EffectHelper;
import hellfirepvp.astralsorcery.client.lib.EffectTemplatesAS;
import hellfirepvp.astralsorcery.client.effect.vfx.FXFacingParticle;
import net.minecraft.core.Direction;
import java.util.ArrayList;
import net.minecraft.world.level.level.block.entity.BlockEntityType;
import hellfirepvp.astralsorcery.common.lib.TileEntityTypesAS;
import net.minecraft.world.item.DyeColor;
import net.minecraft.core.BlockPos;
import java.util.List;
import hellfirepvp.astralsorcery.common.tile.base.TileEntityTick;

public class TileIlluminator extends TileEntityTick
{
    public static final LightCheck ILLUMINATOR_CHECK;
    public static final int SEARCH_RADIUS = 64;
    public static final int STEP_WIDTH = 2;
    private List<List<BlockPos>> layerPositions;
    private boolean doRecalculation;
    private int ticksUntilNextPlacement;
    private boolean playerPlaced;
    private int boostedTicks;
    private DyeColor color;
    
    public TileIlluminator() {
        super(TileEntityTypesAS.ILLUMINATOR);
        this.layerPositions = null;
        this.doRecalculation = false;
        this.ticksUntilNextPlacement = 180;
        this.playerPlaced = false;
        this.boostedTicks = 0;
        this.color = DyeColor.YELLOW;
    }
    
    @Override
    public void func_73660_a() {
        super.func_73660_a();
        if (!this.isPlayerPlaced()) {
            return;
        }
        if (!this.field_145850_b.func_201670_d()) {
            if (this.layerPositions == null) {
                this.recalculate();
            }
            this.placeFlare();
            this.placeFlare();
            this.placeFlare();
            if (TileIlluminator.rand.nextInt(3) == 0 && this.placeFlare()) {
                this.doRecalculation = true;
            }
            if (this.boostedTicks > 0) {
                --this.boostedTicks;
            }
            --this.ticksUntilNextPlacement;
            if (this.ticksUntilNextPlacement <= 0) {
                this.ticksUntilNextPlacement = ((this.boostedTicks > 0) ? 30 : 180);
                if (this.doRecalculation) {
                    this.doRecalculation = false;
                    this.recalculate();
                }
            }
        }
        if (this.field_145850_b.func_201670_d()) {
            this.tickEffects();
        }
    }
    
    private void recalculate() {
        final int height = Math.max(0, this.func_174877_v().getY() - 7);
        final int parts = height / 7;
        this.layerPositions = new ArrayList<List<BlockPos>>(parts);
        for (int i = 0; i < parts; ++i) {
            final int yPart = 3 + i * 7;
            final List<BlockPos> positions = new ArrayList<BlockPos>();
            this.generatePositions(positions, new BlockPos(this.func_174877_v().getX(), yPart, this.func_174877_v().getZ()));
            this.layerPositions.add(positions);
        }
    }
    
    private void generatePositions(final List<BlockPos> positions, final BlockPos center) {
        final int xPos = center.getX();
        final int yPos = center.getY();
        final int zPos = center.getZ();
        BlockPos currentPos = center;
        if (!positions.contains(currentPos)) {
            positions.add(currentPos);
        }
        Direction dir = Direction.NORTH;
        while (Math.abs(currentPos.getX() - xPos) <= 64 && Math.abs(currentPos.getY() - yPos) <= 64 && Math.abs(currentPos.getZ() - zPos) <= 64) {
            currentPos = currentPos.func_177967_a(dir, 2);
            if (!positions.contains(currentPos)) {
                positions.add(currentPos);
            }
            final Direction tryDirNext = dir.func_176746_e();
            if (!positions.contains(currentPos.func_177967_a(tryDirNext, 2))) {
                dir = tryDirNext;
            }
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    private void tickEffects() {
        if (!this.doesSeeSky() && this.boostedTicks <= 0) {
            return;
        }
        FXFacingParticle p = EffectHelper.of(EffectTemplatesAS.GENERIC_PARTICLE).spawn(new Vector3(this).add(0.5, 0.5, 0.5)).setScaleMultiplier(0.25f).setMotion(new Vector3(TileIlluminator.rand.nextFloat() * 0.025f * (TileIlluminator.rand.nextBoolean() ? 1 : -1), TileIlluminator.rand.nextFloat() * 0.025f * (TileIlluminator.rand.nextBoolean() ? 1 : -1), TileIlluminator.rand.nextFloat() * 0.025f * (TileIlluminator.rand.nextBoolean() ? 1 : -1)));
        final Color c = ColorUtils.flareColorFromDye(this.getColor());
        p.color(VFXColorFunction.constant(MiscUtils.eitherOf(TileIlluminator.rand, new Color[] { Color.WHITE, c.brighter().brighter(), c })));
        if (this.boostedTicks > 0 && this.ticksExisted % 4 == 0) {
            final Collection<Vector3> positions = MiscUtils.getCirclePositions(new Vector3(this).add(0.5, 0.5, 0.5), Vector3.RotAxis.Y_AXIS, 0.8f + TileIlluminator.rand.nextFloat() * 0.1f, 20 + TileIlluminator.rand.nextInt(10));
            for (final Vector3 v : positions) {
                p = EffectHelper.of(EffectTemplatesAS.GENERIC_PARTICLE).spawn(v).setScaleMultiplier(0.15f).setMotion(new Vector3(0.0, (TileIlluminator.rand.nextBoolean() ? 1 : -1) * TileIlluminator.rand.nextFloat() * 0.01, 0.0));
                p.color(VFXColorFunction.constant(MiscUtils.eitherOf(TileIlluminator.rand, new Color[] { Color.WHITE, c.brighter().brighter(), c })));
            }
        }
    }
    
    private boolean placeFlare() {
        boolean recalc = false;
        for (final List<BlockPos> list : this.layerPositions) {
            if (list.isEmpty()) {
                recalc = true;
            }
            else {
                final int index = TileIlluminator.rand.nextInt(list.size());
                BlockPos at = list.remove(index);
                if (!recalc && list.isEmpty()) {
                    recalc = true;
                }
                at = at.offset(TileIlluminator.rand.nextInt(5) - 2, TileIlluminator.rand.nextInt(13) - 6, TileIlluminator.rand.nextInt(5) - 2);
                MiscUtils.executeWithChunk((IWorldReader)this.field_145850_b, at, at, pos -> {
                    if (this.doesSeeSky() && TileIlluminator.ILLUMINATOR_CHECK.test(this.field_145850_b, pos, this.field_145850_b.getBlockState(pos))) {
                        final DyeColor color = this.getColor();
                        final BlockState toPlace = (BlockState)BlocksAS.FLARE_LIGHT.defaultBlockState().func_206870_a((Property)BlockFlareLight.COLOR, (Comparable)color);
                        if (this.field_145850_b.func_175656_a(pos, toPlace)) {
                            EntityFlare.spawnAmbientFlare(this.field_145850_b, this.func_174877_v());
                        }
                    }
                    return;
                });
            }
        }
        return recalc;
    }
    
    public void setColor(final DyeColor color) {
        this.color = color;
        this.markForUpdate();
    }
    
    public DyeColor getColor() {
        return this.color;
    }
    
    public void setPlayerPlaced(final boolean playerPlaced) {
        this.playerPlaced = playerPlaced;
        this.markForUpdate();
    }
    
    public boolean isPlayerPlaced() {
        return this.playerPlaced;
    }
    
    public void onWandUsed(final ItemStack stack) {
        this.boostedTicks = 12000;
        this.setColor(ItemIlluminationWand.getConfiguredColor(stack));
    }
    
    @Override
    public void writeCustomNBT(final CompoundTag compound) {
        super.writeCustomNBT(compound);
        compound.putBoolean("playerPlaced", this.playerPlaced);
        compound.putInt("color", this.color.func_196059_a());
        compound.putInt("boostedTicks", this.boostedTicks);
    }
    
    @Override
    public void readCustomNBT(final CompoundTag compound) {
        super.readCustomNBT(compound);
        this.playerPlaced = compound.getBoolean("playerPlaced");
        this.color = DyeColor.func_196056_a(compound.getInt("color"));
        this.boostedTicks = compound.getInt("boostedTicks");
    }
    
    static {
        ILLUMINATOR_CHECK = new LightCheck();
    }
    
    public static class LightCheck implements BlockPredicate
    {
        @Override
        public boolean test(final World world, final BlockPos pos, final BlockState state) {
            return world.isEmptyBlock(pos) && !MiscUtils.canSeeSky(world, pos, false, false) && world.func_201696_r(pos) < 8 && world.func_226658_a_(LightType.SKY, pos) < 4;
        }
    }
}
