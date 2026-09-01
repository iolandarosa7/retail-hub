---
name: architecture-review
description: Reviews RetailHub code for module violations, SOLID violations, MVI violations, dependency direction, KMP portability, DI problems, navigation coupling, and testability.
---

# RetailHub Architecture Review

Review the code against:

1. Module boundaries
2. Dependency direction
3. SOLID
4. MVI
5. KMP portability
6. Koin usage
7. Navigation
8. Testability
9. Compose architecture

## Check Module Boundaries

Verify:

composeApp → features → core

Verify core does not depend on features.

Verify domain does not depend on infrastructure.

## Check Domain

Flag imports from:

- Compose
- Ktor
- DataStore
- Android
- iOS

## Check MVI

Flag:

- business logic in UI
- mutable state exposed publicly
- missing Contract
- State used as Effect
- Effect used as State
- direct repository access from UI
- ViewModel creating dependencies

## Check Navigation

Flag feature dependencies on concrete Navigator.

Prefer callbacks/interfaces.

## Check SOLID

Identify concrete violations rather than recommending abstractions automatically.

## Output

For each issue provide:

- Severity
- Location
- Violation
- Why it matters
- Recommended fix

Do not refactor automatically unless requested.
