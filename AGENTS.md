# AGENTS.md

KutePreferences is a Material Design 3 preferences GUI library for Android, written in Kotlin and Jetpack Compose. It is
published through JitPack. See `README.md` for consumer-facing usage.

## Project layout

| Path           | Purpose                                                                                                   |
|----------------|-----------------------------------------------------------------------------------------------------------|
| `core/`        | Published library. Preference items, behaviors, persistence (`KutePreferenceDataProvider`), navigator, search. |
| `ui/`          | Published library. Compose views, theme, `KutePreferencesViewModel`, `KuteStyleManager`, `BehaviorStore`.  |
| `app/`         | Demo app using Hilt. Not published; excluded from JitPack builds through the `JITPACK` env var.             |
| `build-logic/` | Gradle convention plugins (`kutepreferences.android.*`) and the shared SDK and JVM configuration.           |

Dependency direction: `app` -> `ui` -> `core`. Never make `core` depend on `ui`.

How the pieces fit together:

- An **item** (`Kute*Preference`, `KuteCategory`, `KuteSection`, `KuteAction`) in `core` describes a list entry and owns
  its default value, description and search matching. Items that have children implement `KuteParent`.
- A **behavior** (`*Behavior`) in `core` holds the state of an item. `PersistedPreferenceBehavior` exposes
  `persistedValue` (saved) and `currentValue` (being edited).
- A **view** (composable in `ui/.../views/listitems/`) renders a behavior.
- `KuteStyleManager` maps item types to views through type hooks. Hooks run newest first, so a later hook overrides an
  earlier one. `BehaviorStore` caches one behavior per item instance.

## Setup and commands

Requires JDK 21 and the Android SDK (compile and target SDK 36, min SDK 23). Always use the Gradle wrapper.

```bash
./gradlew assembleDebug                  # build all modules and the demo APK
./gradlew testDebugUnitTest              # run all unit tests
./gradlew lintDebug                      # Android Lint; reports in <module>/build/reports/
./gradlew :core:testDebugUnitTest --tests 'de.markusressel.kutepreferences.core.SomeTest'   # run one test class
```

Run the demo app on a device or emulator:

```bash
./gradlew :app:installDebug
adb shell am start -n de.markusressel.kutepreferences.demo/de.markusressel.kutepreferences.demo.ui.MainActivity
```

There is no formatter or static analysis task (no ktlint, no detekt). Format according to `.editorconfig` and the rules
below.

## Code style

These rules describe the code as it exists. When a file is inconsistent with them, match the surrounding code and do
not reformat unrelated lines.

### Formatting

- Kotlin official code style (`kotlin.code.style=official`), configured in `.editorconfig`.
- UTF-8, LF line endings, 4 spaces, 120 character line limit. YAML and shell files use 2 spaces.
- Import order: everything else, then `java.**`, `javax.**`, `kotlin.**`, then aliases.
- Star imports are used from 5 names of the same package (3 for members), and always for `java.util`.
- Put a blank line after a class header and before its closing brace when the class has a body.
- Trailing commas are mixed. Newer code uses them in multi-line parameter and argument lists; prefer them there.

### Kotlin

- Pass arguments by name, one per line, when calling constructors and composables with several parameters.
- Use `when` rather than `if`/`else` chains and for value selection, including two-branch cases:
  `val x = when { condition -> a; else -> b }` (written over several lines).
- Negate with `.not()` (`collapsed.not()`), which is the dominant form.
- Use an expression body for short overrides (`override fun getDefaultValue() = defaultValue`) and a block body with
  `return` for anything longer.
- Mark extension points `open` so consumers can subclass them. Items and behaviors are `open class` or `abstract class`.
- Write KDoc on public interfaces and non-obvious public functions, using `[Symbol]` links. Mark notes as `// NOTE:` and
  `// TODO:`.

### Naming

| Concept          | Pattern                                | Example                                       |
|------------------|----------------------------------------|-----------------------------------------------|
| Item             | `Kute<Type>Preference`                 | `core/preference/bool/KuteBooleanPreference`  |
| Behavior         | `<Type>PreferenceBehavior`             | `core/preference/bool/BooleanPreferenceBehavior` |
| View             | `<Type>Preference` in `<Type>.kt`      | `ui/views/listitems/Boolean.kt`               |
| Theme defaults   | `KutePreferences<Area>Defaults`        | `ui/theme/KutePreferencesDialogDefaults`      |
| Use case         | `<Action>UseCase` with `operator fun invoke` | `ui/vm/SearchItemsUseCase`              |
| UI events        | Sealed class with `data object`/`data class` | `ui/vm/KuteUiEvent`                     |

Do not copy the `Silder` misspelling from existing file names (`IntSilder.kt`, `FloatSilder.kt`, `RangeSilder.kt`).

### Compose and state

- Expose state as `private val _uiState = MutableStateFlow(UiState())` plus `val uiState = _uiState.asStateFlow()`, with
  an immutable `UiState` data class. Update it with `_uiState.update { old -> old.copy(...) }`.
- Collect flows in composables with `collectAsState()` and `by` delegates.
- Accept `modifier: Modifier = Modifier` on reusable composables.
- Read colors from the theme: `MaterialTheme.colorScheme` or `LocalKuteColors.current`. Do not hardcode colors.
- Put user-visible library strings in `ui/src/main/res/values/strings.xml`.
- Give each view a `private` preview annotated with `@CombinedPreview`, wrapped in `KutePreferencesTheme`, using the
  in-memory `dummy` data provider.

## Adding a preference type

1. Add the item and behavior in `core/preference/<type>/`.
2. Add the view and its preview in `ui/views/listitems/<Type>.kt`.
3. Register it in `KuteStyleManager.registerDefaultStyles`. The `when` is order-sensitive: a subclass must come before
   its parent (for example `KuteUrlPreference` before `KuteTextPreference`).
4. Add an example to `KutePreferencesHolder` and `MainViewModel` in `app`.
5. Add the type to the table in `README.md`.

## Testing

- The repository has no tests yet, so `testDebugUnitTest` currently passes without running anything. A green run proves
  nothing unless you added tests.
- Put new unit tests in `<module>/src/test/java/`. JUnit 4 and `kotlin("test")` are available in `core` and `ui`.
- Add tests for logic you change, such as search, navigation, behaviors and data providers. `DummyDataProvider` is an
  in-memory provider for tests.
- Before finishing a change, run `./gradlew assembleDebug testDebugUnitTest lintDebug`.
- CI (`.github/workflows/build.yml`) runs only `app:testDebugUnitTest` and `app:assembleDebug`, so also run the local
  commands above.

## Compatibility rules

`core` and `ui` are public libraries, and consumers depend on what they expose.

- Treat public classes, constructor parameters and `open` members as API. Add new parameters with defaults, at a position
  that does not break existing positional callers. Call out breaking changes in the pull request description.
- Treat persisted data as a format. Non-primitive values are stored as Gson JSON, so renaming a field in
  `TimePersistenceModel` or `RangePersistenceModel` loses or corrupts saved user data. The same applies to the store name
  `kute_preferences` and to the string values behind preference keys.
- Add dependencies through `gradle/libs.versions.toml`, not with inline coordinates.
- Share build configuration through `build-logic/` rather than copying it into module build files.

## Known pitfalls

Do not copy these patterns into new code. Fix them only when that is the task.

- `GlobalScope` and `runBlocking` are used in behaviors and `DataStoreKutePreferenceDataProvider`.
- `KuteStyleManager` and `BehaviorStore` are global singletons that never release entries.
- The built-in hook's `else` branch returns `true`, so hooks registered before it never run for unknown types.
- The `onClick` and `onLongClick` parameters only take effect on `KuteCategory` and `KuteAction`. Other views ignore them.
- Preference keys are `@StringRes Int` values, resolved through a `Context`. Previews use arbitrary integers, which is
  only safe with `DummyDataProvider`.

## Security

- `KutePasswordPreference` only masks the displayed value. Both data providers store it in plaintext, and the mask reveals
  its length. Do not describe it as secure storage, and do not log preference values.
- `.ci/debug.keystore` is a debug-only key for CI builds. Never commit a release keystore or real credentials.
- CI notification credentials live in GitHub Actions secrets. Never print them or add them to `.ci/` scripts.

## Commits and pull requests

- Write short, lowercase, imperative commit subjects without a type prefix, for example `fix onClick override not working`.
- Target `master` with pull requests. Keep each one focused on a single change.
- Include screenshots in pull requests that change the UI, in both light and dark theme.
