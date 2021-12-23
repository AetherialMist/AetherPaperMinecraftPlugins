package aetherial.aether.essentials.teleportation.command.tp;

import org.bukkit.entity.Player;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Tracks teleportation requests between players
 * <p>
 * Related commands are responsible for updating and removing requests
 */
public class TpRequestTracker {

    private static TpRequestTracker instance;

    public static TpRequestTracker instance() {
        if (instance == null) {
            instance = new TpRequestTracker();
        }
        return instance;
    }

    private final Map<Player, TpRequestRecord> pendingRequests = new ConcurrentHashMap<>();
    private final Map<Player, Player> sentRequests = new ConcurrentHashMap<>();

    private TpRequestTracker() {
    }

    /**
     * The given request will override an existing pending request
     *
     * @param sender      The player who sent the request
     * @param accepter    The player responsible for accepting the request
     * @param teleportee  The player being teleported
     * @param destination The destination player to teleport to
     */
    public void trackRequest(Player sender, Player accepter, Player teleportee, Player destination) {
        this.trackRequest(new TpRequestRecord(sender, accepter, teleportee, destination));
    }

    /**
     * The given request will override an existing pending request
     *
     * @param request The teleport request record
     */
    public void trackRequest(TpRequestRecord request) {
        pendingRequests.put(request.accepter(), request);
        sentRequests.put(request.sender(), request.accepter());
    }

    /**
     * @param accepter The player responsible for accepting a request
     * @return The request record for the tracked request
     */
    public Optional<TpRequestRecord> getAndRemoveRequest(Player accepter) {
        TpRequestRecord request = null;
        if (pendingRequests.containsKey(accepter)) {
            request = pendingRequests.remove(accepter);
        }
        if (request != null) {
            sentRequests.remove(request.sender());
        }

        return Optional.ofNullable(request);
    }

    /**
     * @param sender The player who sent the request
     * @return The request record for the tracked request
     */
    public Optional<TpRequestRecord> cancelRequest(Player sender) {
        TpRequestRecord request = null;
        if (sentRequests.containsKey(sender)) {
            Player accepter = sentRequests.remove(sender);

            request = pendingRequests.remove(accepter);
        }

        return Optional.ofNullable(request);
    }

}
