"use strict";
function __export(m) {
    for (var p in m) if (!exports.hasOwnProperty(p)) exports[p] = m[p];
}
Object.defineProperty(exports, "__esModule", { value: true });
var http = require("http");
__export(require("./parser"));
var Server = (function () {
    function Server() {
        this._staticFilesBaseFolder = "/";
        this._wpiBaseFolder = "/";
        this._isListening = false;
        this.mappings = [];
    }
    Object.defineProperty(Server.prototype, "isListening", {
        get: function () {
            return this._isListening;
        },
        enumerable: true,
        configurable: true
    });
    Object.defineProperty(Server.prototype, "staticFilesBaseFolder", {
        get: function () {
            return this._staticFilesBaseFolder;
        },
        set: function (newValue) {
            this._staticFilesBaseFolder = newValue;
        },
        enumerable: true,
        configurable: true
    });
    Object.defineProperty(Server.prototype, "wpiBaseFolder", {
        get: function () {
            return this._wpiBaseFolder;
        },
        set: function (newValue) {
            this._wpiBaseFolder = newValue;
        },
        enumerable: true,
        configurable: true
    });
    Server.prototype.listen = function (port) {
        var _this = this;
        if (this.isListening) {
            throw "This server is already listening!";
        }
        this.server = http.createServer(function (req, resp) {
            if (req.url.lastIndexOf(".wpi") == req.url.length - 4) {
                _this.handleWpiRequest(req, resp);
                return;
            }
            // TODO handle other type of requests
        });
        this.server.listen(port);
        this._isListening = true;
    };
    Server.prototype.close = function () {
        if (!this.isListening) {
            throw "The server isn't started!";
        }
        this.server.close();
        this.server = null;
        this._isListening = false;
    };
    Server.prototype.GET = function (url, func) {
        this.registerHandler("get", url, func);
    };
    Server.prototype.POST = function (url, func) {
        this.registerHandler("post", url, func);
    };
    Server.prototype.PUT = function (url, func) {
        this.registerHandler("put", url, func);
    };
    Server.prototype.PATCH = function (url, func) {
        this.registerHandler("patch", url, func);
    };
    Server.prototype.registerHandler = function (method, url, func) {
        this.mappings.push(new RequestHandler(method, url, func));
    };
    Server.prototype.handleWpiRequest = function (req, resp) {
    };
    return Server;
}());
exports.Server = Server;
var RequestHandler = (function () {
    function RequestHandler(method, url, func) {
        this.method = method;
        this.url = url;
        this.func = func;
    }
    return RequestHandler;
}());
//# sourceMappingURL=index.js.map