import at.pcgamingfreaks.MarriageMaster.Bukkit.API.MarriageMasterPlugin;
import at.pcgamingfreaks.MarriageMaster.Bukkit.API.MarriageManager;
import at.pcgamingfreaks.MarriageMaster.API.MarriagePlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.plugin.Plugin;

public class MarriageCompatibility implements Compatibility {

    private final MarriageMasterPlugin marriageMaster;

    public MarriageCompatibility() {
        Plugin plugin = Bukkit.getPluginManager().getPlugin("MarriageMaster");
        if (plugin instanceof MarriageMasterPlugin) {
            this.marriageMaster = (MarriageMasterPlugin) plugin;
            Bukkit.getLogger().info("[RealisticVillagers] MarriageMaster detected and hooked.");
        } else {
            this.marriageMaster = null;
        }
    }

    public boolean isMarried(Player player) {
        if (marriageMaster == null) return false;
        MarriageManager manager = marriageMaster.getMarriageManager();

        // Get the MarriagePlayer object from the manager
        MarriagePlayer marriagePlayer = manager.getMarriagePlayer(player.getUniqueId());

        // If marriagePlayer is null, the player is not registered in the system
        return marriagePlayer != null && marriagePlayer.isMarried();
    }

    public boolean isDivorced(Player player) {
        return !isMarried(player); 
    }

    @Override
    public boolean shouldTrack(Villager villager) {
        return true;
    }
}
