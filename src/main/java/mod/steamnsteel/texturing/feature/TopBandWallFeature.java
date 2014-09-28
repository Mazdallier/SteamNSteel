package mod.steamnsteel.texturing.feature;

import mod.steamnsteel.texturing.*;
import mod.steamnsteel.utility.position.ChunkCoord;
import mod.steamnsteel.utility.position.WorldBlockCoord;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;
import java.util.Collection;

public class TopBandWallFeature extends ProceduralWallFeatureBase
{
    private final ProceduralConnectedTexture texture;

    public TopBandWallFeature(ProceduralConnectedTexture texture, String name, int layer) {

        super(name, layer);
        this.texture = texture;
    }

    @Override
    public boolean isFeatureValid(TextureContext context)
    {
        return false;
    }

    @Override
    public Collection<FeatureInstance> getFeatureAreasFor(ChunkCoord chunkCoord)
    {
        return null;
    }

    @Override
    public boolean canIntersect(IProceduralWallFeature feature)
    {
        return false;
    }

    @Override
    public long getSubProperties(TextureContext context)
    {
        return 0;
    }

    @Override
    public Behaviour getBehaviourAgainst(IProceduralWallFeature otherLayerFeature)
    {
        return null;
    }
}
