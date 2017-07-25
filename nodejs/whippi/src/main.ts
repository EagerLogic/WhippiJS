import * as runtime from "./runtime";
import * as defaultSuite from "./default-suite";
import * as http from "http";
import * as fs from "fs";


export * from "./runtime";

runtime.registerSuit(defaultSuite.suite);

export class Server {

    private _staticFilesBaseFolder: string = "/";
    private _wpiBaseFolder: string = "/";
    private _isListening = false;
    private server: http.Server;
    private mappings: RequestHandler[] = [];

    get isListening() {
        return this._isListening;
    }

    get staticFilesBaseFolder() {
        return this._staticFilesBaseFolder;
    }

    set staticFilesBaseFolder(newValue: string) {
        this._staticFilesBaseFolder = newValue;
    }

    get wpiBaseFolder() {
        return this._wpiBaseFolder;
    }

    set wpiBaseFolder(newValue: string) {
        this._wpiBaseFolder = newValue;
    }


    public listen(port: number): void {
        if (this.isListening) {
            throw "This server is already listening!";
        }

        this.server = http.createServer((req, resp) => {
            if (req.url.lastIndexOf(".wpi") == req.url.length - 4) {
                this.handleWpiRequest(req, resp);
                return;
            }
            // TODO handle other type of requests
        });
        this.server.listen(port);
        this._isListening = true;
    }

    public close() {
        if (!this.isListening) {
            throw "The server isn't started!";
        }

        this.server.close();
        this.server = null;
        this._isListening = false;
    }

    public GET(url: string, func: IRequestHandlerFunction) {
        this.registerHandler("get", url, func);
    }

    public POST(url: string, func: IRequestHandlerFunction) {
        this.registerHandler("post", url, func);
    }

    public PUT(url: string, func: IRequestHandlerFunction) {
        this.registerHandler("put", url, func);
    }

    public PATCH(url: string, func: IRequestHandlerFunction) {
        this.registerHandler("patch", url, func);
    }

    private registerHandler(method: string, url: string, func: IRequestHandlerFunction) {
        this.mappings.push(new RequestHandler(method, url, func));
    }

    private handleWpiRequest(req: http.ServerRequest, resp: http.ServerResponse) {

    }

}

export interface IRequestHandlerFunction {
    (req: http.IncomingMessage, resp: http.ServerResponse): void;
}

class RequestHandler {

    constructor(public method: string, public url: string, public func: IRequestHandlerFunction) { }

}
