package hellfirepvp.astralsorcery.client.util;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import hellfirepvp.astralsorcery.common.util.data.Vector3;

public class SphereBuilder
{
    public static List<TriangleFace> buildFaces(final Vector3 axis, final int fractionsSplit, final int fractionsCircle) {
        final List<TriangleFace> sphereFaces = new ArrayList<TriangleFace>();
        final Vector3 centerPerp = axis.clone().perpendicular();
        final double degSplit = 180.0 / fractionsSplit;
        final double degCircleSplit = 360.0 / fractionsCircle;
        final double degCircleOffsetShifted = degCircleSplit / 2.0;
        boolean shift = false;
        Vector3[] prevArray = new Vector3[fractionsCircle];
        final Vector3 prev = axis.clone();
        Arrays.fill(prevArray, prev.clone());
        for (int i = 1; i <= fractionsSplit; ++i) {
            final Vector3 splitVec = axis.clone().rotate(Math.toRadians(degSplit * i), centerPerp);
            final Vector3[] circlePositions = new Vector3[fractionsCircle];
            for (int j = 0; j < fractionsCircle; ++j) {
                double deg = shift ? degCircleOffsetShifted : 0.0;
                deg += degCircleSplit * j;
                circlePositions[j] = splitVec.clone().rotate(Math.toRadians(deg), axis);
            }
            for (int k = 0; k < fractionsCircle; ++k) {
                int prevIndex = shift ? k : (k - 1);
                if (prevIndex < 0) {
                    prevIndex = fractionsCircle - 1;
                }
                int nextIndex = shift ? (k + 1) : k;
                if (nextIndex >= fractionsCircle) {
                    nextIndex = 0;
                }
                sphereFaces.add(new TriangleFace(prevArray[prevIndex], prevArray[nextIndex], circlePositions[k]));
                int nextCircle = k + 1;
                if (nextCircle >= fractionsCircle) {
                    nextCircle = 0;
                }
                sphereFaces.add(new TriangleFace(circlePositions[k], prevArray[nextIndex], circlePositions[nextCircle]));
            }
            prevArray = circlePositions;
            shift = !shift;
        }
        return sphereFaces;
    }
    
    public static class TriangleFace
    {
        private Vector3 v1;
        private Vector3 v2;
        private Vector3 v3;
        
        private TriangleFace(final Vector3 v1, final Vector3 v2, final Vector3 v3) {
            this.v1 = v1;
            this.v2 = v2;
            this.v3 = v3;
        }
        
        public Vector3 getV1() {
            return this.v1;
        }
        
        public Vector3 getV2() {
            return this.v2;
        }
        
        public Vector3 getV3() {
            return this.v3;
        }
    }
}
