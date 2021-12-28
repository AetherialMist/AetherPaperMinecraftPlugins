package com.github.aetherialmist.aether.essentials.teleportation.persistence;

import org.bukkit.entity.Player;

public record TpRequestRecord(Player sender, Player accepter, Player teleportee, Player destination) {

}
