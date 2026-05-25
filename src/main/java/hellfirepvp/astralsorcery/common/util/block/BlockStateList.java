package hellfirepvp.astralsorcery.common.util.block;

import com.mojang.datafixers.kinds.Applicative;
import java.util.Collection;
import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import java.util.Iterator;
import java.util.function.Consumer;
import java.util.function.Function;
import hellfirepvp.astralsorcery.common.data.config.base.ConfiguredBlockStateList;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraft.world.level.block.Block;
import java.util.ArrayList;
import java.util.List;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.block.state.BlockState;
import java.util.function.Predicate;

public class BlockStateList implements BlockPredicate, Predicate<BlockState>
{
    public static final Codec<BlockStateList> CODEC;
    private final List<SimpleBlockPredicate> configuredMatches;
    
    public BlockStateList() {
        this.configuredMatches = new ArrayList<SimpleBlockPredicate>();
    }
    
    public BlockStateList add(final BlockState... states) {
        this.configuredMatches.add(new SimpleBlockPredicate(states));
        return this;
    }
    
    public BlockStateList add(final Block block) {
        this.configuredMatches.add(new SimpleBlockPredicate(block));
        return this;
    }
    
    public ConfiguredBlockStateList getAsConfig(final ForgeConfigSpec.Builder cfgBuilder, final String key, final String translationKey, final String comment) {
        final List<String> out = new ArrayList<String>();
        this.configuredMatches.stream().map((Function<? super Object, ?>)SimpleBlockPredicate::getAsConfigList).forEach((Consumer<? super Object>)out::addAll);
        return new ConfiguredBlockStateList((ForgeConfigSpec.ConfigValue<List<String>>)cfgBuilder.comment(comment).translation(translationKey).define(key, (Object)out));
    }
    
    public static BlockStateList fromConfig(final List<String> serializedBlockPredicates) {
        final BlockStateList list = new BlockStateList();
        for (final String str : serializedBlockPredicates) {
            final SimpleBlockPredicate predicate = SimpleBlockPredicate.fromConfig(str);
            if (predicate != null) {
                list.configuredMatches.add(predicate);
            }
        }
        return list;
    }
    
    @Override
    public boolean test(final BlockState state) {
        return this.configuredMatches.stream().anyMatch(predicate -> predicate.test(state));
    }
    
    @Override
    public boolean test(final Level world, final BlockPos pos, final BlockState state) {
        return this.configuredMatches.stream().anyMatch(predicate -> predicate.test(world, pos, state));
    }
    
    static {
        CODEC = RecordCodecBuilder.create(codecBuilder -> codecBuilder.group((App)BlockState.field_235877_b_.listOf().fieldOf("blockStates").forGetter(stateList -> {
            final ArrayList<BlockState> applicable = new ArrayList<BlockState>();
            stateList.configuredMatches.forEach(predicate -> predicate.validMatch.ifLeft((Consumer)applicable::addAll).ifRight(block -> applicable.addAll((Collection)block.func_176194_O().func_177619_a())));
            return applicable;
        })).apply((Applicative)codecBuilder, states -> {
            final BlockStateList list = new BlockStateList();
            states.forEach(xva$0 -> rec$.add(xva$0));
            return (List<BlockState>)list;
        }));
    }
}
