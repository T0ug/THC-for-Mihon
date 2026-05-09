# Toug - The Hentai Comics (Mihon Extension)

Uma extensão single-source desenvolvida para o aplicativo **Mihon** (antigo Tachiyomi), focada no catálogo de quadrinhos adultos brasileiros do site `thehentaicomics.com`.

## 📌 Visão Geral

O objetivo deste projeto foi construir uma extensão leve e com alta performance, desviando de gargalos comuns na raspagem tradicional de sites. Para isso, a arquitetura foi desenhada em torno do consumo de **Sitemaps XML** (`sitemap_index.xml` e `post_tag-sitemap.xml`) no lugar do scraping da interface web primária.

Essa estratégia garante:
1. Menos bloqueios e consumo de rede desnecessário.
2. Descoberta inteligente de obras novas baseada na data de modificação (`lastmod`).
3. Uma arquitetura modular (Sitemap Parsers, URL Classifiers) de fácil manutenção.

---

## ⚡ Funcionalidades

- **Aba "Mais Recentes" (Latest):** 
  Alimentada pelo `sitemap_index.xml`, ela vasculha os sitemaps de posts, filtra URLs inválidas e ordena as obras sempre pelas que sofreram modificações mais recentes, organizando em lotes (paginação) de 5 itens para garantir um carregamento inicial em menos de 2 segundos.
- **Aba "Popular":** 
  Como o site não possui um endpoint claro de métrica de "popularidade", a extensão utiliza de forma inteligente o `post_tag-sitemap.xml`. Cada rolagem do usuário puxa a página HTML de uma tag recém-atualizada, oferecendo um catálogo dinâmico de descoberta.
- **Extração de Imagens Inteligente:**
  A obtenção das imagens dos capítulos foca nas imagens diretamente associadas ao quadrinho (baseadas em semelhança de slug, classes e estrutura). Se falhar, utiliza métodos fallback perdoáveis, garantindo que a leitura seja carregada.
- **Limpeza de ADS Embutido:** 
  A extensão possui um filtro para eliminar atalhos de ADS (propagandas da Tufos, Império Hentai, etc.) injetados no meio do catálogo de tags ou sugeridos como obras reais, limitando o conteúdo a domínios nativos.

---

## 🏗️ Arquitetura e Estrutura de Diretórios

O projeto usa o ambiente padrão de desenvolvimento de extensões Tachiyomi/Keiyoushi (com o framework Gradle `core`), focado em apenas uma fonte (`src/pt/thehentaicomics`).

Os principais componentes criados são:
- **`TougTheHentaiComics.kt`**: A classe fonte principal que a interface do Mihon consome.
- **`LatestResolver.kt` e `PopularResolver.kt`**: Orquestradores que pegam os sitemaps e gerenciam as chamadas HTTP bloqueantes para montar as páginas de listagem.
- **`ContentPageParser.kt`**: Trabalha nos detalhes de uma obra e lista seu (único) capítulo.
- **`ImageExtractor.kt`**: Garante as imagens exclusivas do conteúdo de leitura.
- **`SitemapIndexParser.kt` e `SitemapUrlParser.kt`**: Lidadores de extração via Regex e Jsoup dos arquivos `.xml`.
- **`UrlClassifier.kt`**: Motor de validação de URLs para evitar links quebrados ou sem sentido no sitemap (como anexos ou categorias puras).

*(A documentação completa de arquitetura encontra-se na pasta `docs/architecture.md`).*

---

## ⛔ Limitações Intencionais (Non-Goals / Escopo do MVP)

Para garantir o funcionamento, algumas funcionalidades comuns foram **intencionalmente omitidas do MVP (Produto Mínimo Viável)** devido às limitações e estrutura do site origem:

- **Busca por Título:** Não implementada.
- **Filtro de Gêneros/Tags:** Não implementado no feed.
- **Banco de Dados/Cache Local:** Não armazenamos URLs persistentes na memória do app para não inflar espaço. Tudo é sob-demanda do site.
- **Agrupamento de Séries:** Cada link de obra do sitemap é tratado como um Quadrinho (Manga) único contendo apenas 1 capítulo ("Capítulo Único").

---

## 🛠️ Como Compilar e Buildar (Dev)

Certifique-se de ter o JDK (Java 17+) configurado. Se o Android SDK local estiver configurado na pasta `android` do projeto (como especificado em `local.properties`), você pode compilar utilizando o Gradle Wrapper:

```powershell
.\gradlew.bat --no-daemon :src:pt:thehentaicomics:assembleDebug
```

O arquivo final `.apk` gerado pelo processo de build estará em:
`src/pt/thehentaicomics/build/outputs/apk/debug/mihon-pt.thehentaicomics-vX.X.X-debug.apk`

---

## 📚 Documentação (Pasta Docs)

Para o histórico detalhado, decisões técnicas e estado de todas as tasks, consulte a pasta `/docs`:
- `tasks.md`: Lista e escopo detalhado de cada feature.
- `decision_log.md`: O "porquê" de cada escolha técnica adotada na extensão (ex: por que não tem pesquisa, escolha do ícone, paginação de 5 por vez).
- `project_status.md` e `handoff.md`: Artefatos para coordenação do ciclo de desenvolvimento em multi-agentes.

---
*Criado com as melhores práticas de Arquitetura Agentic e Validado em Produção Móvel.*
