---
name: mvi-development
description: Implement and review MVI screens in RetailHub using Contract, State, Intent, Effect, StateFlow, ViewModels, and unidirectional data flow. Use when creating or modifying ViewModels, contracts, UI state, intents, effects, or MVI presentation logic.
---

# RetailHub MVI Development

## Required Flow

UI
↓
Intent
↓
ViewModel
↓
UseCase
↓
Repository
↓
Result
↓
ViewModel
↓
State / Effect
↓
UI

## Contract

Every screen must define a Contract containing:

- State
- Intent
- Effect

Follow existing project naming and structure.

## State

State must be immutable.

State represents persistent UI state.

Do not put one-time events in State.

## Intent

All user actions enter through Intent.

Do not expose business operations directly from ViewModel.

## Effect

Use Effect for one-time events.

Examples:

- navigation
- snackbar
- toast

Use the existing Channel/Flow implementation.

## ViewModel

The ViewModel:

- consumes intents
- calls use cases
- updates state
- emits effects

The ViewModel must not:

- render Compose UI
- access Compose APIs
- instantiate dependencies
- directly manipulate Navigator

## UI

UI:

- observes State using `collectAsStateWithLifecycle()`
- sends Intent to ViewModel
- collects Effect in a `LaunchedEffect(viewModel.effects)` block

Prefer stateless composables.

## Testing

Every meaningful MVI state transition should be tested.

Test:

- initial state
- intent
- loading
- success
- failure
- effect
- edge cases
