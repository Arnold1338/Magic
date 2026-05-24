package hellfirepvp.astralsorcery.common.entity.goal;

import java.util.EnumSet;
import hellfirepvp.astralsorcery.common.entity.EntitySpectralTool;
import net.minecraft.world.entity.ai.goal.Goal;

public abstract class SpectralToolGoal extends Goal
{
    private final EntitySpectralTool entity;
    private final double speed;
    protected int actionCooldown;
    
    public SpectralToolGoal(final EntitySpectralTool entity, final double speed) {
        this.actionCooldown = 0;
        this.entity = entity;
        this.speed = speed;
        this.func_220684_a((EnumSet)EnumSet.of(Goal.Flag.MOVE, Goal.Flag.TARGET, Goal.Flag.LOOK));
    }
    
    public EntitySpectralTool getEntity() {
        return this.entity;
    }
    
    public double getSpeed() {
        return this.speed;
    }
}
