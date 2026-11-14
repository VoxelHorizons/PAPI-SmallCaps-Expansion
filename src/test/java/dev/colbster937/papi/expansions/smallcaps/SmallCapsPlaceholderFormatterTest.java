package dev.colbster937.papi.expansions.smallcaps;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

class SmallCapsPlaceholderFormatterTest {
  @Test
  void resolvesLogicPlaceholderBeforeReplacingUnderscores() {
    List<String> seen = new ArrayList<>();
    String result = SmallCapsPlaceholderFormatter.format("{logic_npc-name_6}", value -> {
      seen.add(value);
      return "%logic_npc-name_6%".equals(value) ? "SHOP KEEPER" : value;
    });

    assertTrue(seen.contains("%logic_npc-name_6%"));
    assertEquals(SmallCapsCharacterMap.string2SmallCaps("SHOP KEEPER"), result);
  }

  @Test
  void resolvesNestedContextPlaceholderInsideLogicIdentifier() {
    List<String> seen = new ArrayList<>();
    String result = SmallCapsPlaceholderFormatter.format("{logic_npc-name_{npc}}", value -> {
      seen.add(value);
      if ("%npc%".equals(value)) return "6";
      if ("%logic_npc-name_6%".equals(value)) return "Welcome To The Shop";
      return value;
    });

    assertEquals("%npc%", seen.get(0));
    assertEquals("%logic_npc-name_6%", seen.get(1));
    assertEquals(SmallCapsCharacterMap.string2SmallCaps("Welcome To The Shop"), result);
  }

  @Test
  void convertsPlainUnderscoresButPreservesEscapedUnderscores() {
    String result = SmallCapsPlaceholderFormatter.format("HELLO_WORLD\\_TEST", value -> value);
    assertEquals(SmallCapsCharacterMap.string2SmallCaps("HELLO WORLD_TEST"), result);
  }

  @Test
  void resolvesMultiplePlaceholdersAndUnwrappedPercentPlaceholders() {
    String result = SmallCapsPlaceholderFormatter.format("{player_name}_and_%server_name%", value ->
        value.replace("%player_name%", "Alex").replace("%server_name%", "Voxel Horizons"));
    assertEquals(SmallCapsCharacterMap.string2SmallCaps("Alex and Voxel Horizons"), result);
  }

  @Test
  void doesNotResolveEscapedBraces() {
    String result = SmallCapsPlaceholderFormatter.format("\\{player_name\\}", value ->
        value.replace("%player_name%", "SHOULD NOT RESOLVE"));
    assertEquals(SmallCapsCharacterMap.string2SmallCaps("{player name}"), result);
  }

  @Test
  void preservesPlainInputWithoutBraces() {
    assertEquals(SmallCapsCharacterMap.string2SmallCaps("WOODEN TABLE"),
        SmallCapsPlaceholderFormatter.format("WOODEN_TABLE", value -> value));
  }
}
