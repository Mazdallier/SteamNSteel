package mod.steamnsteel.utility.world;

import net.minecraft.block.Block;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class WorldRaytraceIterator
{
    private final World world;
    private final Vec3 startLocation;
    private final Vec3 direction;
    private final MovingObjectPosition currentBlock;
    private int currentLocationX;
    private int currentLocationY;
    private int currentLocationZ;
    private int locationX;
    private int locationY;
    private int locationZ;
    private boolean valid;
    private int blockLimit;

    public WorldRaytraceIterator(World world, Vec3 location, Vec3 direction)
    {
        this.world = world;
        this.startLocation = location;
        this.direction = direction;


        if (Double.isNaN(location.xCoord) || Double.isNaN(location.yCoord) || Double.isNaN(location.zCoord))
        {
            valid = false;
        }
        if (Double.isNaN(direction.xCoord) || Double.isNaN(direction.yCoord) || Double.isNaN(direction.zCoord))
        {
            valid = false;
        }

        currentLocationX = MathHelper.floor_double(direction.xCoord);
        currentLocationY = MathHelper.floor_double(direction.yCoord);
        currentLocationZ = MathHelper.floor_double(direction.zCoord);
        locationX = MathHelper.floor_double(location.xCoord);
        locationY = MathHelper.floor_double(location.yCoord);
        locationZ = MathHelper.floor_double(location.zCoord);

        blockLimit = 200;

        currentBlock = getInitialBlock();
        if (currentBlock == null) valid = false;
    }

    private MovingObjectPosition getInitialBlock() {

        boolean p_147447_3_ = false;
        boolean p_147447_4_ = false;

        Block block = world.getBlock(locationX, locationY, locationZ);
        int metadata = world.getBlockMetadata(locationX, locationY, locationZ);
        MovingObjectPosition movingobjectposition = null;
        if ((!p_147447_4_ || block.getCollisionBoundingBoxFromPool(world, locationX, locationY, locationZ) != null) && block.canCollideCheck(metadata, p_147447_3_))
        {
            movingobjectposition = block.collisionRayTrace(world, locationX, locationY, locationZ, startLocation, direction);
        }

        return movingobjectposition;
    }

    public MovingObjectPosition findNextBlock()
    {
        boolean p_147447_3_ = false;
        boolean p_147447_4_ = false;
        boolean p_147447_5_ = false;

        MovingObjectPosition movingobjectposition2 = null;

        while (blockLimit-- >= 0)
        {
            if (Double.isNaN(startLocation.xCoord) || Double.isNaN(startLocation.yCoord) || Double.isNaN(startLocation.zCoord))
            {
                return null;
            }

            if (locationX == currentLocationX && locationY == currentLocationY && locationZ == currentLocationZ)
            {
                return p_147447_5_ ? movingobjectposition2 : null;
            }

            boolean movingInDirectionX = true;
            boolean movingInDirectionY = true;
            boolean movingInDirectionZ = true;
            double d0 = 999.0D;
            double d1 = 999.0D;
            double d2 = 999.0D;

            if (currentLocationX > locationX)
            {
                d0 = (double) locationX + 1.0D;
            }
            else if (currentLocationX < locationX)
            {
                d0 = (double) locationX;
            }
            else
            {
                movingInDirectionX = false;
            }

            if (currentLocationY > locationY)
            {
                d1 = (double) locationY + 1.0D;
            }
            else if (currentLocationY < locationY)
            {
                d1 = (double) locationY;
            }
            else
            {
                movingInDirectionY = false;
            }

            if (currentLocationZ > locationZ)
            {
                d2 = (double) locationZ + 1.0D;
            }
            else if (currentLocationZ < locationZ)
            {
                d2 = (double) locationZ;
            }
            else
            {
                movingInDirectionZ = false;
            }

            double d3 = 999.0D;
            double d4 = 999.0D;
            double d5 = 999.0D;
            double d6 = direction.xCoord - startLocation.xCoord;
            double d7 = direction.yCoord - startLocation.yCoord;
            double d8 = direction.zCoord - startLocation.zCoord;

            if (movingInDirectionX)
            {
                d3 = (d0 - startLocation.xCoord) / d6;
            }

            if (movingInDirectionY)
            {
                d4 = (d1 - startLocation.yCoord) / d7;
            }

            if (movingInDirectionZ)
            {
                d5 = (d2 - startLocation.zCoord) / d8;
            }

            byte hitSide;

            if (d3 < d4 && d3 < d5)
            {
                hitSide = currentLocationX > locationX ? (byte)4 : 5;

                startLocation.xCoord = d0;
                startLocation.yCoord += d7 * d3;
                startLocation.zCoord += d8 * d3;
            }
            else if (d4 < d5)
            {
                hitSide = currentLocationY > locationY ? (byte)0 : 1;

                startLocation.xCoord += d6 * d4;
                startLocation.yCoord = d1;
                startLocation.zCoord += d8 * d4;
            }
            else
            {
                hitSide = currentLocationZ > locationZ ? (byte)2 : 3;

                startLocation.xCoord += d6 * d5;
                startLocation.yCoord += d7 * d5;
                startLocation.zCoord = d2;
            }

            locationX = (int) (double)MathHelper.floor_double(startLocation.xCoord);
            locationY = (int) (double)MathHelper.floor_double(startLocation.yCoord);
            locationZ = (int) (double)MathHelper.floor_double(startLocation.zCoord);

            switch (hitSide) {
                case 5:
                    --locationX;
                    break;
                case 1:
                    --locationY;
                    break;
                case 3:
                    --locationZ;
                    break;
            }

            Block block = world.getBlock(locationX, locationY, locationZ);
            int metadata = world.getBlockMetadata(locationX, locationY, locationZ);

            if (!p_147447_4_ || block.getCollisionBoundingBoxFromPool(world, locationX, locationY, locationZ) != null)
            {
                if (block.canCollideCheck(metadata, p_147447_3_))
                {
                    MovingObjectPosition movingobjectposition1 = block.collisionRayTrace(world, locationX, locationY, locationZ, startLocation, direction);

                    if (movingobjectposition1 != null)
                    {
                        return movingobjectposition1;
                    }
                }
                else
                {
                    movingobjectposition2 = new MovingObjectPosition(locationX, locationY, locationZ, hitSide, startLocation, false);
                }
            }
        }

        return p_147447_5_ ? movingobjectposition2 : null;
    }
}
