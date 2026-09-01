---
name: compose-ui
description: Implements Compose Multiplatform UI for RetailHub using Material 3, MVI state rendering, state hoisting, stable state holders, derivedStateOf, key, and the project's design system.
---

# RetailHub Compose UI

## UI Architecture

UI receives state and sends intents.

Prefer:

@Composable
fun Screen(
state: State,
onIntent: (Intent) -> Unit
)

Avoid business logic inside composables.

## Material

Use Material 3 and existing RetailHub design-system components.

Do not introduce custom components when an existing component already provides the required behavior.

## State

Keep UI-local state local.

Hoist state when it belongs to the ViewModel or parent.

Do not duplicate ViewModel state inside remember unnecessarily.

## Performance

Use derivedStateOf when expensive derived state benefits from it.

Use key() for dynamic lists where item identity matters.

Do not add optimizations without a reason.

## Resources

Use Compose Multiplatform Resources for user-visible strings and shared resources.

## Navigation

Screens must not depend on concrete Navigator.

Receive navigation callbacks/interfaces.
