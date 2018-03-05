console.log("hello world");
let fs = require('fs');

module.exports = function (context) {

    console.log("hello on adding platform body tag");

    let src = '\'platforms/android/build.gradle\'';

    let content = fs.readFileSync('platforms/android/build.gradle', 'utf8');
    let extra = 'buildscript {\n' +
        '    repositories {\n' +
        '        jcenter()\n' +
        '    }\n' +
        '    dependencies {\n' +
        '        classpath "io.realm:realm-gradle-plugin:3.5.0"\n' +
        '        // NOTE: Do not place your application dependencies here; they belong\n' +
        '        // in the individual module build.gradle files\n' +
        '    }\n' +
        '}';

    content = extra + '\n' + content;

    console.log(content);
    fs.writeFileSync('platforms/android/build.gradle', content);
};