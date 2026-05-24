package hellfirepvp.astralsorcery.client.util.obj;

import hellfirepvp.astralsorcery.common.util.data.Vector3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import com.mojang.blaze3d.vertex.IVertexBuilder;

public class Face
{
    Vertex[] vertices;
    Vertex[] vertexNormals;
    Vertex faceNormal;
    TextureCoordinate[] textureCoordinates;
    
    @OnlyIn(Dist.CLIENT)
    void addFaceForRender(final IVertexBuilder vb) {
        this.addFaceForRender(vb, 4.0E-4f);
    }
    
    @OnlyIn(Dist.CLIENT)
    void addFaceForRender(final IVertexBuilder vb, final float textureOffset) {
        float averageU = 0.0f;
        float averageV = 0.0f;
        for (final TextureCoordinate textureCoordinate : this.textureCoordinates) {
            averageU += textureCoordinate.u;
            averageV += textureCoordinate.v;
        }
        averageU /= this.textureCoordinates.length;
        averageV /= this.textureCoordinates.length;
        for (int i = 0; i < this.vertices.length; ++i) {
            float offsetU = textureOffset;
            float offsetV = textureOffset;
            if (this.textureCoordinates[i].u > averageU) {
                offsetU = -offsetU;
            }
            if (this.textureCoordinates[i].v > averageV) {
                offsetV = -offsetV;
            }
            vb.func_225582_a_((double)this.vertices[i].x, (double)this.vertices[i].y, (double)this.vertices[i].z).func_225586_a_(255, 255, 255, 255).func_225583_a_(this.textureCoordinates[i].u + offsetU, this.textureCoordinates[i].v + offsetV).func_225584_a_(this.faceNormal.x, this.faceNormal.y, this.faceNormal.z).func_181675_d();
        }
    }
    
    Vertex calculateFaceNormal() {
        final Vector3 v1 = new Vector3(this.vertices[1].x - this.vertices[0].x, this.vertices[1].y - this.vertices[0].y, this.vertices[1].z - this.vertices[0].z);
        final Vector3 v2 = new Vector3(this.vertices[2].x - this.vertices[0].x, this.vertices[2].y - this.vertices[0].y, this.vertices[2].z - this.vertices[0].z);
        final Vector3 normalVector = v1.crossProduct(v2).normalize();
        return new Vertex((float)normalVector.getX(), (float)normalVector.getY(), (float)normalVector.getZ());
    }
}
