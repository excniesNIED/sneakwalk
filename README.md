# Sneak Walk

Client-side third-person crouch-walk pose override for Java Edition.

## What it does

The observed HuaYuTing (GermMod-Hyt) and EaseCation Java clients do not use the
vanilla `ModelBiped`/`PlayerModel` limb swing while crouching. Their custom
player/armour models run their own limb rotations, so the crouch-walk cycle
looks different.

This mod applies the same idea to the vanilla player model, for every player:

- `mode=germ` (default): GermMod custom-bone amplitudes
  - legs: `cos(limbSwing * 0.6662) * 1.05 * limbSwingAmount`
  - arms: `cos(limbSwing * 0.6662) * 0.75 * limbSwingAmount`
  - the vanilla sneak base pose stays untouched
- `mode=bedrock`: EC Java / ViaBedrockUtility `animation.player.sneaking`
  - legs: `-28° + cos(...) * 1.4 * limbSwingAmount`
  - arms: `-5.7° + cos(...) * 1.0 * limbSwingAmount`
- `mode=vanilla`: no limb override

Optional legacy features (high versions, default off):

- `sneakCamera=legacy18`: 1.8 eye height (1.54 crouch / 1.62 stand) and immediate camera
- `sneakCamera=snap`: native eye height, no smoothing (HeyPixel `disableSneakAnimation`)
- `legacySneakPose=true`: 1.8 third-person body/head/leg base pose
- `legacySwing=true`: 1.8 arm swing duration (6 ticks, haste/mining-fatigue modified)
- `legacySwordBlock=true`: 1.8 sword blocking / block-hit animation

1.8.9 and 1.12.2 already use the 1.8 camera/swing/blocking/pose; only the
crouch-walk limb override applies there.

## Config

`config/sneakwalk.properties` is created on first launch.

```properties
enabled=true
mode=germ              # germ | bedrock | vanilla
trigger=sneaking       # sneaking | always
swapSides=false
affectLegs=true
affectArms=true
sneakCamera=smooth     # smooth | snap | legacy18
legacySneakPose=false
legacySwing=false
legacySwordBlock=false
```

Restart the client after editing.

## Build matrix

| Minecraft | Loaders | Module |
| --- | --- | --- |
| 1.8.9 | Forge | `legacy-1.8.9` |
| 1.12.2 | Forge | `legacy-1.12.2` |
| 1.18.2 | Fabric, Forge | `fabric/v1_18_2`, `forge/v1_18_2` |
| 1.20.1 | Fabric, Forge | `fabric/v1_20_1`, `forge/v1_20_1` |
| 1.20.4 | Fabric, Forge | `fabric/v1_20_4`, `forge/v1_20_4` |
| 1.21.1 | Fabric, NeoForge | `fabric/v1_21_1`, `neoforge/v1_21_1` |
| 1.21.4 | Fabric, NeoForge | `fabric/v1_21_4`, `neoforge/v1_21_4` |
| 1.21.8 (EC source) | Fabric, NeoForge | `fabric/v1_21_8`, `neoforge/v1_21_8` |
| 26.1.2 | Fabric, NeoForge | `fabric/v26_1_2`, `neoforge/v26_1_2` |
| 26.2 | Fabric, NeoForge | `fabric/v26_2_0`, `neoforge/v26_2_0` |
| 26.3 | Fabric, NeoForge | `fabric/v26_3_0`, `neoforge/v26_3_0` |

## Build

```bash
# Fabric (Gradle 9.2.0, JDK 17/21/25 toolchains)
cd fabric && ~/.gradle-dist/gradle-9.2.0/bin/gradle build

# Forge (Gradle 8.10.2, JDK 17)
cd forge && ~/.gradle-dist/gradle-8.10.2/bin/gradle build

# NeoForge (Gradle 8.10.2, JDK 21/25)
cd neoforge && ~/.gradle-dist/gradle-8.10.2/bin/gradle build

# Legacy (Gradle 4.10.3, JDK 8)
cd legacy-1.8.9 && JAVA_HOME=~/.jdks/temurin-8 ../.gradle-dist/gradle-4.10.3/bin/gradle build
cd legacy-1.12.2 && JAVA_HOME=~/.jdks/temurin-8 ../.gradle-dist/gradle-4.10.3/bin/gradle build
```

## License

MIT. Author: hxcn.
