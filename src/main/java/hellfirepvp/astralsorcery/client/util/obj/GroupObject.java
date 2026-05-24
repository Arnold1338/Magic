package hellfirepvp.astralsorcery.client.util.obj;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import java.util.Iterator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.ArrayList;

public class GroupObject
{
    public String name;
    public ArrayList<Face> faces;
    
    public GroupObject() {
        this("");
    }
    
    public GroupObject(final String name) {
        this.faces = new ArrayList<Face>();
        this.name = name;
    }
    
    @OnlyIn(Dist.CLIENT)
    public void render(final VertexConsumer vb) {
        if (this.faces.size() > 0) {
            for (final Face face : this.faces) {
                face.addFaceForRender(vb);
            }
        }
    }
}
