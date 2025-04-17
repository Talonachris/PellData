![PellData Banner](./A_2D_digital_graphic_banner_displays_promotional_i.png)

# PellData – Spigot 1.21.5 Statistics Plugin

![Spigot](https://img.shields.io/badge/spigot-1.21.5-orange)
![Java](https://img.shields.io/badge/java-21-brightgreen)

---

## 🇬🇧 English

---

### ✨ Features
- **Block statistics** – placed & broken blocks (overall & per‑type) with *top* lists and rankings.
- **Mob & PvP kills** – separate counters incl. *top* killed mob types.
- **Deaths** – tracks player deaths.
- **Playtime (AFK filter)** – adds 5 s every 5 s while player is active (≤ 10 min idle).
- **Chat messages** – counts chat lines written.
- **Global stats** – `/pelldata globalstats` summarises everyone.
- **Rankings** – `/pelldata ranking <type>` shows global Top 10.
- **PlaceholderAPI** – personal, global & per‑player placeholders.
- **Localisation** – translatable via `locales/en_us.yml`, `locales/de_de.yml`, …

---

### 🚀 Installation
1. Download the JAR from **Releases**.  
2. Drop it into the **`/plugins`** folder.  
3. *(Important!)* Install [PlaceholderAPI](https://www.spigotmc.org/resources/placeholderapi.6245/).  
4. Start the server – PellData will create its files.

---

### ⚙️ Configuration
```yml
# plugins/PellData/config.yml
language: en_us   # en_us | de_de …
```

---

### 🕹️ Commands & Permissions
| Command | Permission | Description |
|---------|------------|-------------|
| `/pelldata stats <sub>` | – | Your stats (`blocks`, `killed`, `pvp`, `deaths`, `playtime`, `chat`, `all`, `topplaced`, `topbroken`, `topmobs`) |
| `/pelldata ranking <type>` | – | Top‑10 list (`placed`, `broken`, `killed`, `deaths`, `playtime`, `chat`, `pvp`) |
| `/pelldata player <name>` | – | Another player’s stats |
| `/pelldata reset <name>` | `pelldata.reset` | Reset a player’s stats |
| `/pelldata globalstats` | – | Combined server statistics |

---

### 📚 PlaceholderAPI Variables

---

**Personal**
```
%pelldata_blocks_placed%
%pelldata_blocks_broken%
%pelldata_killed_mobs%
%pelldata_deaths%
%pelldata_playtime%
%pelldata_chat%
%pelldata_pvp%
```

---

**Global**
```
%pelldata_global_blocks_placed%
%pelldata_global_blocks_broken%
%pelldata_global_killed_mobs%
%pelldata_global_deaths%
%pelldata_global_playtime%
%pelldata_global_chat%
%pelldata_global_pvp%
```

---

**For a specific player**
```
%pelldata_blocks_placed_<Name>%
%pelldata_blocks_broken_<Name>%
%pelldata_killed_mobs_<Name>%
%pelldata_deaths_<Name>%
%pelldata_playtime_<Name>%
%pelldata_chat_<Name>%
%pelldata_pvp_<Name>%
```

---

### 🌐 Localisation
English file **`en_us.yml`** is loaded by default. Copy it (e.g. as `de_de.yml`) and translate – keep colour codes & placeholders **as is**.

---

### 🤖 Made with AI, love & plenty of time by **Talonachris**
The **entire** plugin was built fully with the help of AI – from the very first line of code – and then polished with lots of love and time by **Talonachris**.

---

### 🛣️ Coming Features
- Re‑format & beautify all messages  
- Database backup function  
- Seamless DB upgrade between plugin versions  
- MySQL support

---

## 🇩🇪 Deutsch

---

### ✨ Funktionen
- **Block‑Statistiken** – gesetzte & abgebaute Blöcke (gesamt & pro Typ) mit *Top*‑Listen und Ranglisten.
- **Mob‑ & PvP‑Kills** – getrennte Zähler inkl. *Top* getötete Mob‑Typen.
- **Tode** – Spielertode.
- **Spielzeit (AFK‑Filter)** – alle 5 s, solange der Spieler ≤ 10 Min. aktiv ist.
- **Chatnachrichten** – Anzahl gesendeter Nachrichten.
- **Globale Statistiken** – `/pelldata globalstats` fasst alle Spieler zusammen.
- **Ranglisten** – `/pelldata ranking <typ>` zeigt globale Top 10.
- **PlaceholderAPI** – persönliche, globale & spielerbezogene Platzhalter.
- **Lokalisierung** – Übersetzbar via `locales/en_us.yml`, `locales/de_de.yml` …

---

### 🚀 Installation
1. JAR von den **Releases** herunterladen.  
2. In den **`/plugins`**‑Ordner legen.  
3. *(Wichtig!)* [PlaceholderAPI](https://www.spigotmc.org/resources/placeholderapi.6245/) installieren.  
4. Server starten – PellData erstellt seine Dateien.

---

### ⚙️ Konfiguration
```yml
# plugins/PellData/config.yml
language: de_de   # de_de | en_us …
```

---

### 🕹️ Befehle & Berechtigungen
| Befehl | Permission | Beschreibung |
|--------|------------|--------------|
| `/pelldata stats <sub>` | – | Eigene Stats (`blocks`, `killed`, `pvp`, `deaths`, `playtime`, `chat`, `all`, `topplaced`, `topbroken`, `topmobs`) |
| `/pelldata ranking <typ>` | – | Top‑10‑Liste (`placed`, `broken`, `killed`, `deaths`, `playtime`, `chat`, `pvp`) |
| `/pelldata player <name>` | – | Stats eines anderen Spielers |
| `/pelldata reset <name>` | `pelldata.reset` | Stats eines Spielers zurücksetzen |
| `/pelldata globalstats` | – | Gesamte Server‑Statistiken |

---

### 📚 PlaceholderAPI‑Variablen

---

**Eigene Werte**
```
%pelldata_blocks_placed%
%pelldata_blocks_broken%
%pelldata_killed_mobs%
%pelldata_deaths%
%pelldata_playtime%
%pelldata_chat%
%pelldata_pvp%
```

---

**Globale Werte**
```
%pelldata_global_blocks_placed%
%pelldata_global_blocks_broken%
%pelldata_global_killed_mobs%
%pelldata_global_deaths%
%pelldata_global_playtime%
%pelldata_global_chat%
%pelldata_global_pvp%
```

---

**Spielerbezogen**
```
%pelldata_blocks_placed_<Name>%
%pelldata_blocks_broken_<Name>%
%pelldata_killed_mobs_<Name>%
%pelldata_deaths_<Name>%
%pelldata_playtime_<Name>%
%pelldata_chat_<Name>%
%pelldata_pvp_<Name>%
```

---

### 🌐 Lokalisierung
Die englische Datei **`en_us.yml`** wird standardmäßig geladen. Kopiere sie z. B. als `de_de.yml`, übersetze die Texte -Farbcodes & Platzhalter **unverändert** lassen.

---

### 🤖 Gemacht mit KI, Liebe & viel Zeit von **Talonachris**
Das **gesamte** Plugin wurde vollständig mithilfe von KI (künstlicher Intelligenz) entwickelt – von der ersten Codezeile – und anschließend mit viel Liebe und Zeit von **Talonachris** verfeinert.

---

### 🛣️ Kommende Features
- Alle Nachrichten neu formatieren & verschönern  
- Backup‑Funktion für die Datenbank  
- Datenbank‑Migration zwischen Plugin‑Versionen  
- MySQL‑Support

---
