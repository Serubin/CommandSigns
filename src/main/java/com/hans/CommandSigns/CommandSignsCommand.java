package com.hans.CommandSigns;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

class CommandSignsCommand implements CommandExecutor {

	private CommandSigns plugin;
	private int max_lines;

	public CommandSignsCommand(CommandSigns plugin) {
		this.plugin = plugin;
		max_lines = plugin.getConfig().getInt("options.max_lines");
	}

	public boolean onCommand(CommandSender sender, Command cmd,
			String commandLabel, String[] args) {
		if (commandLabel.equalsIgnoreCase("commandsigns")
				|| commandLabel.equalsIgnoreCase("cmds")) {
			if (args.length < 1) {
				return false;
			}
			Player player = (Player) sender;
			String playerName = player.getName();
			if (args[0].equalsIgnoreCase("?")) {
				if (args.length == 1) {
					player.sendMessage("General Help:");
					player.sendMessage(ChatColor.GRAY
							+ "/"
							+ commandLabel
							+ " line<number>"
							+ ChatColor.WHITE
							+ " - Add a line to your CommandSign clipboard. Right click a sign to activate.");
					player.sendMessage(ChatColor.GRAY
							+ "/"
							+ commandLabel
							+ " read"
							+ ChatColor.WHITE
							+ " - Right click a sign to read the CommandSign text.");
					player.sendMessage(ChatColor.GRAY + "/" + commandLabel
							+ " copy" + ChatColor.WHITE
							+ " - Copy a CommandSigns to your clipboard.");
					player.sendMessage(ChatColor.GRAY + "/" + commandLabel
							+ " remove " + ChatColor.WHITE
							+ "- Right click a sign to remove a CommandSign.");
					player.sendMessage(ChatColor.GRAY + "/" + commandLabel
							+ " clear" + ChatColor.WHITE
							+ " - Clear your CommandSign clipboard.");
					player.sendMessage(ChatColor.RED
							+ "This feature is disabled:");
					player.sendMessage("Type" + ChatColor.GRAY + " /"
							+ commandLabel + " ? edit" + ChatColor.WHITE
							+ " to learn more about editing.");

				} else if (args[1].equalsIgnoreCase("edit")) {
					player.sendMessage(ChatColor.RED
							+ "This feature is disabled.");
					player.sendMessage("Edit help:");
					player.sendMessage(ChatColor.GRAY + "/" + commandLabel
							+ " edit" + ChatColor.WHITE
							+ " - Select a Commandsign to edit");
					player.sendMessage(ChatColor.GRAY + "/" + commandLabel
							+ " edit line<number>" + ChatColor.WHITE
							+ " - Edit line from CommandSign");
					player.sendMessage(ChatColor.GRAY + "/" + commandLabel
							+ " edit cancel" + ChatColor.WHITE
							+ " - Cancel a Commandsign in edit");
				}
				return true;
			}
			if (args[0].indexOf("line") == 0) {
				if (plugin.hasPermission(player, "CommandSigns.create.regular")
						|| plugin.hasPermission(player,
								"CommandSigns.create.super")) {
					int lineNumber;
					try {
						lineNumber = Integer.parseInt(args[0].substring(4));
					} catch (NumberFormatException ex) {
						player.sendMessage(ChatColor.RED
								+ "Line number invalid!");
						return true;
					}
					/*
					 * if (!HashMaps.checkPlayerText(player.getName())) {
					 * player.sendMessage(ChatColor.RED +
					 * "You are not currently editing a sign!"); return true; }
					 */
					if (lineNumber > max_lines) {
						player.sendMessage(ChatColor.RED
								+ "You may not have more then "
								+ Integer.toString(max_lines)
								+ " on one CommandSign. This line will not be added!");
					}
					if (lineNumber > 0) {
						if (HashMaps.getPlayerText(player.getName()).getLine(
								lineNumber - 1) == null
								|| HashMaps.getPlayerText(player.getName()) == null) {
							player.sendMessage(ChatColor.RED
									+ "There is no line before line "
									+ Integer.toString(lineNumber)
									+ ". This line will not be added!");
							return true;
						}
					}
					CommandSignsText text;
					if ((text = HashMaps.getPlayerText(playerName)) == null) {
						text = new CommandSignsText(max_lines);
					}
					String line = getLine(args, 1);

					if (line.contains("/*")
							&& !plugin.hasPermission(player,
									"CommandSigns.create.super")) {
						while (line.contains("/*")) {
							line = line.replace("/*", "/");
						}
						player.sendMessage(ChatColor.RED
								+ "You may not make signs with '/*'");
					}
					text.setLine(lineNumber, line);
					HashMaps.addPlayerText(playerName, text);
					player.sendMessage("Line " + lineNumber + ": " + line);
					HashMaps.addPlayerState(playerName,
							CommandSignsPlayerState.ENABLE);
					player.sendMessage("Ready to add.");
				}

			} else if (args[0].equalsIgnoreCase("name")) {
				if (plugin.hasPermission(player, "CommandSigns.create.regular")
						|| plugin.hasPermission(player,
								"CommandSigns.create.super")) {
					HashMaps.setSignName(playerName, args[1]);
					player.sendMessage("Name: " + args[1]);
					if (HashMaps.getPlayerStates(playerName) == CommandSignsPlayerState.ENABLE) {
						player.sendMessage("Ready to add.");
					} else {
						player.sendMessage("Add commands before adding to sign");
					}
				}
			} else if (args[0].equalsIgnoreCase("read")) {
				if (plugin.hasPermission(player, "CommandSigns.create.regular")
						|| plugin.hasPermission(player,
								"CommandSigns.create.super")) {
					HashMaps.addPlayerState(playerName,
							CommandSignsPlayerState.READ);
					player.sendMessage("Click a sign to read CommandSign text.");
				}
			} else if (args[0].equalsIgnoreCase("copy")) {
				if (plugin.hasPermission(player, "CommandSigns.create.regular")
						|| plugin.hasPermission(player,
								"CommandSigns.create.super")) {
					HashMaps.addPlayerState(playerName,
							CommandSignsPlayerState.COPY);
					player.sendMessage("Click a sign to copy CommandSign text.");
				}
			} else if (args[0].equalsIgnoreCase("remove")) {
				if (plugin.hasPermission(player, "CommandSigns.remove")) {
					HashMaps.addPlayerState(playerName,
							CommandSignsPlayerState.DISABLE);
					player.sendMessage("Click a sign to remove CommandSign.");
				}
			} else if (args[0].equalsIgnoreCase("clear")) {
				if (plugin.hasPermission(player, "CommandSigns.remove")
						|| plugin.hasPermission(player,
								"CommandSigns.create.regular")
						|| plugin.hasPermission(player,
								"CommandSigns.create.super")) {
					HashMaps.removePlayerState(playerName);
					HashMaps.removePlayerText(playerName);
					player.sendMessage("CommandSign text and status cleared.");
				}
				// TODO add edit command
			} else if (args[0].equalsIgnoreCase("edit")) {
				player.sendMessage(ChatColor.RED
						+ "This feature has been disabled.");
				return true;
				/*
				 * if (plugin.hasPermission(player, "CommandSigns.edit")) { if
				 * (args.length == 1) { if
				 * (HashMaps.getPlayerStates(player.getName()) !=
				 * CommandSignsPlayerState.EDIT) {
				 * HashMaps.addPlayerState(playerName,
				 * CommandSignsPlayerState.EDIT);
				 * player.sendMessage("Click the sign you wish to edit."); }
				 * return true; }
				 * 
				 * if (HashMaps.getPlayerStates(playerName) ==
				 * CommandSignsPlayerState.EDIT &&
				 * HashMaps.comfirmEdit(playerName)) { int lineNumber; String
				 * line = getLine(args, 2); CommandSignsText data =
				 * HashMaps.getEditText(player .getName()); // Cancels edit if
				 * (args[1].equalsIgnoreCase("cancel")) {
				 * HashMaps.removePlayerState(playerName); }
				 * 
				 * try { lineNumber = Integer.parseInt(args[1].substring(4)); }
				 * catch (NumberFormatException ex) {
				 * player.sendMessage(ChatColor.RED + "Line number invalid!");
				 * return true; } if (lineNumber > max_lines) {
				 * player.sendMessage(ChatColor.RED +
				 * "You may not have more then " + Integer.toString(max_lines) +
				 * " on one CommandSign. This line will not be added!"); return
				 * true; } if (lineNumber != 0) { if (data.getText()[lineNumber
				 * - 1] == null) { player.sendMessage(ChatColor.RED +
				 * "There is no line before line " +
				 * Integer.toString(lineNumber) +
				 * ". This line will not be added!"); return false; } }
				 * 
				 * // Add data to hashmap
				 * player.sendMessage("Updating line text"); String[] text =
				 * data.getText(); plugin.logDebug(data.toString());
				 * ArrayList<String> temp = new ArrayList<String>(); for(String
				 * str:text){ temp.add(str); plugin.logDebug("Adding to temp " +
				 * str); } temp.add(line); plugin.logDebug("Adding to temp " +
				 * line); String[] tempNew = new String[temp.size()]; int count
				 * = 0; for(Object obj : temp.toArray()){
				 * plugin.logDebug("Adding to tempNew " + (String)obj);
				 * tempNew[count] = (String)obj; } data = new
				 * CommandSignsText(tempNew);
				 * 
				 * HashMaps.setTextEdit(player.getName(), data.getText());
				 * player.sendMessage("Line " + lineNumber + ": " + line);
				 * player.sendMessage("Right click the sign to update"); // TODO
				 * set new text } else { player.sendMessage(ChatColor.RED +
				 * "You are not currently editing a sign."); } }
				 */

			} else if (args[0].equalsIgnoreCase("debug")) {
				if (plugin.hasPermission(player, "CommandSigns.debug")) {
					if (args[1].equalsIgnoreCase("signs")) {
						player.sendMessage(HashMaps.activeSignsToString());
					} else if (args[1].equalsIgnoreCase("ids")) {
						player.sendMessage(HashMaps.activeSignsIdsToString());
					} else if (args[1].equalsIgnoreCase("playerText")) {
						if (args[2] != null) {
							player.sendMessage(args[2] + "s text");
							player.sendMessage(Boolean.toString(HashMaps
									.checkPlayerText(args[2])));
							System.out.println(HashMaps.getPlayerText(args[2])
									.getText().length);
							for (String str : HashMaps.getPlayerText(args[2])
									.getText()) {
								System.out.println(str);
								player.sendMessage(str);
							}
						} else {
							player.sendMessage("No player defined");
						}
					} else if (args[1].equalsIgnoreCase("migrate")) {
						CommandSignsMigrate csm = new CommandSignsMigrate(
								plugin);
						if (args.length == 3) {
							csm.loadData(args[2]);
						}
					}
				}
			} else {
				player.sendMessage(ChatColor.RED
						+ "Wrong CommandSigns command syntax.");
			}
			return true;
		}
		return false;
	}

	/*
	 * /** get line from args, removing unused peices.
	 * 
	 * @param args Sting[] of args
	 * 
	 * @param index where to start
	 * 
	 * @return line
	 * 
	 * private String getLine(String[] args, int index) { String line = ""; for
	 * (int i = index; i < args.length; i++) { line = line.concat(args[i] + ((i
	 * != (args.length - index)) ? " " : "")); } return line; }
	 */
	/**
	 * Gets line from args, removes unused pieces
	 * 
	 * @param args
	 *            Arguments from commands
	 * @param index
	 * @return line
	 */
	public static String getLine(String[] args, int index) {
		String[] argNew = new String[args.length - index];
		for (int i = index; i < args.length; i++) {
			argNew[i - index] = args[i];
		}
		String line = "";
		for (int i = 0; i < argNew.length; i++) {
			line = line + argNew[i] + " ";
		}
		return line;
	}
}
