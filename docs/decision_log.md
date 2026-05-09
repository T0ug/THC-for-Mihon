# Decision Log

## 2026-05-09 - Start Project Workflow Activated

Decision: Start the project through `.agents/workflows/start_project.md` using the Discovery agent and `clarify-intent` skill.

Reason: The project is beginning from an initial idea and needs validated scope before architecture or implementation.

## 2026-05-09 - Source Selected

Decision: Use `thehentaicomics.com` as the source, starting from `https://thehentaicomics.com/sitemap_index.xml`.

Reason: The user selected this site and confirmed the sitemap index as the discovery entry point.

## 2026-05-09 - MVP Listing Scope

Decision: The MVP will expose `Latest` only, while `Popular` remains empty initially.

Reason: The sitemap provides modification dates suitable for latest ordering, but no reliable popularity data was identified.

## 2026-05-09 - No Title Search in MVP

Decision: The MVP will not include title search.

Reason: The user explicitly corrected the scope to exclude title search and focus on latest discovery.

## 2026-05-09 - No Tags or Filters in MVP

Decision: The MVP will not include tag or category filters.

Reason: Tags were considered but then explicitly removed from the MVP scope by the user.

## 2026-05-09 - Latest Discovery Method

Decision: Build `Latest` from individual sitemap URLs ordered by each URL's `Last Mod.` date.

Reason: The user clarified that dates exist on individual pages within sitemaps and should be used for ordering.

## 2026-05-09 - URL Treatment

Decision: Treat each valid content URL as an independent work/series.

Reason: The user confirmed that URLs should not be grouped into series or chapters by path.

## 2026-05-09 - Image Extraction Scope

Decision: Extract main reading images from each page by prioritizing images related to the work name, with a permissive fallback if needed.

Reason: The user showed that pages include unrelated thumbnails and redirects, so extraction must avoid blindly using all images.

## 2026-05-09 - Distribution and License

Decision: Prepare the project for future distribution in an extensions repository and use the MIT license.

Reason: The user confirmed distribution intent and selected MIT.

## 2026-05-09 - Performance Target

Decision: `Latest` should load in under 2 seconds on a normal connection, using around 30 initial entries if needed.

Reason: The user defined the performance target and accepted limiting initial entries.

## 2026-05-09 - Architecture Approach

Decision: Use a single-source Mihon extension with separated internal components for sitemap index parsing, latest resolution, content page parsing, and image extraction.

Alternatives considered:

- A minimal single-source extension with all parsing inline.
- A single-source extension with separated internal components.
- A persistent local sitemap index/cache.

Reason: The selected approach keeps the MVP aligned with the non-goal of no custom persistent cache while giving enough structure to handle large sitemaps, image extraction fallback, and the `Latest` performance target.

## 2026-05-09 - TASK-001 Validation Result

Decision: TASK-001 was approved with reservations.

Reason: The scaffold met the task scope and architecture boundaries, but the workspace does not yet include the root Gradle infrastructure required to build a Mihon/Keiyoushi extension APK.

Impact: The next task must address compatible extension build infrastructure before full source behavior is implemented.

## 2026-05-09 - TASK-002 Validation Result

Decision: TASK-002 was approved with reservations.

Reason: The imported Gradle infrastructure correctly recognizes `:src:pt:thehentaicomics`, but full `assembleDebug` is blocked because Android SDK is not configured locally.

Impact: The next task must configure or locate Android SDK before full APK build validation can pass.

## 2026-05-09 - TASK-003 Validation Result

Decision: TASK-003 was approved.

Reason: Android SDK was configured under the workspace `android/` folder, required SDK packages were installed, `local.properties` was created, and `:src:pt:thehentaicomics:assembleDebug` completed successfully.

Impact: Subsequent implementation tasks can use real Gradle build validation.

## 2026-05-09 - TASK-004 Validation Result

Decision: TASK-004 was approved.

Reason: `Latest` now traverses child sitemaps, parses individual URL entries, filters invalid URLs, sorts candidates by URL `lastmod`, limits results to around 30 entries, and builds successfully.

Impact: The next implementation task can focus on details, chapter, and reading image extraction.

## 2026-05-09 - TASK-005 Validation Result

Decision: TASK-005 was approved.

Reason: Work details parsing, one chapter per work URL, and initial image extraction were implemented within scope, documented in handoff, validated by a successful Gradle build, and checked against a live page sample.

Impact: The next task should validate the generated APK inside Mihon on an Android device before further feature work.

## 2026-05-09 - TASK-006 Validation Result

Decision: TASK-006 was approved.

Reason: The user validated the debug APK inside Mihon on Android. Mihon detected the extension, the source appeared under `pt-BR`, `Latest` loaded, works opened, chapters appeared, and reading images loaded without runtime errors.

Impact: The MVP runtime path is valid. The next task should improve missing thumbnails in `Latest` without changing MVP scope.

## 2026-05-09 - TASK-007 Validation Result

Decision: TASK-007 was approved.

Reason: `Latest` now performs request-scoped thumbnail/detail prefetch for only the first 10 final entries, falls back safely when a fetch fails, increments `extVersionCode` to 2 for Android update testing, and builds successfully.

Impact: The next step is manual Mihon retesting of `Latest` covers and load time with the regenerated APK.

## 2026-05-09 - TASK-008 Validation Result

Decision: TASK-008 was approved.

Reason: `Latest` now paginates in batches of 5, prefetches thumbnails for each returned batch, preserves safe fallback behavior, increments `extVersionCode` to 3, and generates an APK named `mihon-pt.thehentaicomics-v1.4.3-debug.apk`.

Impact: The next step is manual Mihon retesting to confirm scroll pagination, cover behavior, and load speed.

## 2026-05-09 - Popular Tab Strategy

Decision: The `Popular` tab will be populated using the site's `post_tag-sitemap.xml`.

Reason: The site does not have a native "Popular" feed. However, it exposes a tag sitemap with `lastmod` dates. We will use the most recently updated tags as a proxy for popular content, where each Mihon page request corresponds to one tag page from the sitemap (ordered by last updated).

Impact: Adds a new `PopularResolver` and enables the previously empty "Popular" tab.

## 2026-05-09 - TASK-009 Validation Result

Decision: TASK-009 was approved.

Reason: The user validated the Popular tab on Android. The feed loaded correctly, ADS elements were filtered out, and pagination worked. Additional post-task fixes included ADS filtering, custom icon injection (extVersionCode incremented to 6), and README creation.

Impact: The extension is now feature-complete for Latest and Popular. The user requested title search as the next feature.

## 2026-05-09 - Enable Title Search

Decision: Title search will be implemented using the site's native WordPress search endpoint (`?s=query`).

Reason: The user explicitly requested the ability to search works by name. This expands the project beyond the original MVP non-goal that excluded title search.

Impact: Removes "Title search" from `non_goals.md`. Adds TASK-010 to implement search. Requires a new `SearchResolver` component and modifications to `TougTheHentaiComics.kt`.
