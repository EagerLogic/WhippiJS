"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
var whippi = require("whippi");
var svr = new whippi.Server();
svr.staticFilesBaseFolder = "/public";
svr.listen(8080);
//# sourceMappingURL=main.js.map