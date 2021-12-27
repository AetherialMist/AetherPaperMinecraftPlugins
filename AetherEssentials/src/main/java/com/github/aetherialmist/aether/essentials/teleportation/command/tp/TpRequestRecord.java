package com.github.aetherialmist.aether.essentials.teleportation.command.tp;

import org.bukkit.entity.Player;

public record TpRequestRecord(Player sender, Player accepter, Player teleportee, Player destination) {

}
