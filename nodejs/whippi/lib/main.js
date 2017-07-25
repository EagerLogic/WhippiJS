"use strict";
function __export(m) {
    for (var p in m) if (!exports.hasOwnProperty(p)) exports[p] = m[p];
}
Object.defineProperty(exports, "__esModule", { value: true });
var runtime = require("./runtime");
var defaultSuite = require("./default-suite");
var http = require("http");
__export(require("./runtime"));
runtime.registerSuit(defaultSuite.suite);
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
//# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoibWFpbi5qcyIsInNvdXJjZVJvb3QiOiIiLCJzb3VyY2VzIjpbIi4uL3NyYy9tYWluLnRzIl0sIm5hbWVzIjpbXSwibWFwcGluZ3MiOiI7Ozs7O0FBQUEsbUNBQXFDO0FBQ3JDLDhDQUFnRDtBQUNoRCwyQkFBNkI7QUFJN0IsK0JBQTBCO0FBRTFCLE9BQU8sQ0FBQyxZQUFZLENBQUMsWUFBWSxDQUFDLEtBQUssQ0FBQyxDQUFDO0FBRXpDO0lBQUE7UUFFWSwyQkFBc0IsR0FBVyxHQUFHLENBQUM7UUFDckMsbUJBQWMsR0FBVyxHQUFHLENBQUM7UUFDN0IsaUJBQVksR0FBRyxLQUFLLENBQUM7UUFFckIsYUFBUSxHQUFxQixFQUFFLENBQUM7SUF5RTVDLENBQUM7SUF2RUcsc0JBQUksK0JBQVc7YUFBZjtZQUNJLE1BQU0sQ0FBQyxJQUFJLENBQUMsWUFBWSxDQUFDO1FBQzdCLENBQUM7OztPQUFBO0lBRUQsc0JBQUkseUNBQXFCO2FBQXpCO1lBQ0ksTUFBTSxDQUFDLElBQUksQ0FBQyxzQkFBc0IsQ0FBQztRQUN2QyxDQUFDO2FBRUQsVUFBMEIsUUFBZ0I7WUFDdEMsSUFBSSxDQUFDLHNCQUFzQixHQUFHLFFBQVEsQ0FBQztRQUMzQyxDQUFDOzs7T0FKQTtJQU1ELHNCQUFJLGlDQUFhO2FBQWpCO1lBQ0ksTUFBTSxDQUFDLElBQUksQ0FBQyxjQUFjLENBQUM7UUFDL0IsQ0FBQzthQUVELFVBQWtCLFFBQWdCO1lBQzlCLElBQUksQ0FBQyxjQUFjLEdBQUcsUUFBUSxDQUFDO1FBQ25DLENBQUM7OztPQUpBO0lBT00sdUJBQU0sR0FBYixVQUFjLElBQVk7UUFBMUIsaUJBY0M7UUFiRyxFQUFFLENBQUMsQ0FBQyxJQUFJLENBQUMsV0FBVyxDQUFDLENBQUMsQ0FBQztZQUNuQixNQUFNLG1DQUFtQyxDQUFDO1FBQzlDLENBQUM7UUFFRCxJQUFJLENBQUMsTUFBTSxHQUFHLElBQUksQ0FBQyxZQUFZLENBQUMsVUFBQyxHQUFHLEVBQUUsSUFBSTtZQUN0QyxFQUFFLENBQUMsQ0FBQyxHQUFHLENBQUMsR0FBRyxDQUFDLFdBQVcsQ0FBQyxNQUFNLENBQUMsSUFBSSxHQUFHLENBQUMsR0FBRyxDQUFDLE1BQU0sR0FBRyxDQUFDLENBQUMsQ0FBQyxDQUFDO2dCQUNwRCxLQUFJLENBQUMsZ0JBQWdCLENBQUMsR0FBRyxFQUFFLElBQUksQ0FBQyxDQUFDO2dCQUNqQyxNQUFNLENBQUM7WUFDWCxDQUFDO1lBQ0QscUNBQXFDO1FBQ3pDLENBQUMsQ0FBQyxDQUFDO1FBQ0gsSUFBSSxDQUFDLE1BQU0sQ0FBQyxNQUFNLENBQUMsSUFBSSxDQUFDLENBQUM7UUFDekIsSUFBSSxDQUFDLFlBQVksR0FBRyxJQUFJLENBQUM7SUFDN0IsQ0FBQztJQUVNLHNCQUFLLEdBQVo7UUFDSSxFQUFFLENBQUMsQ0FBQyxDQUFDLElBQUksQ0FBQyxXQUFXLENBQUMsQ0FBQyxDQUFDO1lBQ3BCLE1BQU0sMkJBQTJCLENBQUM7UUFDdEMsQ0FBQztRQUVELElBQUksQ0FBQyxNQUFNLENBQUMsS0FBSyxFQUFFLENBQUM7UUFDcEIsSUFBSSxDQUFDLE1BQU0sR0FBRyxJQUFJLENBQUM7UUFDbkIsSUFBSSxDQUFDLFlBQVksR0FBRyxLQUFLLENBQUM7SUFDOUIsQ0FBQztJQUVNLG9CQUFHLEdBQVYsVUFBVyxHQUFXLEVBQUUsSUFBNkI7UUFDakQsSUFBSSxDQUFDLGVBQWUsQ0FBQyxLQUFLLEVBQUUsR0FBRyxFQUFFLElBQUksQ0FBQyxDQUFDO0lBQzNDLENBQUM7SUFFTSxxQkFBSSxHQUFYLFVBQVksR0FBVyxFQUFFLElBQTZCO1FBQ2xELElBQUksQ0FBQyxlQUFlLENBQUMsTUFBTSxFQUFFLEdBQUcsRUFBRSxJQUFJLENBQUMsQ0FBQztJQUM1QyxDQUFDO0lBRU0sb0JBQUcsR0FBVixVQUFXLEdBQVcsRUFBRSxJQUE2QjtRQUNqRCxJQUFJLENBQUMsZUFBZSxDQUFDLEtBQUssRUFBRSxHQUFHLEVBQUUsSUFBSSxDQUFDLENBQUM7SUFDM0MsQ0FBQztJQUVNLHNCQUFLLEdBQVosVUFBYSxHQUFXLEVBQUUsSUFBNkI7UUFDbkQsSUFBSSxDQUFDLGVBQWUsQ0FBQyxPQUFPLEVBQUUsR0FBRyxFQUFFLElBQUksQ0FBQyxDQUFDO0lBQzdDLENBQUM7SUFFTyxnQ0FBZSxHQUF2QixVQUF3QixNQUFjLEVBQUUsR0FBVyxFQUFFLElBQTZCO1FBQzlFLElBQUksQ0FBQyxRQUFRLENBQUMsSUFBSSxDQUFDLElBQUksY0FBYyxDQUFDLE1BQU0sRUFBRSxHQUFHLEVBQUUsSUFBSSxDQUFDLENBQUMsQ0FBQztJQUM5RCxDQUFDO0lBRU8saUNBQWdCLEdBQXhCLFVBQXlCLEdBQXVCLEVBQUUsSUFBeUI7SUFFM0UsQ0FBQztJQUVMLGFBQUM7QUFBRCxDQUFDLEFBL0VELElBK0VDO0FBL0VZLHdCQUFNO0FBcUZuQjtJQUVJLHdCQUFtQixNQUFjLEVBQVMsR0FBVyxFQUFTLElBQTZCO1FBQXhFLFdBQU0sR0FBTixNQUFNLENBQVE7UUFBUyxRQUFHLEdBQUgsR0FBRyxDQUFRO1FBQVMsU0FBSSxHQUFKLElBQUksQ0FBeUI7SUFBSSxDQUFDO0lBRXBHLHFCQUFDO0FBQUQsQ0FBQyxBQUpELElBSUMiLCJzb3VyY2VzQ29udGVudCI6WyJpbXBvcnQgKiBhcyBydW50aW1lIGZyb20gXCIuL3J1bnRpbWVcIjtcclxuaW1wb3J0ICogYXMgZGVmYXVsdFN1aXRlIGZyb20gXCIuL2RlZmF1bHQtc3VpdGVcIjtcclxuaW1wb3J0ICogYXMgaHR0cCBmcm9tIFwiaHR0cFwiO1xyXG5pbXBvcnQgKiBhcyBmcyBmcm9tIFwiZnNcIjtcclxuXHJcblxyXG5leHBvcnQgKiBmcm9tIFwiLi9ydW50aW1lXCI7XHJcblxyXG5ydW50aW1lLnJlZ2lzdGVyU3VpdChkZWZhdWx0U3VpdGUuc3VpdGUpO1xyXG5cclxuZXhwb3J0IGNsYXNzIFNlcnZlciB7XHJcblxyXG4gICAgcHJpdmF0ZSBfc3RhdGljRmlsZXNCYXNlRm9sZGVyOiBzdHJpbmcgPSBcIi9cIjtcclxuICAgIHByaXZhdGUgX3dwaUJhc2VGb2xkZXI6IHN0cmluZyA9IFwiL1wiO1xyXG4gICAgcHJpdmF0ZSBfaXNMaXN0ZW5pbmcgPSBmYWxzZTtcclxuICAgIHByaXZhdGUgc2VydmVyOiBodHRwLlNlcnZlcjtcclxuICAgIHByaXZhdGUgbWFwcGluZ3M6IFJlcXVlc3RIYW5kbGVyW10gPSBbXTtcclxuXHJcbiAgICBnZXQgaXNMaXN0ZW5pbmcoKSB7XHJcbiAgICAgICAgcmV0dXJuIHRoaXMuX2lzTGlzdGVuaW5nO1xyXG4gICAgfVxyXG5cclxuICAgIGdldCBzdGF0aWNGaWxlc0Jhc2VGb2xkZXIoKSB7XHJcbiAgICAgICAgcmV0dXJuIHRoaXMuX3N0YXRpY0ZpbGVzQmFzZUZvbGRlcjtcclxuICAgIH1cclxuXHJcbiAgICBzZXQgc3RhdGljRmlsZXNCYXNlRm9sZGVyKG5ld1ZhbHVlOiBzdHJpbmcpIHtcclxuICAgICAgICB0aGlzLl9zdGF0aWNGaWxlc0Jhc2VGb2xkZXIgPSBuZXdWYWx1ZTtcclxuICAgIH1cclxuXHJcbiAgICBnZXQgd3BpQmFzZUZvbGRlcigpIHtcclxuICAgICAgICByZXR1cm4gdGhpcy5fd3BpQmFzZUZvbGRlcjtcclxuICAgIH1cclxuXHJcbiAgICBzZXQgd3BpQmFzZUZvbGRlcihuZXdWYWx1ZTogc3RyaW5nKSB7XHJcbiAgICAgICAgdGhpcy5fd3BpQmFzZUZvbGRlciA9IG5ld1ZhbHVlO1xyXG4gICAgfVxyXG5cclxuXHJcbiAgICBwdWJsaWMgbGlzdGVuKHBvcnQ6IG51bWJlcik6IHZvaWQge1xyXG4gICAgICAgIGlmICh0aGlzLmlzTGlzdGVuaW5nKSB7XHJcbiAgICAgICAgICAgIHRocm93IFwiVGhpcyBzZXJ2ZXIgaXMgYWxyZWFkeSBsaXN0ZW5pbmchXCI7XHJcbiAgICAgICAgfVxyXG5cclxuICAgICAgICB0aGlzLnNlcnZlciA9IGh0dHAuY3JlYXRlU2VydmVyKChyZXEsIHJlc3ApID0+IHtcclxuICAgICAgICAgICAgaWYgKHJlcS51cmwubGFzdEluZGV4T2YoXCIud3BpXCIpID09IHJlcS51cmwubGVuZ3RoIC0gNCkge1xyXG4gICAgICAgICAgICAgICAgdGhpcy5oYW5kbGVXcGlSZXF1ZXN0KHJlcSwgcmVzcCk7XHJcbiAgICAgICAgICAgICAgICByZXR1cm47XHJcbiAgICAgICAgICAgIH1cclxuICAgICAgICAgICAgLy8gVE9ETyBoYW5kbGUgb3RoZXIgdHlwZSBvZiByZXF1ZXN0c1xyXG4gICAgICAgIH0pO1xyXG4gICAgICAgIHRoaXMuc2VydmVyLmxpc3Rlbihwb3J0KTtcclxuICAgICAgICB0aGlzLl9pc0xpc3RlbmluZyA9IHRydWU7XHJcbiAgICB9XHJcblxyXG4gICAgcHVibGljIGNsb3NlKCkge1xyXG4gICAgICAgIGlmICghdGhpcy5pc0xpc3RlbmluZykge1xyXG4gICAgICAgICAgICB0aHJvdyBcIlRoZSBzZXJ2ZXIgaXNuJ3Qgc3RhcnRlZCFcIjtcclxuICAgICAgICB9XHJcblxyXG4gICAgICAgIHRoaXMuc2VydmVyLmNsb3NlKCk7XHJcbiAgICAgICAgdGhpcy5zZXJ2ZXIgPSBudWxsO1xyXG4gICAgICAgIHRoaXMuX2lzTGlzdGVuaW5nID0gZmFsc2U7XHJcbiAgICB9XHJcblxyXG4gICAgcHVibGljIEdFVCh1cmw6IHN0cmluZywgZnVuYzogSVJlcXVlc3RIYW5kbGVyRnVuY3Rpb24pIHtcclxuICAgICAgICB0aGlzLnJlZ2lzdGVySGFuZGxlcihcImdldFwiLCB1cmwsIGZ1bmMpO1xyXG4gICAgfVxyXG5cclxuICAgIHB1YmxpYyBQT1NUKHVybDogc3RyaW5nLCBmdW5jOiBJUmVxdWVzdEhhbmRsZXJGdW5jdGlvbikge1xyXG4gICAgICAgIHRoaXMucmVnaXN0ZXJIYW5kbGVyKFwicG9zdFwiLCB1cmwsIGZ1bmMpO1xyXG4gICAgfVxyXG5cclxuICAgIHB1YmxpYyBQVVQodXJsOiBzdHJpbmcsIGZ1bmM6IElSZXF1ZXN0SGFuZGxlckZ1bmN0aW9uKSB7XHJcbiAgICAgICAgdGhpcy5yZWdpc3RlckhhbmRsZXIoXCJwdXRcIiwgdXJsLCBmdW5jKTtcclxuICAgIH1cclxuXHJcbiAgICBwdWJsaWMgUEFUQ0godXJsOiBzdHJpbmcsIGZ1bmM6IElSZXF1ZXN0SGFuZGxlckZ1bmN0aW9uKSB7XHJcbiAgICAgICAgdGhpcy5yZWdpc3RlckhhbmRsZXIoXCJwYXRjaFwiLCB1cmwsIGZ1bmMpO1xyXG4gICAgfVxyXG5cclxuICAgIHByaXZhdGUgcmVnaXN0ZXJIYW5kbGVyKG1ldGhvZDogc3RyaW5nLCB1cmw6IHN0cmluZywgZnVuYzogSVJlcXVlc3RIYW5kbGVyRnVuY3Rpb24pIHtcclxuICAgICAgICB0aGlzLm1hcHBpbmdzLnB1c2gobmV3IFJlcXVlc3RIYW5kbGVyKG1ldGhvZCwgdXJsLCBmdW5jKSk7XHJcbiAgICB9XHJcblxyXG4gICAgcHJpdmF0ZSBoYW5kbGVXcGlSZXF1ZXN0KHJlcTogaHR0cC5TZXJ2ZXJSZXF1ZXN0LCByZXNwOiBodHRwLlNlcnZlclJlc3BvbnNlKSB7XHJcblxyXG4gICAgfVxyXG5cclxufVxyXG5cclxuZXhwb3J0IGludGVyZmFjZSBJUmVxdWVzdEhhbmRsZXJGdW5jdGlvbiB7XHJcbiAgICAocmVxOiBodHRwLkluY29taW5nTWVzc2FnZSwgcmVzcDogaHR0cC5TZXJ2ZXJSZXNwb25zZSk6IHZvaWQ7XHJcbn1cclxuXHJcbmNsYXNzIFJlcXVlc3RIYW5kbGVyIHtcclxuXHJcbiAgICBjb25zdHJ1Y3RvcihwdWJsaWMgbWV0aG9kOiBzdHJpbmcsIHB1YmxpYyB1cmw6IHN0cmluZywgcHVibGljIGZ1bmM6IElSZXF1ZXN0SGFuZGxlckZ1bmN0aW9uKSB7IH1cclxuXHJcbn1cclxuIl19