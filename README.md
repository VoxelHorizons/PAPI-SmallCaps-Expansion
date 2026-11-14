# SmallCaps PlaceholderAPI Expansion

Converts text to Unicode small caps through the `%smallcaps_...%` placeholder.

Requires **PlaceholderAPI** only. **ViaVersion is not required.**

## Downloading a built JAR

The [Build SmallCaps workflow](https://github.com/VoxelHorizons/PAPI-SmallCaps-Expansion/actions/workflows/gradle.yml) automatically builds the plugin:

- For each opened or updated pull request.
- After a merge or other push to `main`.
- Manually via **Actions → Build SmallCaps → Run workflow**.

Open a successful workflow run, scroll to **Artifacts**, and download **SmallCaps-1.0.3**. Unzip the artifact to get `PAPI-Expansion-SmallCaps-1.0.3.jar`.

Pushes to `main` also publish the same tested JAR as a prerelease asset on the [Releases page](https://github.com/VoxelHorizons/PAPI-SmallCaps-Expansion/releases).

If the repository's Actions tab has no workflows or runs, fork workflows may be disabled. An administrator needs to enable workflows in the **Actions** tab or **Settings → Actions → General**, and ensure the organisation permits the workflow's actions (`actions/checkout@v4`, `actions/setup-java@v4`, `gradle/actions/setup-gradle@v4`, `actions/upload-artifact@v4`, `actions/download-artifact@v4`, and `softprops/action-gh-release@v2`).

## Installation

Put the JAR in the server's top-level `plugins/` directory and restart. Do not put this standalone plugin JAR into `plugins/PlaceholderAPI/expansions/`.

Quick check:

```text
/papi parse me %smallcaps_HELLO_WORLD%
```

This version always converts text to small caps regardless of the player's protocol. Older clients may not support every Unicode glyph.
