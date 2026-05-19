# Voxly — Claude Code Guidelines

## Project Overview

**Voxly** — professional English speech coaching app for SEA professionals.
Architecture: **Kotlin Multiplatform Mobile (KMM)** with Compose Multiplatform UI.

| Module      | Purpose                                              |
|-------------|------------------------------------------------------|
| `androidApp`| Android shell — `MainActivity`, `VoxlyApp`           |
| `shared`    | All KMM code: UI, domain, data, DI                   |
| `backend`   | FastAPI Python backend (Whisper + Claude + ElevenLabs)|

**Figma source:** `https://www.figma.com/design/A4kpkA3cASFwBkyRkAUV6l/Linguo`

Package names: `com.voxly.androidapp` (Android), `com.voxly.shared` (shared module).
Build system: Gradle KTS + `gradle/libs.versions.toml`.
Min SDK: 30 · Compile SDK: 34.

### Tech Stack
- Kotlin 2.0.0 · Compose Multiplatform 1.6.11
- Voyager 1.0.0 (navigation) · Koin 3.5.6 (DI)
- Ktor 2.3.12 (HTTP) · SQLDelight 2.0.2 (local DB)
- Backend: FastAPI · SQLAlchemy async · Claude `claude-sonnet-4-6` · Whisper

---

## Architecture: MVVM + Clean Architecture

Every feature follows strict layered separation. Never let a lower layer reference a higher one.

```
androidApp/                          ← Android shell only
└── src/main/java/com/voxly/androidapp/
    ├── MainActivity.kt
    └── VoxlyApp.kt

shared/src/commonMain/kotlin/com/voxly/shared/
├── ui/
│   ├── theme/               ← VoxlyColors, VoxlyTypography, VoxlyTheme
│   ├── components/          ← All reusable atoms (VoxlyButton, ScenarioCard, etc.)
│   └── screens/
│       ├── auth/            ← Splash, Landing, SignUpMethod, SignUpDetails, OTPVerify, Login, ForgotPassword
│       ├── home/            ← HomeScreen + HomeViewModel
│       ├── learn/           ← LearnScreen + LearnViewModel
│       ├── practice/        ← ScenarioLibrary, SessionBrief, SessionActive, SessionFeedback
│       ├── progress/        ← ProgressScreen
│       └── profile/         ← ProfileScreen
├── domain/
│   ├── model/Models.kt      ← All domain models + enums
│   ├── repository/Repositories.kt  ← Repository interfaces
│   └── usecase/UseCases.kt  ← All use cases (one invoke per class)
├── data/
│   ├── remote/              ← Ktor client, DTOs, API service classes
│   └── repository/          ← Repository implementations
├── di/AppModule.kt          ← Koin modules (networkModule, repositoryModule, useCaseModule, viewModelModule)
└── sqldelight/              ← VoxlyDatabase.sq (UserEntity, SessionEntity, ChallengeEntity, StreakEntity)
```

### Layer rules

- **Domain layer**: zero Android dependencies. Pure Kotlin classes and interfaces only.
- **Data layer**: implements domain interfaces; maps DTOs/entities to domain models before returning.
- **Presentation layer**: ViewModels expose `StateFlow<UiState>`; screens observe and render — no logic in Composables.
- **Use cases**: one public `suspend operator fun invoke(...)` per class. Never hold state.

---

## Module Structure

Register every module in `settings.gradle.kts`. Prefer feature modules over a monolithic `:app`.

```
:app                  — shell only: DI graph wiring, NavGraph, Application class
:core:ui              — design system, reusable Composables, theme
:core:utils           — pure Kotlin utilities
:core:network         — Retrofit/OkHttp, serialisation
:core:data            — Room, DataStore, shared repository base classes
:feature:feature_name — self-contained feature slice (domain + data + presentation)
```

- `:app` depends on `:feature:*` and `:core:*`.
- `:feature:*` depends on `:core:*` only — never on another feature module.
- `:core:*` modules must not depend on `:feature:*` or `:app`.

---

## Kotlin Coding Standards

### Naming
- Classes/interfaces: `PascalCase`
- Functions/properties: `camelCase`
- Constants: `SCREAMING_SNAKE_CASE` in a companion object or top-level `val`
- Files: match the primary class name exactly

### Null safety
- Prefer non-null types everywhere; push nullability to the boundary (API response, DB query).
- Use `?.let`, `?: return`, or `?: throw` — never `!!` except when the null case is genuinely impossible and you can explain why.
- Model optional values with `sealed class` or Kotlin's `Result<T>`, not nullable returns.

### Coroutines
- ViewModels launch coroutines in `viewModelScope`. Repositories and use cases are suspend functions.
- Use `Dispatchers.IO` for I/O in the data layer; let the ViewModel or use case decide the dispatcher via `withContext`.
- Expose results as `StateFlow` (UI state) or `SharedFlow` (one-shot events like navigation, toasts).
- Never expose `MutableStateFlow`/`MutableSharedFlow` outside the ViewModel.

### Data classes & sealed classes
- Domain models must be `data class` (value semantics, easy diffing).
- UI state must be a `data class` wrapping all screen state in one object — not multiple `LiveData`/`StateFlow` fields.
- Use `sealed class` or `sealed interface` for events, results, and multi-branch state.

### Extension functions
- Write extensions in `:core:utils` if they are reusable; keep feature-specific extensions local.
- Never extend a class just to avoid writing a helper function — prefer a named top-level function.

### Collections & functional style
- Prefer `map`, `filter`, `flatMap`, `groupBy` over manual loops.
- Use `buildList { }` / `buildMap { }` when constructing collections imperatively.
- Avoid mutable collections as return types; return `List`, `Map`, `Set`.

---

## Jetpack Compose UI Rules

### Stateless vs stateful
- Composables should be stateless: accept state as parameters, emit events via lambdas.
- Hoist state to the ViewModel. A Composable must not call `viewModel()` deep in the tree — do it at the screen level and pass down only what's needed.

### Reusable components
- All shared UI elements live in `:core:ui`. Examples: `VoxlyButton`, `VoxlyTextField`, `LoadingOverlay`, `ErrorState`.
- Never copy-paste a Composable across features — extract it to `:core:ui` on the second use.
- Composables in `:core:ui` must have no feature-specific logic or imports.

### Component contracts
```kotlin
// Good — stateless, explicit contract
@Composable
fun VoxlyButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
)

// Bad — hides state, hard to reuse
@Composable
fun MyFeatureButton() { ... }
```

### Previews
- Every Composable in `:core:ui` must have at least one `@Preview`.
- Feature screens should have a preview using a fake `UiState`.

### Side effects
- Use `LaunchedEffect` for one-shot triggers (navigation, snackbars).
- Use `SideEffect` only for non-Compose state synchronisation.
- Never launch coroutines directly inside a Composable body.

---

## ViewModel Pattern

```kotlin
class FeatureViewModel(
    private val getSomethingUseCase: GetSomethingUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(FeatureUiState())
    val uiState: StateFlow<FeatureUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<FeatureEvent>()
    val events: SharedFlow<FeatureEvent> = _events.asSharedFlow()

    fun onAction(action: FeatureAction) {
        when (action) {
            is FeatureAction.Load -> load(action.id)
        }
    }

    private fun load(id: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            getSomethingUseCase(id)
                .onSuccess { data -> _uiState.update { it.copy(isLoading = false, data = data) } }
                .onFailure { e -> _uiState.update { it.copy(isLoading = false, error = e.message) } }
        }
    }
}
```

- One `UiState` data class per screen — never multiple flows for related state.
- Actions from the UI go through a single `onAction(action: SealedAction)` entry point.
- Events (navigate, show snackbar) go through `SharedFlow<Event>`, not state.

---

## Repository Pattern

```kotlin
// domain/repository/SomethingRepository.kt — interface only, no imports outside kotlin.*
interface SomethingRepository {
    suspend fun getSomething(id: String): Result<Something>
}

// data/repository/SomethingRepositoryImpl.kt
class SomethingRepositoryImpl(
    private val api: SomethingApi,
    private val dao: SomethingDao,
) : SomethingRepository {

    override suspend fun getSomething(id: String): Result<Something> =
        runCatching { api.fetchSomething(id).toDomain() }
}
```

- Map DTOs to domain models inside the repository — domain models never leak into the data layer.
- Use `Result<T>` for failable operations. Never throw from a repository; catch at the boundary.

---

## Use Case Pattern

```kotlin
// Single responsibility: one action, one class
class GetSomethingUseCase(private val repository: SomethingRepository) {
    suspend operator fun invoke(id: String): Result<Something> =
        repository.getSomething(id)
}
```

- Use cases may compose multiple repositories or other use cases.
- Never inject `Context` into a use case.

---

## Dependency Injection

Use Hilt throughout. Annotate:
- ViewModels with `@HiltViewModel`
- Repositories/use cases provided via `@Module` + `@Provides` or `@Binds`
- Application class with `@HiltAndroidApp`
- Activities/Fragments with `@AndroidEntryPoint`

DI modules live in the module that owns the dependency. `:app` wires cross-module bindings only when necessary.

---

## Error Handling

- Wrap all I/O with `runCatching { }` at the data boundary.
- Propagate `Result<T>` upward; the ViewModel decides how to surface errors in `UiState`.
- Never silently swallow exceptions — at minimum log them.
- Show user-facing errors via `UiState.error: String?` or a dedicated `ErrorState` Composable from `:core:ui`.

---

## Testing

- **Unit tests** cover use cases and ViewModels with fakes (not mocks) for repositories.
- **Integration tests** cover repository implementations against a real in-memory Room database.
- **UI tests** (Compose) use `createComposeRule()` and test Composables with fake state.
- Test file mirrors source path: `src/test/java/...` matches `src/main/java/...`.
- Name tests: `givenX_whenY_thenZ` or descriptive backtick strings.

---

## Gradle & Dependencies

- All versions are declared in `gradle/libs.versions.toml`. Never hardcode a version string in a `build.gradle.kts`.
- Dependencies are declared per-module — `:feature:*` modules must not pull in dependencies that belong in `:core:*`.
- Prefer `implementation` over `api`; only use `api` when the dependency is part of the module's public contract.
- Keep `isMinifyEnabled = true` and configure ProGuard rules for release builds.

---

## Code Style

- Max line length: 120 characters.
- No wildcard imports.
- `when` expressions must be exhaustive — add an `else` branch only when genuinely needed, prefer sealed types.
- Trailing lambdas outside parentheses for single-lambda calls.
- Prefer `object` declarations over companion object for singletons with no state.
- Remove unused imports, parameters, and variables before committing — treat warnings as errors.

---

## What to Avoid

- Business logic in Composables or Activities/Fragments.
- Android framework classes (`Context`, `Resources`) in domain or use case classes.
- Calling `notifyDataSetChanged()` — use `DiffUtil` or Compose's diffing automatically.
- `GlobalScope` — always use `viewModelScope`, `lifecycleScope`, or a scoped coroutine.
- Shared mutable state outside a ViewModel.
- Copy-pasting UI components — extract to `:core:ui` instead.
- `!!` (non-null assertion) — fix the nullability at the source.
