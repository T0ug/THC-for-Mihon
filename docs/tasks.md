# Tasks

## Macro Tasks

### 1. Discovery

Status: Completed

### 2. Architecture

Status: Completed

### 3. Implementation

Status: In Progress

### 4. Validation

Status: In Progress

---

# Task

## Identificacao

- ID: TASK-001
- Nome: Scaffold Mihon extension source
- Fase: Implementation
- Agente responsavel: Executor

## Objetivo

Create the initial repository structure for a single-source Mihon extension named `Toug - The Hentai Comics`.

## Status

- [ ] Nao iniciada
- [ ] Em andamento
- [x] Concluida
- [ ] Bloqueada

---

# Task

## Identificacao

- ID: TASK-002
- Nome: Add compatible extension build infrastructure
- Fase: Implementation
- Agente responsavel: Executor

## Objetivo

Add or import the repository-level build infrastructure needed to compile the extension module.

## Status

- [ ] Nao iniciada
- [ ] Em andamento
- [x] Concluida
- [ ] Bloqueada

---

# Task

## Identificacao

- ID: TASK-003
- Nome: Configure Android SDK for build validation
- Fase: Implementation environment setup
- Agente responsavel: Executor

## Objetivo

Configure a valid Android SDK path for Gradle build validation.

## Status

- [ ] Nao iniciada
- [ ] Em andamento
- [x] Concluida
- [ ] Bloqueada

---

# Task

## Identificacao

- ID: TASK-004
- Nome: Implement real Latest sitemap traversal
- Fase: Implementation
- Agente responsavel: Executor

## Objetivo

Implement real `Latest` discovery using the sitemap index and child sitemaps.

## Status

- [ ] Nao iniciada
- [ ] Em andamento
- [x] Concluida
- [ ] Bloqueada

---

# Task

## Identificacao

- ID: TASK-005
- Nome: Implement work details, single chapter, and initial image extraction
- Fase: Implementation
- Agente responsavel: Executor

## Objetivo

Implement the reading entry flow for a `Latest` item: parse work details, expose one chapter, and return reading image pages from the work HTML.

## Status

- [ ] Nao iniciada
- [ ] Em andamento
- [x] Concluida
- [ ] Bloqueada

---

# Task

## Identificacao

- ID: TASK-006
- Nome: Validate APK inside Mihon on Android device
- Fase: Runtime validation
- Agente responsavel: Executor

## Objetivo

Validate the generated debug APK inside Mihon on an Android device.

## Status

- [ ] Nao iniciada
- [ ] Em andamento
- [x] Concluida
- [ ] Bloqueada

---

# Task

## Identificacao

- ID: TASK-007
- Nome: Load limited thumbnails for Latest entries
- Fase: Implementation
- Agente responsavel: Executor

## Objetivo

Improve the `Latest` listing by loading thumbnails for a limited number of latest entries before returning the page to Mihon.

## Status

- [ ] Nao iniciada
- [ ] Em andamento
- [x] Concluida
- [ ] Bloqueada

---

# Task

## Identificacao

- ID: TASK-008
- Nome: Paginate Latest thumbnails and rename APK artifact
- Fase: Implementation
- Agente responsavel: Executor

---

## Objetivo

Change `Latest` so thumbnails are loaded in small paginated batches and remove `tachiyomi` from the generated APK filename.

---

## Contexto

TASK-007 loaded thumbnails only for the first 10 entries. The user requested a behavior closer to loading thumbnails in small batches and asked to remove `tachiyomi` from the APK file name.

Mihon does not support progressive background thumbnail loading controlled by the extension after a `MangasPage` has already been returned. The closest supported behavior is real `Latest` pagination: each requested page returns a small batch with thumbnails.

---

## Entradas

- `docs/architecture.md`
- `docs/decision_log.md`
- `docs/handoff.md`
- Existing `LatestResolver`
- Existing `TougTheHentaiComics`
- Existing Gradle infrastructure

---

## Escopo

- Implement `Latest` pagination using small pages of 5 entries.
- Fetch thumbnails/details for all entries in the current page batch.
- Preserve sitemap-based ordering by `Last Mod.`.
- Return `hasNextPage` when enough candidates exist.
- Keep failures isolated so a failed thumbnail fetch does not break the page.
- Change generated APK archive prefix for this extension from `tachiyomi` to `mihon`.
- Increment `extVersionCode`.
- Run Gradle build validation.

---

## Fora de escopo

- Do not implement title search.
- Do not implement filters/tags.
- Do not populate `Popular`.
- Do not add persistent cache/local index.
- Do not add background workers.
- Do not add custom APK icon.
- Do not change the internal package namespace required for Mihon/Tachiyomi-compatible extensions.
- Do not implement release signing/trusted repository distribution in this task.

---

## Saidas esperadas

- Mihon can request `Latest` page by page.
- Each page returns up to 5 entries with thumbnail prefetch attempted.
- APK filename no longer starts with `tachiyomi`.
- Build passes.

---

## Criterios de aceite

- `latestUpdatesRequest(page)` carries the requested page number.
- `latestUpdatesParse` resolves the correct page.
- `LatestResolver` returns paginated results with page size 5.
- Thumbnail prefetch applies to the returned page batch.
- `Popular` remains empty.
- The APK filename does not include the `tachiyomi-` prefix.
- `.\gradlew.bat --no-daemon :src:pt:thehentaicomics:assembleDebug` passes.

---

## Dependencias

- TASK-007 completed and approved.

---

## Restricoes

- Follow `docs/architecture.md`.
- Keep behavior request-scoped.
- Do not introduce persistent cache.
- Preserve compatibility with Mihon extension loading.

---

## Impacto no sistema

Improves `Latest` loading behavior by spreading thumbnail work across scroll/pagination instead of front-loading many thumbnail requests at once.

---

## Estrategia de implementacao

- Add page/page-size support to `LatestResolver`.
- Encode the Mihon page number as a query parameter in the sitemap index request.
- Read the page number back from the response request URL.
- Fetch details only for the page entries being returned.
- Add a module-level APK archive prefix override and use it in `common.gradle`.

---

## Plano de validacao

- Run Gradle build.
- Confirm generated APK filename.
- Reinstall on Android and verify `Latest` loads entries in batches with covers.

---

## Artefatos a atualizar

- `src/pt/thehentaicomics/src/eu/kanade/tachiyomi/extension/pt/thehentaicomics/LatestResolver.kt`
- `src/pt/thehentaicomics/src/eu/kanade/tachiyomi/extension/pt/thehentaicomics/TougTheHentaiComics.kt`
- `src/pt/thehentaicomics/build.gradle`
- `common.gradle`
- `docs/handoff.md`
- `docs/project_status.md`
- `docs/review_report.md`
- `docs/decision_log.md`

---

## Observacoes

The app/package may still contain Tachiyomi-compatible internal identifiers because Mihon extensions rely on that compatibility layer.

---

## Status

- [ ] Nao iniciada
- [ ] Em andamento
- [x] Concluida
- [ ] Bloqueada

---

# Task

## Identificacao

- ID: TASK-009
- Nome: Implement Popular tab using post_tag-sitemap.xml
- Fase: Implementation
- Agente responsavel: Executor

---

## Objetivo

Populate the "Popular" tab in Mihon by fetching the most recently updated tags from `post_tag-sitemap.xml` and parsing the works inside them.

---

## Contexto

The user validated the basic functionality and the `Latest` feed. Now the `Popular` feed is needed to provide more discoverability. Since the source site lacks a true "popular" page but has a tag sitemap with `lastmod` dates, we will use the most recently updated tags as a proxy for "popular/active" content.

---

## Entradas

- `https://thehentaicomics.com/post_tag-sitemap.xml`
- Existent `SitemapUrlParser.kt`
- Existent `TougTheHentaiComics.kt`
- Mihon's `popularMangaRequest(page: Int)`

---

## Escopo

- Create `PopularResolver.kt`.
- Fetch and parse `post_tag-sitemap.xml` extracting tags and sorting by `lastmod` descending.
- Resolve the tag URL corresponding to the requested Mihon page (`page - 1`).
- Fetch the tag HTML page.
- Parse the works inside the tag page using Jsoup (`.video-conteudo .thumb-conteudo a`).
- Return a `MangasPage` with `hasNextPage = true` if there are more tags.
- Integrate `PopularResolver` into `TougTheHentaiComics.kt`.

---

## Fora de escopo (CRITICO)

- Do not implement a persistent local cache for the sitemap.
- Do not implement title search or filters.
- Do not prefetch chapters for these entries.

---

## Saidas esperadas

- The "Popular" tab in Mihon loads successfully.
- Each scroll page in Mihon corresponds to the works of one tag, starting from the most recently updated tag.

---

## Criterios de aceite

- `popularMangaRequest` and `popularMangaParse` handle the request and return works successfully.
- Entries have title, url, and thumbnail correctly mapped.
- Pagination works correctly by fetching the next tag in the sorted list.
- Build passes without errors.

---

## Dependencias

- TASK-008 concluded and validated.

---

## Restricoes

- Re-use the existing HTTP client and parser logic where applicable.
- Ensure failures during tag fetch do not crash the app, but fail gracefully or return an empty page.

---

## Impacto no sistema

- Modifies `TougTheHentaiComics.kt` to delegate `Popular` to the new resolver.
- Introduces new network requests to `post_tag-sitemap.xml` and individual tag pages.

---

## Estrategia de implementacao

- Fetch `post_tag-sitemap.xml` inside `popularMangaParse` (or fetch earlier if possible).
- Extract tag URLs, sort by `lastmod`.
- Pick the target tag by index.
- Fetch tag page HTML, parse using Jsoup.

---

## Plano de validacao

- Build the project using Gradle.
- Handoff the APK to the user for manual testing on a mobile device.

---

## Artefatos a atualizar

- `src/pt/thehentaicomics/src/eu/kanade/tachiyomi/extension/pt/thehentaicomics/PopularResolver.kt` (New)
- `src/pt/thehentaicomics/src/eu/kanade/tachiyomi/extension/pt/thehentaicomics/TougTheHentaiComics.kt`
- `docs/project_status.md`
- `docs/handoff.md`

---

## Observacoes

- None.

---

## Status

- [ ] Nao iniciada
- [ ] Em andamento
- [x] Concluida
- [ ] Bloqueada

---

# Task

## Identificacao

- ID: TASK-010
- Nome: Implement title search using WordPress search endpoint
- Fase: Implementation
- Agente responsavel: Executor

---

## Objetivo

Allow users to search works by name in the Mihon search bar. The search must query the site's native WordPress search endpoint (`?s=query`) and return matching works with title, URL, and thumbnail.

---

## Contexto

Title search was originally excluded from the MVP scope (`non_goals.md`). The user has now explicitly requested it as the next feature after validating Latest and Popular feeds. The site uses a standard WordPress search at `https://thehentaicomics.com/?s=<query>` which returns an HTML results page with the same `.video-conteudo .thumb-conteudo` structure used in tag pages.

The decision to enable title search was registered in `decision_log.md` on 2026-05-09.

---

## Entradas

- Site search endpoint: `https://thehentaicomics.com/?s=<query>`
- Existing `PopularResolver.kt` HTML parsing patterns (`.video-conteudo .thumb-conteudo a`)
- Existing `TougTheHentaiComics.kt` (currently returns empty `MangasPage` for search)
- `docs/architecture.md`
- `docs/decision_log.md`

---

## Escopo

- Implement `searchMangaRequest(page, query, filters)` to build a GET request to `$baseUrl/?s=<query>&paged=<page>`.
- Implement `searchMangaParse(response)` to parse the search results HTML page.
- Extract works using the same Jsoup selectors and ADS filtering logic already used in `PopularResolver`.
- Support pagination if the site provides a "next page" link on search results.
- Filter out ADS entries (elements containing `.selo` with "ADS" text, `.thumb-ads`, external domain links).
- Increment `extVersionCode`.
- Run Gradle build validation.

---

## Fora de escopo (CRITICO)

- Do not implement advanced filters (tags, categories, date ranges).
- Do not implement autocomplete or search suggestions.
- Do not cache search results locally.
- Do not modify Latest or Popular behavior.

---

## Saidas esperadas

- Mihon's search bar returns matching works when a user types a title query.
- Results display title, URL, and thumbnail correctly.
- ADS entries are excluded from results.
- Pagination works if the site provides multiple result pages.

---

## Criterios de aceite

- `searchMangaRequest` builds a valid URL with the user query URL-encoded.
- `searchMangaParse` returns a `MangasPage` with correct entries.
- ADS filtering is active on search results (same rules as Popular).
- Empty queries return empty results gracefully.
- Build passes without errors: `.\\gradlew.bat --no-daemon :src:pt:thehentaicomics:assembleDebug`.

---

## Dependencias

- TASK-009 completed and validated.
- Decision "Enable Title Search" registered in `decision_log.md`.

---

## Restricoes

- Follow `docs/architecture.md`.
- Reuse existing HTTP client (`fetchBody`) and ADS filtering logic.
- Keep behavior request-scoped (no persistent cache).
- Preserve compatibility with Mihon extension loading.
- Do not break existing Latest or Popular feeds.

---

## Impacto no sistema

- Modifies `TougTheHentaiComics.kt` to implement real search request/parse methods.
- May introduce a `SearchResolver.kt` or inline the logic if simple enough.
- Adds new network requests to the WordPress search endpoint.

---

## Estrategia de implementacao

- Build the search URL using `$baseUrl/?s=<encoded_query>&paged=<page>`.
- In `searchMangaParse`, load the response HTML body with Jsoup.
- Select work entries using `.video-conteudo .thumb-conteudo a` (same as Popular/tag pages).
- Apply ADS filtering (skip entries with `.selo` "ADS", `.thumb-ads`, external links).
- Extract title from `a` title attribute or inner text, URL from `a` href, thumbnail from `img` src.
- Detect "next page" via pagination links to set `hasNextPage`.
- If no results match, return `MangasPage(emptyList(), false)`.

---

## Plano de validacao

- Run Gradle build.
- Handoff the APK to the user for manual testing on mobile device.
- User searches for a known title (e.g., "Dragon Ball") and verifies results.
- User searches for a non-existent title and verifies empty results.

---

## Artefatos a atualizar

- `src/pt/thehentaicomics/src/eu/kanade/tachiyomi/extension/pt/thehentaicomics/TougTheHentaiComics.kt`
- `src/pt/thehentaicomics/build.gradle`
- `docs/handoff.md`
- `docs/project_status.md`

---

## Observacoes

- The site uses Cloudflare, which may occasionally return 520 errors. The extension should handle these gracefully (return empty results, not crash).
- WordPress search supports pagination via `?paged=2`, `?paged=3`, etc.

---

## Status

- [ ] Nao iniciada
- [ ] Em andamento
- [x] Concluida
- [ ] Bloqueada
