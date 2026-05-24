package hellfirepvp.astralsorcery.common.event;

public class EventFlags
{
    public static BooleanFlag SWEEP_ATTACK;
    public static BooleanFlag LIGHTNING_ARC;
    public static BooleanFlag MANTLE_DISCIDIA_ADDED;
    public static BooleanFlag CHAIN_MINING;
    public static BooleanFlag MINING_SIZE_BREAK;
    public static BooleanFlag CHECK_BREAK_SPEED;
    public static BooleanFlag CHECK_UNDERWATER_BREAK_SPEED;
    public static BooleanFlag PLAY_BLOCK_BREAK_EFFECTS;
    public static BooleanFlag SKY_RENDERING;
    public static BooleanFlag GUI_CLOSING;
    public static BooleanFlag CAN_HAVE_DYN_ENCHANTMENTS;
    
    static {
        EventFlags.SWEEP_ATTACK = new BooleanFlag(false);
        EventFlags.LIGHTNING_ARC = new BooleanFlag(false);
        EventFlags.MANTLE_DISCIDIA_ADDED = new BooleanFlag(false);
        EventFlags.CHAIN_MINING = new BooleanFlag(false);
        EventFlags.MINING_SIZE_BREAK = new BooleanFlag(false);
        EventFlags.CHECK_BREAK_SPEED = new BooleanFlag(false);
        EventFlags.CHECK_UNDERWATER_BREAK_SPEED = new BooleanFlag(false);
        EventFlags.PLAY_BLOCK_BREAK_EFFECTS = new BooleanFlag(false);
        EventFlags.SKY_RENDERING = new BooleanFlag(false);
        EventFlags.GUI_CLOSING = new BooleanFlag(false);
        EventFlags.CAN_HAVE_DYN_ENCHANTMENTS = new BooleanFlag(false);
    }
    
    public static class BooleanFlag
    {
        private final boolean originalState;
        private boolean flag;
        
        private BooleanFlag(final boolean flag) {
            this.flag = flag;
            this.originalState = flag;
        }
        
        public boolean isSet() {
            return this.originalState != this.flag;
        }
        
        public synchronized void executeWithFlag(final Runnable run) {
            if (this.originalState == this.flag) {
                this.flag = !this.flag;
                try {
                    run.run();
                }
                finally {
                    this.flag = !this.flag;
                }
            }
        }
    }
}
