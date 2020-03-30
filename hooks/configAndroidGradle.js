#!/usr/bin/env node

const path = require('path');

module.exports = function(ctx) {
    const buildGradlePath = path.join("platforms", "android", "build.gradle");
    const buildGradleTemp = path.join(".", "build.gradle");
    const pluginId = ctx.opts.plugin.id;

    return {
        buildGradlePath,
        buildGradleTemp,
        comment: {
            start: `// START ${pluginId}`,
            end:   `// END ${pluginId}`
        }
    };
};