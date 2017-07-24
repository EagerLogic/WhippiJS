/// <reference types="node" />
import * as http from "http";
export * from "./parser";
export declare class Server {
    private _staticFilesBaseFolder;
    private _wpiBaseFolder;
    private _isListening;
    private server;
    private mappings;
    readonly isListening: boolean;
    staticFilesBaseFolder: string;
    wpiBaseFolder: string;
    listen(port: number): void;
    close(): void;
    GET(url: string, func: IRequestHandlerFunction): void;
    POST(url: string, func: IRequestHandlerFunction): void;
    PUT(url: string, func: IRequestHandlerFunction): void;
    PATCH(url: string, func: IRequestHandlerFunction): void;
    private registerHandler(method, url, func);
    private handleWpiRequest(req, resp);
}
export interface IRequestHandlerFunction {
    (req: http.IncomingMessage, resp: http.ServerResponse): void;
}
