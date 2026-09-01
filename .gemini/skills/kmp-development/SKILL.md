---
name: kmp-development
description: Implements Kotlin Multiplatform code for RetailHub while maintaining commonMain portability, platform boundaries, expect/actual conventions, coroutine usage, and shared business logic.
---

# RetailHub KMP Development

## commonMain

Prefer commonMain.

Do not use:

- Android APIs
- JVM-only APIs
- iOS APIs

in commonMain.

## Platform Code

Use platform source sets only when necessary.

Use the existing expect/actual pattern for platform-specific infrastructure.

## Business Logic

Business logic should remain shared.

Do not duplicate business logic between Android and iOS.

## Coroutines

Use injected DispatcherProvider.

Do not hardcode dispatchers in testable business logic.

## Serialization

Use Kotlin Serialization following existing project conventions.

## Testing

Prefer commonTest for platform-independent behavior.
