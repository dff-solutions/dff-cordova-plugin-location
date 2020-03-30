const fs = require("fs");
const path = require("path").posix;

const xmlReader = require("./xml-reader");
const dirReader = require("./dir-reader");

const projectDir = path.join(__dirname, "..");
const pluginXmlPath = path.join(projectDir, "plugin.xml");
const srcMainJavaDir = path.join("src", "main", "java");

const androidSrcFiles = [
        "location-plugin"
    ]
    .map((m) => ({
        m,
        files: dirReader.readDir(path.join(projectDir, m, srcMainJavaDir))
    }))
    .map(({m, files}) => (
        files
            .sort()
            .map((file) => path.relative(projectDir, file))
            .map((file) => ({
                $: {
                    src: file,
                    /**
                     * replace `${m}/src/main/java` with `src`
                     */
                    "target-dir": path.join(
                        "src",
                        path.relative(
                            path.join(m, srcMainJavaDir),
                            file
                        )
                    )
                }
            }))
    ))
    .reduce((result, files) => [ ...result, ...files ], []);

const pluginXmlData = xmlReader.read(pluginXmlPath);

const xmlPlatformAndroid = pluginXmlData.plugin.platform
    .find((p) => p.$.name == "android");
const oldSourceFiles = xmlPlatformAndroid["source-file"] || [];

// keep system libs
const sourceLibs = oldSourceFiles
    .filter((oldFile) => oldFile.$.src.endsWith(".jar"));

xmlPlatformAndroid["source-file"] = [
    ...androidSrcFiles,
    ...sourceLibs
];

xmlReader.write(pluginXmlPath, pluginXmlData);
