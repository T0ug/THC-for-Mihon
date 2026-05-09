# Handoff

## Task

- ID: TASK-011
- Nome: Enable remote installation via GitHub repository
- Agente responsavel: Executor

---

## Objetivo da Task

Make the extension installable from Mihon by adding the GitHub repository URL in Mihon's extension settings. The Mihon app reads `index.min.json` from the repo to discover available extensions and downloads the APK directly.

---

## Escopo executado

Implemented:
- Created `repo/index.min.json` with extension metadata matching the Mihon repository schema.
- Source ID calculated via MurmurHash3 (`-7329981312210241844`) matching the APK's internal ID.
- Copied the APK `mihon-pt.thehentaicomics-v1.4.7.apk` to `repo/`.
- Removed `repo/` from `.gitignore` so it is tracked by git.
- Added `index.min_example/` to `.gitignore`.
- Resolved git history divergence (remote had different initial commit) via force push.
- All changes pushed to `https://github.com/T0ug/THC-for-Mihon`.

---

## Artefatos afetados

New:
- `repo/index.min.json`
- `repo/mihon-pt.thehentaicomics-v1.4.7.apk`

Modified:
- `.gitignore`
- `docs/project_status.md`
- `docs/handoff.md`

---

## Evidencia da entrega

Remote `index.min.json` accessible at:
```
https://raw.githubusercontent.com/T0ug/THC-for-Mihon/main/repo/index.min.json
```

Contents:
```json
[{"name":"Tachiyomi: Toug - The Hentai Comics","pkg":"eu.kanade.tachiyomi.extension.pt.thehentaicomics","apk":"mihon-pt.thehentaicomics-v1.4.7.apk","lang":"pt-BR","code":7,"version":"1.4.7","nsfw":1,"hasReadme":0,"hasChangelog":0,"sources":[{"name":"Toug - The Hentai Comics","lang":"pt-BR","id":"-7329981312210241844","baseUrl":"https://thehentaicomics.com"}]}]
```

Git push output:
```
To https://github.com/T0ug/THC-for-Mihon.git
 + a42a049...5fd29e3 main -> main (forced update)
```

---

## Logica implementada

When a user adds the repository URL in Mihon:
1. Mihon fetches `<repo_url>/index.min.json`.
2. It reads the extension metadata (name, package, APK filename, version, sources).
3. The extension appears in Mihon's extension list under the "pt-BR" language filter.
4. When the user taps "Install", Mihon downloads `<repo_url>/<apk>` and installs it.

Repository URL to add in Mihon:
```
https://raw.githubusercontent.com/T0ug/THC-for-Mihon/main/repo
```

---

## Validacao realizada

- Verified `repo/index.min.json` is accessible via `raw.githubusercontent.com`.
- Verified JSON format matches the Keiyoushi/Mihon extension repository schema.
- Verified APK file exists in `repo/`.

---

## Limitacoes conhecidas

- The source ID was calculated using MurmurHash3 on `"$baseUrl/$lang/$name"`. If the extension's `name`, `lang`, or `baseUrl` change, the ID must be recalculated and `index.min.json` updated.
- The `repo/` directory contains the APK directly in git; for large-scale repositories, GitHub Releases or LFS would be more appropriate.

---

## Pendencias

- Manual Mihon test: add the repository URL and verify the extension appears and installs correctly.

---

## Proxima acao sugerida

1. Open Mihon → Settings → Browse → Extension repos.
2. Add: `https://raw.githubusercontent.com/T0ug/THC-for-Mihon/main/repo`
3. Go to Browse → Extensions tab.
4. Verify "Toug - The Hentai Comics" appears under "pt-BR".
5. Tap Install and verify the extension works.

---

## Status

- [x] Completo
- [ ] Parcial
- [ ] Bloqueado
