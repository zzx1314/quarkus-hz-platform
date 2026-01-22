# AGENTS.md

This document provides guidelines for agentic coding assistants working in this repository.

## Project Overview

Vue 3 + TypeScript + Vite admin panel using Element Plus, Pinia, VueUse, and TailwindCSS. Uses pnpm as package manager (v9+).

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
- API response codes: `SUCCESS = 10200`, `FAIL = 10400` (see `src/api/base.ts`)
- **Import paths**: Omit file extensions (`.ts`, `.tsx`, `.vue`) - `tsconfig.json` uses `moduleResolution: "bundler"` which auto-resolves extensions for Vite

### Vue Components

- Use `<script setup lang="ts">` for all new components
- Props typing: use `PropType<T>` or define outside component
- Emit types: define with `type EmitType = (e: 'change', value: T) => void`
- Multi-word component names: `MyComponent.vue` (enforced off in ESLint)
- Place components in `src/components/` with PascalCase names
- Icons: use `src/components/ReIcon/` for icon components

### Naming Conventions

- Files: `PascalCase` for components, `camelCase` for utilities, `kebab-case` for routes
- Variables/functions: `camelCase`
- Constants: `SCREAMING_SNAKE_CASE`
- CSS classes: Tailwind utility classes preferred
- API files: `src/api/*.ts` with descriptive names (e.g., `dronesDevice.ts`, `system.ts`)
- Store modules: `src/store/modules/*.ts` (e.g., `user.ts`, `permission.ts`)

### Error Handling

- Use `try/catch` with typed error objects
- Log errors with context, don't use bare `console.error`
- Handle promise rejections explicitly
- Provide user-friendly error messages in UI
- Check API responses against `SUCCESS` code from `src/api/base.ts`

### CSS/SCSS

- Use TailwindCSS utility classes when possible
- SCSS for component-specific styles in `src/style/`
- Global styles: `src/style/index.scss`
- Theme variables: `src/style/theme.scss`
- Dark mode: `src/style/dark.scss`
- Follow stylelint config with recess-order property ordering

### API Design

- Place API functions in `src/api/*.ts`
- Use axios instance from `@/[module]/utils/service` pattern
- Return types should be explicit
- Example:

```typescript
import { service } from "@/api/service";
import type { AxiosRequestConfig } from "axios";

export function getDronesList(params?: object, config?: AxiosRequestConfig) {
  return service.get("/drones/list", { params, ...config });
}
```

### Store (Pinia)

- Store modules in `src/store/modules/*.ts`
- Use `defineStore` with composition API pattern
- Types in `src/store/types.ts`
- State interface naming: `*State` (e.g., `UserState`)

### Router

- Route modules in `src/router/routes/modules/*.ts`
- Use lazy loading for pages: `component: () => import("@/views/...")`
- Meta types: define in `src/router/types.ts` if needed

## Project Structure

```
src/
├── api/              # API functions
├── assets/           # Static assets
├── components/       # Reusable components
├── hooks/            # Composable hooks
├── layout/           # Layout components
├── router/           # Vue Router config
├── store/            # Pinia stores (modules/)
├── style/            # Global styles
├── utils/            # Utility functions
└── views/            # Page components
```

## Git Hooks

- Husky + lint-staged run on commit:
  - `.vue`: prettier → eslint → stylelint
  - `.{js,ts,jsx,tsx}`: prettier → eslint
  - `.{css,scss,html}`: prettier → stylelint

## Tech Stack

- Node: `^18.18.0 || ^20.9.0 || >=22.0.0`
- Package manager: pnpm 9+
- Build tool: Vite 6
- Framework: Vue 3.5+
- State: Pinia
- UI: Element Plus + TailwindCSS
- Router: Vue Router 4
- Utils: VueUse

## Notes

- No test framework currently configured
- No Cursor/Copilot rules files present
- Run `pnpm lint` before committing
- Use `@/` alias for `src/` in imports
