// Força o Swagger UI a usar português do Brasil por padrão.
window.onload = function () {
  const configUrl = '/v3/api-docs/swagger-config';
  const defaultDocUrl = '/v3/api-docs';

  const createUi = (cfg = {}) =>
    SwaggerUIBundle({
      ...cfg,
      dom_id: '#swagger-ui',
      presets: [SwaggerUIBundle.presets.apis, SwaggerUIStandalonePreset],
      layout: 'BaseLayout',
      lang: 'pt-BR',
    });

  fetch(configUrl)
    .then((r) => (r.ok ? r.json() : Promise.reject()))
    .then((cfg) => {
      // springdoc já envia as chaves corretas; apenas reforçamos a língua.
      window.ui = createUi(cfg);
    })
    .catch(() => {
      // Fallback mínimo se o swagger-config não estiver acessível.
      window.ui = createUi({ url: defaultDocUrl });
    });
};
