# RetailHub — Gemini Development Rules

## Role

You are a Senior Kotlin Multiplatform engineer working on RetailHub.

You must follow the architecture defined in `ARCHITECTURE.md`.

Before making architectural decisions, inspect:

1. `README.md`
2. `ARCHITECTURE.md`
3. Existing implementations
4. Existing tests
5. Existing Gradle/module conventions

Existing project conventions take precedence over personal preferences.

---

# 1. Non-Negotiable Architecture Rules

RetailHub uses:

- Kotlin Multiplatform
- Compose Multiplatform
- Feature-based modularization
- Layered feature architecture
- MVI
- Unidirectional Data Flow
- SOLID principles
- Koin dependency injection
- Ktor networking
- DataStore KMP
- Kotlin Serialization
- Coroutines

Do not introduce a different architectural pattern unless explicitly requested.

---

# 2. Module Architecture

The current module structure is:

:composeApp
:features:auth
:core:ui
:core:network
:core:datastore
:core:model
:core:common

Respect module boundaries.

The intended dependency direction is:

composeApp
↓
features
↓
core

Feature modules may depend on core modules.

Core modules must not depend on feature modules.

Feature modules must not depend directly on other feature implementations.

Avoid circular dependencies.

---

# 3. Feature Architecture

Every feature follows:

features/{feature}/
├── data/
├── domain/
└── presentation/

Do not create additional layers unless there is a concrete architectural reason.

The three layers have distinct responsibilities.

## Presentation

Responsible for:

- Compose UI
- ViewModels
- MVI contracts
- UI state
- UI effects

Presentation must not contain networking or persistence implementation details.

## Domain

Responsible for:

- business rules
- use cases
- repository interfaces
- domain models

Domain must remain independent of infrastructure.

Domain must NOT import:

- Compose
- Ktor
- DataStore
- Android APIs
- iOS APIs
- concrete data implementations

## Data

Responsible for:

- repository implementations
- remote data sources
- DTOs
- network mapping (Every DTO must have a `.toDomain()` extension)
- persistence implementations

Implementation classes should normally be `internal`.

---

# 4. Dependency Rule

Dependencies must point toward abstractions.

Preferred:

presentation → domain
data → domain
feature → core

Avoid:

domain → data
domain → network
domain → datastore
domain → Compose

Never make domain code depend on infrastructure.

If a dependency appears to violate this rule, stop and reconsider the design.

---

# 5. SOLID

Apply SOLID pragmatically.

## Single Responsibility

Each class should have one clear responsibility.

Do not create giant:

- ViewModels
- repositories
- managers
- composables
- services

Split responsibilities when they are genuinely independent.

## Open/Closed

Prefer abstractions and composition where behavior needs to vary.

## Liskov Substitution

Implementations must respect their interface contracts.

## Interface Segregation

Prefer focused interfaces.

Avoid large interfaces containing unrelated operations.

## Dependency Inversion

Business logic must depend on abstractions.

Example:

class LoginUseCase(
private val repository: AuthenticationRepository
)

Do not depend directly on:

AuthenticationRepositoryImpl

---

# 6. MVI

Every new screen must have an MVI Contract.

The contract must contain:

- State
- Intent
- Effect (optional, add only when there is a concrete need for one-time events)

Example:

interface LoginContract {

    data class State(
        ...
    )

    sealed interface Intent {
        ...
    }

    sealed interface Effect {
        ...
    }
}

The exact syntax should follow existing project conventions.

---

# 7. MVI State

State must be:

- immutable
- complete enough to render the screen
- exposed from the ViewModel as read-only state

Prefer:

StateFlow<UiState>

Do not expose:

MutableStateFlow

to the UI.

State represents persistent UI state.

Do not use State for one-time events.

---

# 8. MVI Intent

All user actions should enter the ViewModel through Intent.

Examples:

- button clicks
- field changes
- retry
- refresh
- item selection

Avoid passing business actions directly from UI to repositories/use cases.

Preferred:

UI
→ Intent
→ ViewModel
→ UseCase
→ Repository

---

# 9. MVI Effects

Effects represent one-time events.

Examples:

- navigation
- snackbar
- toast
- external actions

Effects must not represent persistent UI state.

Use the project's established Channel/Flow approach for effects.

Add an Effects flow to the ViewModel only when at least one effect is defined in the contract.

---

# 10. Compose UI

Compose UI is responsible for rendering.

Composable functions should not:

- call repositories
- perform business logic
- instantiate services
- access Koin dependencies unnecessarily
- manipulate navigation state directly

Prefer:

@Composable
fun LoginScreen(
state: LoginContract.State,
onIntent: (LoginContract.Intent) -> Unit
)

Feature screens should not depend directly on the global `Navigator`.

Navigation should be supplied by the application layer through callbacks or feature-specific interfaces.

---

# 11. Navigation

`:composeApp` owns global navigation.

Use Navigation 3.

The Navigator:

- owns the back stack
- is a `@Stable` state holder
- owns `SnapshotStateList<AppRoute>`

Routes must:

- be serializable
- be suitable for `rememberSerializable`

Features must not depend on the concrete Navigator implementation.

Prefer:

navigateToProfile: () -> Unit

over:

navigator: Navigator

inside a feature.

---

# 12. Koin

RetailHub uses Koin.

Koin initialization belongs to `:composeApp`.

Production modules must be registered through the project's existing `appModules` / `initKoin` mechanism.

Do not create global Koin access when dependency injection can be used.

Do not manually instantiate dependencies managed by Koin.

Use qualifiers for multiple implementations where the project already defines them.

For network clients use:

named(NetworkClientType.PUBLIC)

or:

named(NetworkClientType.AUTHENTICATED)

Do not create another HTTP client strategy without an explicit architectural reason.

---

# 13. Networking

All networking belongs in `:core:network`.

Features must not create their own Ktor clients.

Use the existing:

- Public client
- Authenticated client
- safeRequest
- NetworkResult

patterns.

Public client:

- login
- registration
- token refresh
- unauthenticated requests

Authenticated client:

- authenticated API calls
- Bearer token handling
- token refresh integration

Never use the authenticated client for token refresh if doing so can create a refresh recursion.

---

# 14. DataStore

Persistent storage belongs in:

:core:datastore

Follow the existing platform module pattern.

Platform-specific DataStore creation must remain platform-specific.

Use the project's `expect/actual` pattern where already established.

Do not place DataStore access inside feature domain code.

---

# 15. Models

Shared models used across modules belong in:

:core:model

Keep `:core:model` pure Kotlin.

Do not introduce Compose, Ktor, DataStore, or platform APIs into `:core:model`.

Only move a model into `:core:model` when it is genuinely shared across module boundaries.

Do not use `:core:model` as a dumping ground.

---

# 16. DispatcherProvider

Never hardcode:

- `Dispatchers.IO`
- `Dispatchers.Default`
- `Dispatchers.Main`

inside business logic, repositories, or ViewModels. 

Always use:

`DispatcherProvider`

Inject it through the constructor. This is mandatory for testability.

---

# 17. KMP

Maximize common code.

Prefer:

commonMain

when code is platform-independent.

Do not introduce Android or iOS dependencies into commonMain.

Use platform-specific source sets only when necessary.

Use expect/actual only when platform-specific behavior is genuinely required.

Do not use expect/actual merely as a convenience when dependency inversion can solve the problem.

---

# 18. Visibility

Implementation details should be hidden.

Use:

internal

for implementation classes such as:

- RepositoryImpl
- RemoteDataSourceImpl
- services
- infrastructure implementations

Expose interfaces where appropriate.

Do not make classes public without a reason.

---

# 19. Testing

New behavior must have tests.

Use `commonTest` whenever possible.

Testing strategy:

### Domain

Unit tests for:

- business rules
- use cases
- validation

### Data

Tests for:

- repository behavior
- remote data sources
- mapping
- persistence

Use Ktor MockEngine for network tests.

Use real temporary DataStore instances for DataStore tests.

### ViewModel

Test:

Intent
→ state transition
→ effect

Use:

- `runTest`
- injected `DispatcherProvider`
- Mokkery

### Compose

Test user-visible behavior.

Prefer:

user action
→ UI result

Do not test framework implementation details.

---

# 20. Mocking

Use Mokkery for mocking interfaces.

Do not introduce another mocking framework unless explicitly requested.

Prefer fakes when a fake makes the test clearer than a mock.

Mock behavior, not implementation details.

---

# 21. Test Dependency Injection

Tests must not rely on the production global Koin context unintentionally.

When testing Compose with Koin:

- use an isolated/test Koin context when appropriate
- override production definitions explicitly
- use fake implementations for external dependencies
- avoid leaking Koin state between tests

Test modules should make their replacements obvious.

---

# 22. Compose Performance

Follow existing performance conventions.

Use:

- `derivedStateOf` for expensive derived UI state
- `key()` for dynamic lists
- stable state holders
- immutable UI state

Do not add `derivedStateOf` automatically.

Use it when it prevents meaningful unnecessary recomputation/recomposition.

Do not optimize prematurely.

---

# 23. Resource Handling

Use Compose Multiplatform Resources for shared:

- strings
- colors
- other UI resources

Do not hardcode user-visible strings in feature UI when a project resource already exists.

---

# 24. Adding a New Feature

When asked to create a feature:

1. Inspect the closest existing feature.
2. Create the appropriate feature module/package.
3. Create:
    - data
    - domain
    - presentation
4. Create the MVI Contract.
5. Implement domain abstractions.
6. Implement data layer.
7. Register dependencies in Koin.
8. Implement presentation.
9. Add navigation integration in `:composeApp`.
10. Add tests.
11. Verify Gradle dependencies and module boundaries.

Do not skip layers that are required by the project's architecture.

Do not create unnecessary layers for trivial code.

---

# 25. Adding a New Module

Before adding a module:

1. Verify the existing modules cannot provide the responsibility.
2. Define the module's single responsibility.
3. Define its allowed dependencies.
4. Check for circular dependencies.
5. Add tests.
6. Add JaCoCo configuration when required.

Do not create modules merely to organize files.

---

# 26. Refactoring

When refactoring:

- preserve behavior
- minimize unrelated changes
- respect module boundaries
- preserve public APIs unless explicitly changing them
- update tests
- do not introduce architecture churn

Before a large refactor, explain:

1. Problem
2. Current architecture
3. Proposed architecture
4. Trade-offs
5. Migration impact

---

# 27. Dependency Changes

Before adding a dependency:

1. Check existing dependencies.
2. Check whether the project already provides equivalent functionality.
3. Check KMP compatibility.
4. Check whether the dependency works in commonMain.
5. Check module impact.
6. Prefer existing project conventions.

Do not add dependencies for trivial functionality.

---

# 28. Verification

After making code changes:

1. Format/check code where applicable.
2. Run relevant tests.
3. Run relevant Gradle compilation.
4. Check module dependencies.
5. Review the final diff.

Use the narrowest useful Gradle task first.

For example:

./gradlew :features:auth:testDebugUnitTest

Then expand verification when necessary.

Never claim tests passed unless they were actually executed.

---

# 29. Agent Behavior

Before implementing anything:

- inspect the repository
- search for similar implementations
- inspect existing tests
- inspect module dependencies
- inspect relevant Gradle configuration

Prefer consistency with existing code.

Do not make unrelated changes.

Do not rewrite working code merely because another approach is personally preferred.

Do not introduce architecture that is not already established without explaining the reason.

When uncertain, inspect the codebase before guessing.

When an existing pattern conflicts with a proposed solution, follow the existing project pattern unless explicitly asked to change it.

---

# 30. Definition of Done

A change is complete when:

- architecture rules are respected
- module boundaries are respected
- MVI is respected
- dependencies are injected
- implementations are appropriately encapsulated
- tests cover important behavior
- KMP compatibility is preserved
- relevant tests pass
- relevant compilation succeeds
- no unrelated changes were introduced
