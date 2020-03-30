const exec = require('cordova/exec');

function CordovaExec(feature, actions) {
    this.feature = feature;

    this.createActions(actions);
}

CordovaExec.prototype.createActionFunction = function (action) {
    var self = this;

    return function (success, error, args) {
        exec(success, error, self.feature, action, [ args ]);
    }
};

CordovaExec.prototype.createActions = function (actions) {
    var self = this;

    if (Array.isArray(actions)) {
        actions
            .forEach(function (action) {
                if (typeof action === "string") {
                    self[action] = self.createActionFunction(action);
                } else if (typeof action === "object") {
                    self[action.key] = self.createActionFunction(action.value);
                } else {
                    throw new Error(`unknown action type ${action}`);
                }
            });
    }
};

module.exports = function (feature, actions) {
    return new CordovaExec(feature, actions);
};
