import * as whippi from "whippi";

var svr = new whippi.Server();
svr.staticFilesBaseFolder = "/public";
svr.listen(8080);

