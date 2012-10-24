package com.hans.CommandSigns;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.permissions.PermissionAttachment;

public class CommandSignsSignClickEvent {

    private static CommandSigns plugin;

    // private static String[] delimiters = {"/","\\\\","@"};

    public CommandSignsSignClickEvent(CommandSigns instance) {
        plugin = instance;
    }

    public void onRightClick(PlayerInteractEvent event, Sign sign) {
        Player player = event.getPlayer();
        Location location = new Location(sign.getWorld(), sign.getX(),
                sign.getY(), sign.getZ());
        CommandSignsPlayerState state = HashMaps
                .getPlayerStates(player.getName());
        if (state != null) {
            if (state.equals(CommandSignsPlayerState.ENABLE)) {
                enableSign(player, location);
            } else if (state.equals(CommandSignsPlayerState.DISABLE)) {
                disableSign(player, location);
            } else if (state.equals(CommandSignsPlayerState.READ)) {
                readSign(player, location);
            } else if (state.equals(CommandSignsPlayerState.COPY)) {
                copySign(player, location);
            }
            return;
        }
        if (!HashMaps.signCheck(location)) {
            return;
        }
        List<String> commandList = parseCommandSign(player,
                HashMaps.getSignText(location));
        if (plugin.hasPermission(player, "CommandSigns.use.regular")) {
            String groupFilter = null;

            for (String command : commandList) {
                if (command.indexOf("@") != 0 && groupFilter != null
                        && !inGroup(player, groupFilter)) {
                    continue;
                }

                if (command.startsWith("@")) {
                    if (command.length() <= 1) {
                        groupFilter = null;
                    } else {
                        groupFilter = command.substring(1).trim();
                    }
                    continue;
                }

                if (command.startsWith("/")) {
                    PermissionAttachment newPermission = null;
                    if (command.length() <= 1) {
                        player.sendMessage("Error, SignCommand /command is of length 0.");
                        continue;
                    }
                    if (command.indexOf("*") == 1) {
                        command = command.substring(1);
                        if (player.hasPermission("CommandSigns.use.super")) {
                            if (!player
                                    .hasPermission("CommandSigns.permissions")) {
                                newPermission = player.addAttachment(plugin,
                                        "CommandSigns.permissions", true);
                            }
                        } else {
                            player.sendMessage("You may not use this type of sign.");
                            continue;
                        }
                    }
                    player.performCommand(command.substring(1));
                    if (newPermission != null) {
                        newPermission.remove();
                    }
                    continue;
                }

                if (command.startsWith("\\")) {
                    String msg = command.substring(1);
                    player.sendMessage(msg);
                    continue;
                }
            }
        }
    }

    private boolean inGroup(Player player, String group) {
        String permissionsNode = ("CommandSigns.group." + group);
        if (player.hasPermission(permissionsNode)) {
            return true;
        }
        player.sendMessage("You do not have that group permission");
        return false;
    }

    public static List<String> parseCommandSign(Player player,
            CommandSignsText commandSign) {
        List<String> commandList = new ArrayList<String>();
        String line;
        for (int i = 0; i < commandSign.getText().length; i++) {
            line = commandSign.getLine(i);
            if (line != null) {
                line = line.replace("<X>", ""
                        + player.getLocation().getBlockX());
                line = line.replace("<Y>", ""
                        + player.getLocation().getBlockY());
                line = line.replace("<Z>", ""
                        + player.getLocation().getBlockZ());
                line = line.replace("<NAME>", "" + player.getName());
                line = line.replace("&0", "§0");
                line = line.replace("&1", "§1");
                line = line.replace("&2", "§2");
                line = line.replace("&3", "§3");
                line = line.replace("&4", "§4");
                line = line.replace("&5", "§5");
                line = line.replace("&6", "§6");
                line = line.replace("&7", "§7");
                line = line.replace("&8", "§8");
                line = line.replace("&9", "§9");
                line = line.replace("&a", "§a");
                line = line.replace("&b", "§b");
                line = line.replace("&c", "§c");
                line = line.replace("&d", "§d");
                line = line.replace("&e", "§e");
                line = line.replace("&f", "§f");
                commandList.add(line);
            }
        }
        return commandList;
    }

    // To parse a sign... kept in case regular sign support is added again
    /*
     * public static List<String> parseSignText(Player player, String text) {
     * text = text.replace("<X>", ""+ player.getLocation().getBlockX()); text =
     * text.replace("<Y>", ""+ player.getLocation().getBlockY()); text =
     * text.replace("<Z>", ""+ player.getLocation().getBlockZ()); text =
     * text.replace("<NAME>", ""+ player.getName()); List<String> commandList =
     * new ArrayList<String>(); commandList.add(text); for(String delimiter :
     * delimiters) { List<String> commandSplit = new ArrayList<String>();
     * for(String s : commandList) { String[] split = s.split(delimiter);
     * for(int i=0;i<split.length;i++) { if(split[i].length()>1)
     * commandSplit.add((i!=0?delimiter:"")+split[i]); } } commandList =
     * commandSplit; } return commandList; }
     */

    public void enableSign(Player player, Location location) {
        if (HashMaps.signCheck(location)) {
            player.sendMessage("Sign is already enabled!");
            return;
        }
        CommandSignsText text = HashMaps.getPlayerText(player.getName());
        if (plugin.addSign(new CommandSignsData(0, location, text))) {
            HashMaps.removePlayerState(player.getName());
            HashMaps.removePlayerText(player.getName());
            player.sendMessage("CommandSign enabled");
        } else {
            player.sendMessage(ChatColor.RED
                    + "There was an error enabling CommandSign, check console for error!");
        }
    }

    public void readSign(Player player, Location location) {
        CommandSignsText text = HashMaps.getSignText(location);
        if (text == null) {
            player.sendMessage("Sign is not a CommandSign.");
        }
        String[] lines = text.getText();
        for (int i = 0; i < lines.length; i++) {
            if (lines[i] != null) {
                player.sendMessage("Line" + i + ": " + lines[i]);
            }
        }
        HashMaps.removePlayerState(player.getName());
    }

    public void copySign(Player player, Location location) {
        String playerName = player.getName();
        CommandSignsText text = HashMaps.getSignText(location);
        if (text == null) {
            player.sendMessage("Sign is not a CommandSign.");
        }
        HashMaps.addPlayerText(playerName, text);
        String[] lines = text.getText();
        for (int i = 0; i < lines.length; i++) {
            if (lines[i] != null) {
                player.sendMessage("Line" + i + ": " + lines[i]);
            }
        }
        player.sendMessage("Added to CommandSigns clipboard. Click a sign to enable.");
        HashMaps.addPlayerState(playerName, CommandSignsPlayerState.ENABLE);
    }

    public void disableSign(Player player, Location location) {
        int id = HashMaps.getSignId(location);
        String playerName = player.getName();
        if (!HashMaps.signCheck(location)) {
            player.sendMessage("Sign is not enabled!");
            HashMaps.removePlayerState(playerName);
            return;
        }
        if (HashMaps.playerTextContainsKey(playerName)) {
            HashMaps.addPlayerState(playerName, CommandSignsPlayerState.ENABLE);
            if (plugin.removeSign(id)) {
                player.sendMessage("Sign disabled. You still have text in your clipboard.");
            } else {
                player.sendMessage(ChatColor.RED
                        + "There was an error disabling this CommandSign. Check console for error!");
                player.sendMessage("You still have text in your clipboard!");
            }

        } else {
            HashMaps.removePlayerState(playerName);
            if (plugin.removeSign(id)) {
                player.sendMessage("Sign disabled.");
            } else {
                player.sendMessage(ChatColor.RED
                        + "There was an error disabling this CommandSign. Check console for error!");
            }
        }
    }
}
