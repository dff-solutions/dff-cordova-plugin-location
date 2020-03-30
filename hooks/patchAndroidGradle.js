#!/usr/bin/env node

module.exports = function(ctx) {
    const fs = require('fs');
    const path = require('path');
    const os = require("os");
    const readline = require("readline");
    const deferral = require('q').defer();
    const androidGradleConfig = require("./configAndroidGradle")(ctx);
    const buildGradleTemp = androidGradleConfig.buildGradleTemp;

    console.log("patch build.gradle");

    const lineReader = readline.createInterface({
        terminal: false,
        input : fs.createReadStream(androidGradleConfig.buildGradlePath)
    });

    lineReader
        .on("line", function(line) {
            fs.appendFileSync(buildGradleTemp, line.toString() + os.EOL);

            if (/.*\ dependencies \{.*/.test(line)) {
                fs.appendFileSync(buildGradleTemp, androidGradleConfig.comment.start + os.EOL);
                fs.appendFileSync(buildGradleTemp, 'classpath "io.realm:realm-gradle-plugin:5.12.0"'
                 + os.EOL);
                fs.appendFileSync(buildGradleTemp, androidGradleConfig.comment.end + os.EOL);
            }
        })
        .on("close", function () {
            fs.rename(buildGradleTemp, androidGradleConfig.buildGradlePath, deferral.resolve);
        });

    return deferral.promise;
};
