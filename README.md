# PellData
_A lightweight and powerful player statistics plugin for your Minecraft server._

---

## 📋 About

**PellData** is a stats tracking plugin that logs various player actions like block placements, kills, deaths, chat messages, playtime, and more.  
It supports localization, PlaceholderAPI integration, and both player and global statistics.

It stores data in a **SQLite database**, tracks **every block type separately**, and is fully designed to be performance-friendly and modular.

> This plugin was built entirely with the help of AI – because I wanted a custom stats system like this for my server, but couldn't find anything that fit.  
> Suggestions and ideas are always welcome!

---

## 🧩 Dependencies

This plugin **requires** [PlaceholderAPI](https://www.spigotmc.org/resources/placeholderapi.6245/).

Make sure you install it before using PellData.

---

## 💬 Commands

| Command | Description |
|--------|-------------|
| `/pelldata stats` | Show player stats |
| `/pelldata stats blocks` | Show placed & broken blocks |
| `/pelldata stats killed` | Show killed mobs |
| `/pelldata stats pvp` | Show PvP kills |
| `/pelldata stats deaths` | Show death count |
| `/pelldata stats playtime` | Show total playtime |
| `/pelldata stats chat` | Show chat message count |
| `/pelldata stats all` | Show all stats |
| `/pelldata stats topplaced` | Show top 10 blocks placed (by type) |
| `/pelldata stats topbroken` | Show top 10 blocks broken (by type) |
| `/pelldata stats topmobs` | Show top 10 mob types killed |
| `/pelldata ranking <type>` | Show leaderboard for a stat (type: placed, broken, killed, pvp, chat, deaths, playtime) |
| `/pelldata player <name>` | View another player's stats |
| `/pelldata globalstats` | View global server-wide statistics |
| `/pelldata reset <name>` | Reset a player’s stats (requires permission) |

---

## 🔐 Permissions

| Permission | Description |
|------------|-------------|
| `pelldata.reset` | Allows resetting another player's stats (`/pelldata reset`) |
| _default:_ `op` |

---

## 📦 PlaceholderAPI Variables

You can use these in any plugin that supports PlaceholderAPI (scoreboards, signs, menus, etc.)

### 👤 Player-specific

```
%pelldata_blocks_placed%
%pelldata_blocks_broken%
%pelldata_killed_mobs%
%pelldata_pvp%
%pelldata_deaths%
%pelldata_playtime%
%pelldata_chat%
```

### 🔄 Player-specific (named)

```
%pelldata_blocks_placed_PlayerName%
%pelldata_pvp_Talonachris%
%pelldata_chat_Notch%
```

> These variants let you show stats of any player, even if they’re offline.

### 🌍 Global statistics

```
%pelldata_global_blocks_placed%
%pelldata_global_blocks_broken%
%pelldata_global_killed_mobs%
%pelldata_global_pvp%
%pelldata_global_deaths%
%pelldata_global_playtime%
%pelldata_global_chat%
```

---

## 📁 Localization

Default languages: `en_us` and `de_de`  
You can switch via `config.yml`:

```yml
language: en_us
```

---

## 💛 Credits

Developed with love by Talonachris.  
Powered by [PlaceholderAPI](https://www.spigotmc.org/resources/placeholderapi.6245/).  
Data stored in a local **SQLite database**.  
Every block type is tracked individually for maximum detail.

> This plugin was created 100% with the help of AI, based on my vision of a custom Minecraft stats system.  
> If you have ideas or suggestions – feel free to share them!
