# AGENTS.md

This document provides guidelines for agentic coding assistants working in this repository.

## Project Overview

Vue 3 + TypeScript + Vite admin panel using Element Plus, Pinia, and TailwindCSS. Uses pnpm as package manager (v9+).

## Commands

### Development

```bash
pnpm dev              # Start dev server with 4GB memory limit
pnpm serve            # Alias for dev
```

### Build

```bash
pnpm build            # Production build with version file generation (8GB memory)
pnpm build:staging    # Staging build
pnpm report           # Build with bundle analysis
pnpm preview          # Preview production build locally
pnpm preview:build    # Build then preview
```

### Linting & Type Checking

```bash
pnpm typecheck        # Run TypeScript and Vue-TSC type checks (noEmit)
pnpm lint:eslint      # Lint JS/TS/Vue files (auto-fix)
pnpm lint:prettier    # Format code with Prettier
pnpm lint:stylelint   # Lint CSS/SCSS/Vue styles (auto-fix)
pnpm lint             # Run all linters sequentially
```

### Utilities

```bash
pnpm svgo             # Optimize SVG files
pnpm clean:cache      # Clear caches and reinstall dependencies
```

## Code Style

### Imports

- Use TypeScript `import type` for type-only imports
- Group imports: external libs → internal imports → styles
- Example:

```typescript
import { defineStore } from "pinia";
import type { UserState } from "./types";
import { useUserApi } from "@/api/user";
import "./styles/index.scss";
```

### Formatting (Prettier)

- Brackets: `bracketSpacing: true`
- Quotes: `singleQuote: false` (use double quotes)
- Trailing comma: `none`
- Arrow functions: `arrowParens: "avoid"`

### TypeScript

- `strict: false` enabled, but prefer explicit types for props/returns
- Avoid `any` when possible; use `unknown` or explicit types
- Use `const` for enum members: `enum Foo { A = "a" }`
- Unused vars: prefix with `_` to ignore (`argsIgnorePattern: "^_"`)
- Consistent type imports: `import type { Foo } from "..."`

### Vue Components

- Use `<script setup lang="ts">` for all new components
- Props typing: use `PropType<T>` or define outside component
- Emit types: define with `type EmitType = (e: 'change', value: T) => void`
- Multi-word component names: `MyComponent.vue` (enforced off in ESLint)

### Naming Conventions

- Files: `PascalCase` for components, `camelCase` for utilities
- Variables/functions: `camelCase`
- Constants: `SCREAMING_SNAKE_CASE`
- CSS classes: BEM-style or Tailwind utility classes

### Error Handling

- Use `try/catch` with typed error objects
- Log errors with context, don't use bare `console.error`
- Handle promise rejections explicitly
- Provide user-friendly error messages in UI

### CSS/SCSS

- Use TailwindCSS utility classes when possible
- SCSS for component-specific styles
- Follow stylelint config with recess-order property ordering
- Use SCSS variables from `@/style/variables/index.scss`

### Git Hooks

- Husky + lint-staged run on commit:
  - `.vue`: prettier → eslint → stylelint
  - `.{js,ts,jsx,tsx}`: prettier → eslint
  - `.{css,scss,html}`: prettier → stylelint

### Misc

- Node: `^18.18.0 || ^20.9.0 || >=22.0.0`
- Build tool: Vite 6
- State: Pinia stores in `src/store/modules/`
- Router: `src/router/routes/modules/*.ts`
- Utils: `src/utils/` (use `@/` alias for `src/`)

### Notes

- No test framework currently configured
- No Cursor/Copilot rules files present
