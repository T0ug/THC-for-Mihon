# Scope

## MVP Scope

The MVP includes:

- A pure Mihon extension for `thehentaicomics.com`.
- Source metadata using `pt-BR`.
- License set to MIT.
- A `Latest` listing.
- `Popular` present but empty initially.
- Discovery through the site's sitemap index and child sitemaps.
- `Latest` entries built from individual sitemap URLs.
- Ordering of `Latest` entries by each URL's `Last Mod.` date.
- Filtering out URLs that clearly are not readable content pages, such as tags, categories, and attachments.
- Treating each valid content URL as an independent work/series.
- Exposing one readable chapter per work/series.
- Extracting main reading images from the content page.
- Prioritizing images whose file names or attributes relate to the work title.
- Avoiding thumbnails, cards, and images that redirect or point to other works when identifiable.
- Using a more permissive image extraction fallback if the primary strategy cannot identify reading images safely.

## Functional Requirements

- The extension must load in Mihon as a source.
- The extension must provide `Latest`.
- The extension must keep `Popular` empty initially.
- The extension must parse sitemap data to discover candidate content pages.
- The extension must use sitemap `Last Mod.` values to order latest items.
- The extension must limit the initial `Latest` result count to around 30 entries when needed for performance.
- The extension must try alternate available sitemaps if one sitemap fails temporarily.
- The extension must allow opening an entry and reading its images in Mihon.

## Non-Functional Requirements

### Performance

- `Latest` should load in under 2 seconds on a normal internet connection.
- If the sitemap set is too large to meet this requirement, the MVP may limit initial loading to around 30 entries.

### Scale

- The project should account for a large sitemap index.
- The MVP does not need to index the entire source before showing initial results.

### Security

- No extra security requirements were declared beyond normal extension behavior.
- The MVP should not collect telemetry.
- The MVP should not persist its own user data.

### Availability

- If one sitemap fails temporarily, the extension should try other available sitemaps before failing.

### Persistence

- The extension depends on live internet access.
- No custom persistent cache or local index is required in the MVP beyond what Mihon already provides.

### Maintenance

- The project should follow the current official Mihon extension documentation and conventions during architecture and implementation.
- Project state and decisions must remain documented in `docs/`.

## Constraints

- The project must follow the local `.agents/` pipeline.
- Architecture is not defined in the discovery phase.
- Implementation is not performed in the discovery phase.
- Technical compatibility should follow the current official Mihon extension documentation in the architecture phase.

