# RetailHub Architectural Standards

This document defines the architectural patterns and constraints for the RetailHub project. All contributions must adhere to these standards to maintain scalability and performance.

## 1. Modularization Strategy
RetailHub uses a **Feature-Oriented Modular Architecture**.

### Module Types
- **`:composeApp`**: The orchestrator. Handles Navigation 3, global DI initialization, and platform entry points.
- **`:features:*`**: Contains business logic for a specific capability (e.g., `:features:auth`). Must contain `data`, `domain`, and `presentation` layers.
- **`:core:ui`**: Centralized Design System, Theme, and the shared Form Engine.
- **`:core:network`**: Shared Ktor configuration.
- **`:core:datastore`**: Persistent storage using the Platform Module pattern.
- **`:core:model`**: Pure Kotlin library for shared data entities. Must only contain models used by 2+ features.

---

## 2. Dependency Rule (SOLID)
- **Domain is King**: The `domain` package must be a pure Kotlin library. No imports from Ktor, DataStore, or Compose.
- **Unidirectional Dependencies**: 
  - `presentation` -> `domain`
  - `data` -> `domain`
  - `feature` -> `core`
- **Encapsulation**: Use the `internal` modifier for implementation classes (`RepositoryImpl`, `AuthRemoteDataSourceImpl`). Only expose interfaces.

---

## 3. MVI Pattern (Model-View-Intent)
Every screen must follow the MVI pattern using a `Contract`:
- **State**: A single immutable data class representing the UI.
- **Intent**: User actions sent to the ViewModel.
- **Effect**: One-time side effects (Navigation, Toasts) handled via `Channels`.

---

## 4. Navigation 3 Standard
- **Navigator**: A `@Stable` state holder class that owns a `SnapshotStateList<AppRoute>`.
- **Serialization**: All routes must be `@Serializable` and handled via `rememberSerializable` to support Android and iOS process death.
- **Interface Injection**: Features should not depend on the `Navigator` class; they receive navigation lambdas or feature-specific interfaces from `App.kt`.

---

## 5. Networking & Security
- **Dual-Client Strategy**: 
  - **Public Client**: For `/login`, `/register`, and `/refresh`.
  - **Authenticated Client**: Uses Ktor `Auth` plugin with Bearer tokens.
- **Token Refresh**: Must be implemented using a separate public client to avoid recursion loops.
- **Safe Requests**: All network calls must use the `safeRequest` wrapper to map exceptions to `NetworkResult`.
- **Mappers**: Implementation detail (DTOs) must be converted to Domain Models in the `data` layer before reaching the `domain` or `presentation`.

---

## 6. Performance Optimization
- **Stability**: Use `@Stable` for interfaces and abstract classes in the Form Engine.
- **Recomposition Guard**: Use `derivedStateOf` for UI properties derived from complex state objects.
- **List Optimization**: Use `key()` when rendering dynamic items (like form fields) to preserve component state.

---

## 7. Testing Requirements
- **Mokkery**: Use for mocking interfaces in `commonTest`.
- **Real Persistence**: Use real DataStore instances with temporary files for `TokenManager` tests.
- **Dispatcher Injection**: Always inject `DispatcherProvider` for coroutine testing.

---

## 8. Resource Handling
- **Localization**: Use Compose Multiplatform Resources (`Res`).
- **ViewModels**: ViewModels should return `StringResource` identifiers from `Res` rather than raw strings to support localization.
