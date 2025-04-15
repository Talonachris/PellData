package com.talona.pellData;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CommandCompleter implements TabCompleter {

    // Erste Argumente für /pelldata
    private final List<String> firstArgs = Arrays.asList("stats", "ranking", "player");

    // Sub-Argumente für den "ranking"-Subcommand
    private final List<String> rankingArgs = Arrays.asList("placed", "broken", "killed");

    // Sub-Argumente für den "stats"-Subcommand
    private final List<String> statsArgs = Arrays.asList("blocks", "killed", "all");

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            String toComplete = args[0].toLowerCase();
            return firstArgs.stream()
                    .filter(arg -> arg.startsWith(toComplete))
                    .collect(Collectors.toList());
        }

        if (args.length == 2) {
            String firstArg = args[0].toLowerCase();
            if (firstArg.equals("ranking")) {
                String toComplete = args[1].toLowerCase();
                return rankingArgs.stream()
                        .filter(arg -> arg.startsWith(toComplete))
                        .collect(Collectors.toList());
            }
            if (firstArg.equals("stats")) {
                String toComplete = args[1].toLowerCase();
                return statsArgs.stream()
                        .filter(arg -> arg.startsWith(toComplete))
                        .collect(Collectors.toList());
            }
            if (firstArg.equals("player")) {
                String toComplete = args[1].toLowerCase();
                List<String> playerNames = new ArrayList<>();
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (p.getName().toLowerCase().startsWith(toComplete)) {
                        playerNames.add(p.getName());
                    }
                }
                return playerNames;
            }
        }
        return null;
    }
}
