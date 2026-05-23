package hellfirepvp.observerlib.client.preview;

import hellfirepvp.observerlib.api.structure.MatchableStructure;

public class StructureSnapshot {
    private final MatchableStructure structure;
    private final long snapshot;

    StructureSnapshot(MatchableStructure structure, long snapshot) {
        this.structure = structure;
        this.snapshot = snapshot;
    }

    MatchableStructure getStructure() { return structure; }
    long getSnapshotTick() { return snapshot; }
}
