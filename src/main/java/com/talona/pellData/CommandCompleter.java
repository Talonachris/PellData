package com.talona.pellData;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CommandCompleter implements TabCompleter {

    private final List<String> firstArgs = Arrays.asList("stats", "ranking", "player", "reset");
    private final List<String> rankingArgs = Arrays.asList("placed", "broken", "killed");
    private final List<String> statsArgs = Arrays.asList("blocks", "killed", "all");

    private final DatabaseManager db;

    public CommandCompleter(DatabaseManager db) {
        this.db = db;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return firstArgs.stream()
                    .filter(arg -> arg.startsWith(args[0].toLowerCase()))
                    .collect(Collectors.toList());
        }

        if (args.length == 2) {
            String sub = args[0].toLowerCase();
            String input = args[1].toLowerCase();

            return switch (sub) {
                case "ranking" -> rankingArgs.stream()
                        .filter(arg -> arg.startsWith(input))
                        .collect(Collectors.toList());

                case "stats" -> statsArgs.stream()
                        .filter(arg -> arg.startsWith(input))
                        .collect(Collectors.toList());

                case "player", "reset" -> db.getAllStoredPlayerNames().stream()
                        .filter(name -> name.toLowerCase().startsWith(input))
                        .collect(Collectors.toList());

                default -> null;
            };
        }

        return null;
    }
}
