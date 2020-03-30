#!/usr/bin/env node

module.exports = function(ctx) {
    const fs = require('fs');
    const path = require('path');
    const os = require("os");
    const readline = require("readline");
    const deferral = require('q').defer();
    const androidGradleConfig = require("./configAndroidGradle")(ctx);
    const buildGradleTemp = androidGradleConfig.buildGradleTemp;

    let inPluginSection = false;
    const startSectionRegExp = new RegExp(`.*${androidGradleConfig.comment.start}.*`);
    const endSectionRegExp = new RegExp(`.*${androidGradleConfig.comment.end}.*`);

    console.log("unpatch build.gradle");

    const lineReader = readline.createInterface({
        terminal: false,
        input : fs.createReadStream(androidGradleConfig.buildGradlePath)
    });

    lineReader
        .on("line", function(line) {
            if(startSectionRegExp.test(line)) {
                inPluginSection = true;
            }

            if (!inPluginSection) {
                fs.appendFileSync(buildGradleTemp, line.toString() + os.EOL);
            }

            // end line to be removed too
            if (endSectionRegExp.test(line)) {
                inPluginSection = false;
            }
        })
        .on("close", function () {
            fs.rename(buildGradleTemp, androidGradleConfig.buildGradlePath, deferral.resolve);
        });

    return deferral.promise;
}
