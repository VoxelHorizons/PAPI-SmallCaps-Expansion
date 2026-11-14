package dev.colbster937.papi.expansions.smallcaps;

import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;

import org.bukkit.OfflinePlayer;

public class SmallCapsPAPIExpansion extends PlaceholderExpansion {
  @Override
  public String getIdentifier() {
    return "${expansion_id}";
  }

  @Override
  public String getVersion() {
    return "${expansion_version}";
  }

  @Override
  public String getAuthor() {
    return "${expansion_author}";
  }

  @Override
  public String onRequest(OfflinePlayer plr, String str) {
    String out = str;
    out = out
        .replaceFirst("${regex_a}", "%")
        .replaceFirst("${regex_b}", "%")
        .replaceAll("${regex_c}", " ")
        .replaceAll("${regex_d}", "${dollar}1");
    if (out.contains("%"))
      out = PlaceholderAPI.setPlaceholders(plr, out);
    return SmallCapsCharacterMap.string2SmallCaps(out);
  }
}
