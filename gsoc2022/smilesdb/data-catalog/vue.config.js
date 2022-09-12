const NodePolyfillPlugin = require("node-polyfill-webpack-plugin");

module.exports = {
    configureWebpack: config => {
        config.entry.app = [
            './src/main.js',
        ]
        config.resolve.fallback = {
            ...config.resolve.fallback,
            "fs": false
        }

        config.plugins = [...config.plugins, new NodePolyfillPlugin()];
    },
    transpileDependencies: ["airavata-custos-portal"],
    pluginOptions: {
        i18n: {
            locale: 'en',
            fallbackLocale: 'en',
            localeDir: 'locales',
            enableInSFC: true
        }
    }
}