# RetailHub Architectural Standards

This document defines the architectural patterns and constraints for the RetailHub project. All contributions must adhere to these standards to maintain scalability, testability, and performance.

## 1. Modularization Strategy
RetailHub uses a **Feature-Oriented Modular Architecture** with strict core capability separation.

### Module Types
- **`:composeApp`**: The orchestrator. Handles Navigation 3, global Koin DI initialization (`initKoin`), top app bar coordination, and platform entry points.
- **`:features:*`**: Contains business logic for a specific capability (e.g., `:features:home`, `:features:profile`). Each feature module contains `data`, `domain`, and `presentation` layers.
- **`:core:user`**: Centralized User domain and data capability providing `UserRepository`, `User` domain model, and `GetAuthUserUseCase` across features.
- **`:core:storage`**: Multiplatform local disk persistence for user image files (`LocalImageStorage`) using platform-specific implementations on Android and iOS.
- **`:core:ui`**: Centralized Design System, Theme, hardware Controller-Delegates, SnackBar components, and the shared Form Engine.
- **`:core:network`**: Shared Ktor HTTP client configuration (Public and Authenticated variants).
- **`:core:datastore`**: Persistent key-value storage (`TokenManager`, `PreferencesManager`) using Jetpack DataStore KMP.
- **`:core:common`**: Pure Kotlin utilities, `DispatcherProvider`, and Mapbox static map generator logic.
- **`:core:model`**: Pure Kotlin library for shared data entities (`NetworkResult`, `AuthTokens`, `NetworkClientType`). Must only contain models used by 2+ features or core modules.

---

## 2. Dependency Rules (SOLID)
- **Domain is King**: The `domain` package must be a pure Kotlin library. No imports from Ktor, DataStore, or Compose.
- **Feature Isolation**: Feature modules must **NEVER** depend directly on each other. Common capabilities or models shared across features must live in a `:core` module (e.g., `:core:user` or `:core:model`).
- **Unidirectional Dependencies**: 
  - `presentation` -> `domain`
  - `data` -> `domain`
  - `feature` -> `core`
- **Encapsulation**: Use the `internal` modifier for implementation classes (`ProfileRepositoryImpl`, `UserRemoteDataSourceImpl`). Only expose interfaces.

---

## 3. MVI Pattern (Model-View-Intent)
Every screen must follow the MVI pattern using a `Contract`:
- **State**: A single immutable data class representing the UI.
- **Intent**: User actions sent to the ViewModel.
- **Effect**: One-time side effects (Navigation, SnackBars) handled via `Channels`.

---

## 4. Navigation 3 Standard
- **Navigator**: A `@Stable` state holder class that owns a `SnapshotStateList<AppRoute>`.
- **Serialization**: All routes must be `@Serializable` sealed interfaces extending `NavKey` and handled via `rememberSerializable` to support Android process death and iOS state restoration.
- **Interface Injection**: Features must not depend on the `Navigator` class; they receive navigation lambdas or feature-specific callbacks from `App.kt`.

---

## 5. Platform Capabilities (Controller-Delegate Pattern)
To keep ViewModels platform-agnostic while accessing system APIs (Camera, Gallery, Permissions), we use the **Controller-Delegate Pattern**:
- **Controller**: A common interface (e.g., `PermissionController`, `ImagePickerController`) injected into ViewModels.
- **Delegate**: A platform-specific implementation. 
- **Binding**: On Android, delegates use a "Binder" Composable (`InitializePermissionsAndPicker()`) to link singleton/factory controllers to the `ActivityResultLauncher` during the UI lifecycle.
- **Scope**: Controllers and Delegates should be ViewModel-scoped (`factory` in Koin) or Singleton where appropriate.

---

## 6. Networking & Security
- **Dual-Client Strategy**: 
  - **Public Client**: For `/login`, `/register`, and `/refresh`.
  - **Authenticated Client**: Uses Ktor `Auth` plugin with Bearer tokens and auto-refresh interceptors.
- **Safe Requests**: All network calls must use the `safeRequest` wrapper to map exceptions to `NetworkResult`.
- **Mappers**: DTOs must be converted to Domain Models in the `data` layer before reaching `domain` or `presentation`.

---

## 7. Performance Optimization
- **Stability**: Use `@Stable` for interfaces and state holders.
- **Recomposition Guard**: Use `derivedStateOf` for UI properties derived from complex state objects.
- **List Optimization**: Use `key()` when rendering dynamic items to preserve component state.

---

## 8. Testing Requirements
- **Mokkery**: Use for mocking interfaces in `commonTest`.
- **Turbine**: Use for testing `StateFlow` transitions and `Channel` effects.
- **Real Persistence**: Use real DataStore or file storage instances with temporary files for persistence tests.
- **Dispatcher Injection**: Always inject `DispatcherProvider` for coroutine testing.

---

## 9. Resource Handling
- **Localization**: Use Compose Multiplatform Resources (`Res`).
- **ViewModels**: ViewModels should return `StringResource` identifiers from `Res` rather than raw strings to support localization.
