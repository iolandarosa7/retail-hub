---
name: feature-development
description: Creates complete RetailHub feature modules following the data/domain/presentation architecture, SOLID, MVI, Koin, Ktor, and testing conventions. Use when implementing a new feature or significant feature functionality.
---

# RetailHub Feature Development

## Step 1 — Inspect

Before coding:

- inspect existing features
- inspect closest feature
- inspect Gradle dependencies
- inspect Koin modules
- inspect tests

Copy architectural patterns, not implementation blindly.

## Step 2 — Define Responsibilities

Determine:

- domain behavior
- data sources
- repository abstraction
- presentation state
- user intents
- effects
- navigation requirements

## Step 3 — Domain

Create:

- models where necessary
- repository interfaces
- use cases where meaningful
- MVI-independent business logic

Domain must remain infrastructure-free.

## Step 4 — Data

Implement:

- repository implementations
- remote data sources
- DTOs
- mapping

Use existing Ktor and DataStore infrastructure.

Implementation classes should normally be `internal`.

## Step 5 — Presentation

Create:

- Contract
- ViewModel
- Screen

Follow MVI.

## Step 6 — DI

Register dependencies using existing Koin conventions.

Do not create a separate DI mechanism.

## Step 7 — Navigation

Navigation belongs to `:composeApp`.

Features must not depend on the concrete Navigator.

Pass navigation callbacks/interfaces.

## Step 8 — Tests

Add tests for:

- domain behavior
- data behavior
- ViewModel state
- effects
- important UI behavior

## Step 9 — Verify

Run relevant tests and compilation before finishing.
