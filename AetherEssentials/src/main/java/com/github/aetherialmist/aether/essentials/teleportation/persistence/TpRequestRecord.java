package com.github.aetherialmist.aether.essentials.teleportation.persistence;

import org.bukkit.entity.Player;

/**
 * Information of a teleport request
 *
 * @param sender      The Player that send the request
 * @param accepter    The Player responsible for accepting/denying the request
 * @param teleportee  The Player being teleported
 * @param destination The Player to teleport the teleportee to
 */
public record TpRequestRecord(Player sender, Player accepter, Player teleportee, Player destination) {

}
