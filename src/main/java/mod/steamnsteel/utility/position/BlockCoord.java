/*
 * Copyright (c) 2014 Rosie Alexander and Scott Killen.
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option) any
 * later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, see <http://www.gnu.org/licenses>.
 */

package mod.steamnsteel.utility.position;

import com.google.common.base.Objects;
import org.apache.commons.lang3.tuple.ImmutableTriple;

public class BlockCoord implements Comparable<BlockCoord>
{
    private final ImmutableTriple<Integer, Integer, Integer> data;

    public BlockCoord(int x, int y, int z) { data = ImmutableTriple.of(x, y, z); }

    public int getX() { return data.left; }

    public int getY() { return data.middle; }

    public int getZ() { return data.right; }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final BlockCoord that = (BlockCoord) o;
        return data.left.equals(that.data.left)
                && data.middle.equals(that.data.middle)
                && data.right.equals(that.data.right);
    }

    @Override
    public int hashCode()
    {
        return Objects.hashCode(data.left, data.middle, data.right);
    }

    @Override
    public int compareTo(BlockCoord o)
    {
        if (data.left.equals(o.data.left)) return data.middle.equals(o.data.middle)
                ? data.right.compareTo(o.data.right)
                : data.middle.compareTo(o.data.middle);

        else return data.left.compareTo(o.data.left);
    }

    @Override
    public String toString()
    {
        return Objects.toStringHelper(this)
                .add("X", data.left)
                .add("Y", data.middle)
                .add("Z", data.right)
                .toString();
    }
}