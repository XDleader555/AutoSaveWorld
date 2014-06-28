/**
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *
 */

package autosaveworld.threads.purge.weregen.nms;

import net.minecraft.server.v1_7_R3.Block;
import net.minecraft.server.v1_7_R3.TileEntity;
import net.minecraft.server.v1_7_R3.Chunk;
import net.minecraft.server.v1_7_R3.WorldServer;

import org.bukkit.World;
import org.bukkit.craftbukkit.v1_7_R3.util.LongHash;
import org.bukkit.craftbukkit.v1_7_R3.CraftWorld;

import autosaveworld.threads.purge.weregen.NMSWorldEditRegeneration.NMSBlock;

import com.sk89q.worldedit.Vector;

public class NMS17R3Access implements NMSAccess {

	@Override
	public Object generateNMSChunk(World world, int cx, int cz) {
		WorldServer nmsWorld = ((CraftWorld)world).getHandle();
		return nmsWorld.chunkProviderServer.chunkProvider.getOrCreateChunk(cx, cz);
	}

	@Override
	public void loadChunk(World world, int cx, int cz, Object nmsChunk) {
		WorldServer nmsWorld = ((CraftWorld)world).getHandle();
		nmsWorld.chunkProviderServer.chunks.put(LongHash.toLong(cx, cz), (Chunk) nmsChunk);
	}

	@Override
	public void setTileEntity(World world, Vector pt, Object tileEntity) {
		WorldServer nmsWorld = ((CraftWorld)world).getHandle();
		nmsWorld.setTileEntity(pt.getBlockX(), pt.getBlockY(), pt.getBlockZ(), (TileEntity) tileEntity);
	}

	@Override
	public NMSBlock getBlock(Object nmsChunk, Vector pt) {
		Chunk chunk = (Chunk) nmsChunk;
		return new NMSBlock(
			Block.b(chunk.getType(pt.getBlockX() & 0xF, pt.getBlockY(), pt.getBlockZ() & 0xF)),
			(byte) chunk.getData(pt.getBlockX() & 0xF, pt.getBlockY(), pt.getBlockZ() & 0xF),
			chunk.world.getTileEntity(pt.getBlockX(), pt.getBlockY(), pt.getBlockZ())
		);
	}

}
