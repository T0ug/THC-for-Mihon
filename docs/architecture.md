# Architecture

## Project

Toug - The Hentai Comics

## Architecture Status

Architecture defined and validated incrementally with the user.

## Selected Approach

Use a single-source Mihon extension with separated internal components for sitemap index parsing, sitemap URL parsing, latest resolution, content page parsing, URL classification, and image extraction.

This approach keeps the MVP aligned with the non-goal of no custom persistent cache while providing enough structure to handle large sitemaps, image extraction fallback, and the `Latest` performance target.

## Structure

The extension is a pure Mihon source for `thehentaicomics.com`.

Layers:

- Mihon source entry point.
- Sitemap discovery.
- Latest resolution.
- Content page parsing.
- Image extraction.
- Error handling.

There is no backend, standalone UI, local database, custom persistent cache, or separate app experience.

## Components

### TougTheHentaiComics

Main source class.

Responsibilities:

- Define source name, base domain, and `pt-BR` metadata.
- Expose `Latest`.
- Keep `Popular` empty initially.
- Do not implement title search in the MVP.
- Do not implement filters in the MVP.
- Provide manga/work details, chapter list, and page list to Mihon.

### SitemapIndexParser

Responsibilities:

- Read `https://thehentaicomics.com/sitemap_index.xml`.
- Extract child sitemap URLs.
- Extract child sitemap `lastmod` values when available.
- Return normalized sitemap index entries.

### SitemapUrlParser

Responsibilities:

- Read a child sitemap.
- Extract individual content candidate URLs.
- Extract each URL's `Last Mod.` value when available.
- Return normalized sitemap URL entries.

### LatestResolver

Responsibilities:

- Coordinate `Latest` discovery.
- Prioritize the most recent child sitemaps.
- Ask `SitemapUrlParser` for URL entries.
- Ask `UrlClassifier` whether each URL is a readable content page candidate.
- Sort candidates by `Last Mod.`.
- Return around 30 initial valid entries when needed for performance.
- Try other available sitemaps if one sitemap fails.

### UrlClassifier

Responsibilities:

- Reject URLs that clearly are not readable content pages.
- Exclude known non-content patterns such as tags, categories, attachments, `attachment_id`, and administrative or structural pages.
- Allow valid content page candidates to proceed to `Latest`.

### ContentPageParser

Responsibilities:

- Load a selected content page.
- Extract title and basic metadata when available.
- Treat the page as one independent work/series.
- Expose one chapter pointing to the content page URL.

### ImageExtractor

Responsibilities:

- Extract reading image URLs from a content page.
- Primary strategy: prioritize images whose `src`, `srcset`, file name, class, or surrounding context relates to the work title or slug.
- Avoid thumbnails, cards, redirects to other works, and unrelated page images when identifiable.
- Fallback strategy: use a more permissive extraction if primary extraction fails, while still excluding obvious thumbnails and non-reading assets when possible.

## Data Flows

### Latest

1. Mihon requests `Latest`.
2. `TougTheHentaiComics` calls `LatestResolver`.
3. `LatestResolver` calls `SitemapIndexParser`.
4. `SitemapIndexParser` returns child sitemap entries.
5. `LatestResolver` prioritizes recent child sitemaps.
6. `SitemapUrlParser` extracts URL entries from selected child sitemaps.
7. `UrlClassifier` filters invalid or non-readable URLs.
8. `LatestResolver` sorts valid candidates by `Last Mod.`.
9. The source returns around 30 entries to Mihon.

### Popular

1. Mihon requests `Popular`.
2. The source returns an empty list or equivalent empty response.
3. The MVP does not infer popularity.

### Work Details

1. Mihon opens an entry from `Latest`.
2. The source loads the content page.
3. `ContentPageParser` extracts title and basic metadata.
4. The page is represented as one independent work/series.

### Chapters

1. Each work exposes one chapter.
2. The chapter URL is the work URL.
3. The MVP does not group multiple URLs into one series.

### Pages

1. Mihon requests chapter pages.
2. The source loads the work HTML.
3. `ImageExtractor` applies the primary extraction strategy.
4. If needed, `ImageExtractor` applies fallback extraction.
5. The source returns image URLs in reading order.

## Persistence

The MVP has no custom persistence.

No custom persistent data:

- No local sitemap index.
- No local database.
- No custom cache.
- No telemetry.
- No user data storage.

Temporary data exists only during request handling:

- sitemap index entries;
- candidate latest URLs;
- loaded HTML;
- extracted image URLs.

HTTP caching or Mihon internal behavior may exist, but it is not a project-level persistence requirement.

## Integrations

External integrations:

- `https://thehentaicomics.com/sitemap_index.xml`
- child sitemaps listed by the sitemap index;
- content HTML pages from `thehentaicomics.com`;
- image URLs referenced by content pages, including any site/CDN URLs present in the HTML.

Mihon integration:

- The implementation must follow the current official Mihon extension expectations and source contracts.
- The extension has no standalone UI.
- The extension has no backend intermediary.

Prohibited MVP integrations:

- external index services;
- custom proxy;
- persistent crawler;
- telemetry services;
- services unrelated to loading the selected site content.

## Error Handling

Expected failure scenarios:

- sitemap index unavailable;
- child sitemap unavailable;
- malformed or empty sitemap;
- URL without `Last Mod.`;
- content page unavailable;
- HTML without clearly related images;
- broken image URLs;
- unrelated thumbnails mixed into page content.

Behavior:

- If one child sitemap fails, try other available child sitemaps before failing.
- If a URL is clearly not readable content, ignore it.
- If a URL has no `Last Mod.`, use it only after dated entries if more entries are needed.
- If primary image extraction fails, use fallback extraction.
- If no useful images are found even after fallback, return no pages or the appropriate Mihon reading error behavior during implementation.
- If the sitemap index fails entirely, return an error to Mihon because no alternative source exists in the MVP.

## Scalability

The MVP should avoid full-site indexing before returning `Latest`.

Initial strategy:

- prioritize recent sitemaps;
- parse only enough sitemap data to return around 30 valid latest entries;
- keep all discovery live and request-scoped;
- optimize through selective loading, not persistent caching.

Future evolution can include:

- improved URL classification;
- improved image extraction heuristics;
- adjusted sitemap traversal;
- real `Latest` pagination if compatible with Mihon and the sitemap structure.

Future additions that change MVP non-goals, such as title search, tag filters, or persistent indexing, require new decisions before implementation.

## Constraints

- No title search in MVP.
- No tag or category filters in MVP.
- No populated `Popular` in MVP.
- No standalone app screen.
- No custom persistent cache or local index.
- No grouping of related URLs into shared series.
- License: MIT.
- Source metadata language: `pt-BR`.
- Distribution target: future third-party extension repository.

## Validation Notes

The architecture was validated incrementally with the user across these blocks:

- structure;
- components;
- data flow;
- persistence;
- integrations;
- error handling;
- scalability.

