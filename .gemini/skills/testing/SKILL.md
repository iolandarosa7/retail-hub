---
name: testing
description: Creates and reviews KMP commonTest, MVI ViewModel tests, Ktor MockEngine tests, DataStore tests, Koin test setup, and Compose UI tests for RetailHub.
---

# RetailHub Testing

## Principles

Test behavior, not implementation.

Prefer commonTest.

Tests must be deterministic.

Do not use arbitrary sleeps.

---

## MVI Tests

Use:

runTest

and injected:

DispatcherProvider

Verify:

Intent
→ State

and:

Intent
→ Effect

---

## Mocking

Use Mokkery.

Mock interfaces.

Prefer fakes when they improve readability.

---

## Ktor

Use Ktor MockEngine.

Do not make real network calls from unit tests.

Test:

- HTTP status
- response body
- errors
- mapping
- authentication behavior where applicable

---

## DataStore

Use real DataStore instances with temporary files.

Test actual persistence behavior.

Do not replace DataStore with a mock when persistence behavior is what is being tested.

---

## Koin

Use isolated Koin contexts for tests when appropriate.

Override production dependencies explicitly.

Do not depend on a globally started Koin application.

---

## Compose

Test user-visible behavior.

Prefer:

click
→ state change
→ visible UI

Do not test framework behavior.

---

## Navigation

Test navigation through user interaction.

Do not couple feature tests to the concrete Navigator.

The Navigator itself should have focused unit tests.
