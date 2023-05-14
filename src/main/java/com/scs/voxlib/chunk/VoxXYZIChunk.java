package com.scs.voxlib.chunk;

import com.scs.voxlib.GridPoint3;
import com.scs.voxlib.StreamUtils;
import com.scs.voxlib.Voxel;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;

public final class VoxXYZIChunk extends VoxChunk {
	
    private final Voxel[] voxels;

    public VoxXYZIChunk(int voxelCount) {
        super(ChunkFactory.XYZI);
        voxels = new Voxel[voxelCount];
    }

    public VoxXYZIChunk(Collection<Voxel> voxels) {
        super(ChunkFactory.XYZI);
        this.voxels = new Voxel[voxels.size()];
        voxels.toArray(this.voxels);
    }

    public static VoxXYZIChunk read(InputStream stream) throws IOException {
        int voxelCount = StreamUtils.readIntLE(stream);
        VoxXYZIChunk chunk = new VoxXYZIChunk(voxelCount);
        //System.out.println(voxelCount + " voxels");

        for (int i = 0; i < voxelCount; i++) {
            GridPoint3 position = StreamUtils.readVector3b(stream);
            byte colorIndex = (byte) ((byte)stream.read() & 0xff);
            chunk.voxels[i] = new Voxel(position, colorIndex);
        }
        return chunk;
    }

    public Voxel[] getVoxels() {
        return voxels;
    }

    @Override
    protected void writeContent(OutputStream stream) throws IOException {
        StreamUtils.writeIntLE(voxels.length, stream);
        for (Voxel voxel : voxels) {
            StreamUtils.writeVector3b(voxel.getPosition(), stream);
            stream.write(voxel.getColourIndex());
        }
    }
}
