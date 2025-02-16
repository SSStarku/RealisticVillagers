package me.matsubara.realisticvillagers.listener;
import at.pcgamingfreaks.MarriageMaster.Bukkit.API.Events.KissEvent;
import at.pcgamingfreaks.MarriageMaster.Bukkit.API.MarriagePlayer;
import me.matsubara.realisticvillagers.files.Config;
import me.matsubara.realisticvillagers.task.BabyTaskPlayer;
import me.matsubara.realisticvillagers.util.PluginUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import at.pcgamingfreaks.MarriageMaster.Bukkit.API.MarriageMasterPlugin;
import at.pcgamingfreaks.MarriageMaster.Bukkit.API.Events.MarriedEvent;
import me.matsubara.realisticvillagers.RealisticVillagers;
import me.matsubara.realisticvillagers.entity.IVillagerNPC;
import me.matsubara.realisticvillagers.files.Messages;
import me.matsubara.realisticvillagers.nms.INMSConverter;
import me.matsubara.realisticvillagers.tracker.VillagerTracker;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.persistence.PersistentDataType;
import me.matsubara.realisticvillagers.entity.PlayerProcreationTracker;
import java.util.UUID;



public class MarriageListener implements Listener {
    private final RealisticVillagers plugin;

    public MarriageListener(RealisticVillagers plugin) {
        this.plugin = plugin;
    }
    private UUID partnerUUID1;
    private UUID partnerUUID2;


    @EventHandler
    private void ForceDivorcewhenmarried(MarriedEvent event) {
        Messages messages = plugin.getMessages();
        VillagerTracker tracker = plugin.getTracker();
        INMSConverter converter = plugin.getConverter();

        MarriagePlayer marriedplayer1 = event.getPlayer1();
        MarriagePlayer marriedplayer2 = event.getPlayer2();

        Player player1 = marriedplayer1.getPlayerOnline();
        Player player2 = marriedplayer2.getPlayerOnline();

        String uuidString1 = player1.getPersistentDataContainer().get(plugin.getMarriedWith(), PersistentDataType.STRING);
        if (uuidString1 != null) partnerUUID1 = UUID.fromString(uuidString1);
        player1.getPersistentDataContainer().remove(plugin.getMarriedWith());

        String uuidString2 = player2.getPersistentDataContainer().get(plugin.getMarriedWith(), PersistentDataType.STRING);
        if (uuidString2 != null) partnerUUID2 = UUID.fromString(uuidString2);;
        player2.getPersistentDataContainer().remove(plugin.getMarriedWith());

        for (IVillagerNPC offlineVillager : tracker.getOfflineVillagers()) {
            if (!offlineVillager.getUniqueId().equals(partnerUUID1)) continue;

            LivingEntity bukkit = offlineVillager.bukkit();
            if (bukkit == null) {
                bukkit = plugin.getUnloadedOffline(offlineVillager);
                if (bukkit == null) continue;
            }

            IVillagerNPC npc = converter.getNPC(bukkit).orElse(null);
            if (npc == null) continue;

            npc.divorceAndDropRing(player1);
            break;
        }
        for (IVillagerNPC offlineVillager : tracker.getOfflineVillagers()) {
            if (!offlineVillager.getUniqueId().equals(partnerUUID2)) continue;

            LivingEntity bukkit = offlineVillager.bukkit();
            if (bukkit == null) {
                bukkit = plugin.getUnloadedOffline(offlineVillager);
                if (bukkit == null) continue;
            }

            IVillagerNPC npc = converter.getNPC(bukkit).orElse(null);
            if (npc == null) continue;

            npc.divorceAndDropRing(player1);
            break;
        }
    }

    @EventHandler
    private void KidWhenKissing(KissEvent event) {
        MarriagePlayer kisser = event.getPlayer();
        MarriagePlayer partner = event.getPlayer().getPartner();

        Player kisserplayer = kisser.getPlayerOnline();
        Player partnerplayer = partner.getPlayerOnline();

        UUID player1UUID = kisserplayer.getUniqueId();
        UUID player2UUID = partnerplayer.getUniqueId();



        new BabyTaskPlayer(plugin, partnerplayer, kisserplayer).runTaskTimer(plugin, 0L, 20L);


    }
}
