package dev.colbster937.papi.expansions.smallcaps;

import java.util.function.Function;

/**
 * Resolves brace-wrapped PlaceholderAPI placeholders before formatting text.
 *
 * The braces allow a placeholder to be embedded inside the parameters of
 * %smallcaps_...% without introducing additional percent delimiters.
 * Placeholder identifiers (including their underscores) must not be changed
 * until after PlaceholderAPI has had a chance to resolve them.
 */
public final class SmallCapsPlaceholderFormatter {
  private static final int MAX_NESTING = 16;

  private SmallCapsPlaceholderFormatter() {}

  public static String format(String parameters, Function<String, String> resolver) {
    if (parameters == null || parameters.isEmpty())
      return "";

    String expanded = resolveBraces(parameters, resolver, 0);
    // Also support ordinary %placeholder_name% values inside the input.
    String resolved = resolve(expanded, resolver);
    return SmallCapsCharacterMap.string2SmallCaps(normalizeText(resolved));
  }

  private static String resolveBraces(String text, Function<String, String> resolver, int depth) {
    if (depth >= MAX_NESTING)
      return text;

    StringBuilder out = new StringBuilder(text.length());
    for (int i = 0; i < text.length(); i++) {
      char current = text.charAt(i);

      // Leave escapes intact until all placeholder names have been processed.
      if (current == '\\' && i + 1 < text.length() && isEscapable(text.charAt(i + 1))) {
        out.append(current).append(text.charAt(++i));
        continue;
      }

      if (current == '{') {
        int end = matchingBrace(text, i);
        if (end != -1) {
          String contents = resolveBraces(text.substring(i + 1, end), resolver, depth + 1);
          out.append(resolve("%" + contents + "%", resolver));
          i = end;
          continue;
        }
      }
      out.append(current);
    }
    return out.toString();
  }

  private static int matchingBrace(String text, int open) {
    int level = 0;
    for (int i = open; i < text.length(); i++) {
      char c = text.charAt(i);
      if (c == '\\' && i + 1 < text.length() && isEscapable(text.charAt(i + 1))) {
        i++;
      } else if (c == '{') {
        level++;
      } else if (c == '}' && --level == 0) {
        return i;
      }
    }
    return -1;
  }

  private static boolean isEscapable(char c) {
    return c == '{' || c == '}' || c == '_' || c == '\\';
  }

  private static String resolve(String text, Function<String, String> resolver) {
    String result = resolver.apply(text);
    return result != null ? result : text;
  }

  private static String normalizeText(String text) {
    StringBuilder out = new StringBuilder(text.length());
    for (int i = 0; i < text.length(); i++) {
      char c = text.charAt(i);
      if (c == '\\' && i + 1 < text.length() && isEscapable(text.charAt(i + 1))) {
        out.append(text.charAt(++i));
      } else {
        out.append(c == '_' ? ' ' : c);
      }
    }
    return out.toString();
  }
}
