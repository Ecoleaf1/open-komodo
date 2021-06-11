/* 		************************************
 * 		Use Cases:
 * 		Prevent ice from melting
 *		************************************
 */

package net.wigoftime.open_komodo.events;

import net.wigoftime.open_komodo.Main;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.plugin.EventExecutor;

public class BlockFade implements EventExecutor {

	@Override
	public void execute(Listener listener, Event event) throws EventException {
		BlockFadeEvent blockFadeEvent = (BlockFadeEvent) event;
		Block block = blockFadeEvent.getBlock();
		
		if (Main.iceMelts == true) return;
		
		if (block.getType() == Material.ICE) {
			blockFadeEvent.setCancelled(true);
			return;
		}
	}
	
}
