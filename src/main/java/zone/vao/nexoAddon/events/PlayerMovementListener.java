package zone.vao.nexoAddon.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import zone.vao.nexoAddon.NexoAddon;
import zone.vao.nexoAddon.events.playerMovements.FurnituresRaytrace;

public class PlayerMovementListener implements Listener {

  @EventHandler
  public void onPlayerMove(PlayerMoveEvent event) {

    if(NexoAddon.getInstance().getGlobalConfig().getBoolean("boss_bar", true))
      FurnituresRaytrace.onFurnituresRaytrace(event);
  }
}
