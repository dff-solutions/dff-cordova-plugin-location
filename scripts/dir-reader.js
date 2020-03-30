const path = require("path").posix;
const fs = require("fs");

function DirReader() {}

DirReader.prototype.readDir = function (dir) {
    if (!fs.existsSync(dir)) {
        console.warn(`directory not found ${dir}`);
        return [];
    }

    return fs
        .readdirSync(dir)
        .reduce((result, file) => {
            const filePath = path.join(dir, file)
            const stat = fs.lstatSync(filePath);

            if (stat.isDirectory()) {
                return [
                    ...result,
                    ...this.readDir(filePath)
                ];
            } else if (stat.isFile()) {
                return [
                    ...result,
                    filePath
                ];
            } else {
                return result;
            }
        }, []);
};

DirReader.prototype.readDirs = function (dirs) {
    let result = [];

    dirs
        .forEach((dir) => {
            let dirResult = this.readDir(dir);
            result = [
                ...result,
                ...dirResult
            ];
        });

    return result;
};

module.exports = new DirReader();
