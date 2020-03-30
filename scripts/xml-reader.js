const fs = require("fs");
const xml2js = require("xml2js");

module.exports = {
    read: function (xmlPath) {
        const parser = new xml2js.Parser();
        const data = fs.readFileSync(xmlPath);
        let xmlJson;

        parser.parseString(data, (err, result) => {
            if (err) {
                throw err;
            }

            xmlJson = result;
        });

        return xmlJson;
    },

    write: function (path, data) {
        const builder = new xml2js.Builder();
        const xml = builder.buildObject(data);
        fs.writeFileSync(path, xml);
    }
};
