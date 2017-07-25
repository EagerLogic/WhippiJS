"use strict";
var __extends = (this && this.__extends) || (function () {
    var extendStatics = Object.setPrototypeOf ||
        ({ __proto__: [] } instanceof Array && function (d, b) { d.__proto__ = b; }) ||
        function (d, b) { for (var p in b) if (b.hasOwnProperty(p)) d[p] = b[p]; };
    return function (d, b) {
        extendStatics(d, b);
        function __() { this.constructor = d; }
        d.prototype = b === null ? Object.create(b) : (__.prototype = b.prototype, new __());
    };
})();
Object.defineProperty(exports, "__esModule", { value: true });
var runtime = require("./runtime");
var ForkDirective = (function (_super) {
    __extends(ForkDirective, _super);
    function ForkDirective() {
        return _super.call(this, ":fork") || this;
    }
    ForkDirective.prototype.parse = function (node, ctx) {
        return null;
    };
    return ForkDirective;
}(runtime.ADirective));
exports.suite = new runtime.Suite();
exports.suite.addDirective(new ForkDirective());
//# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoiZGVmYXVsdC1zdWl0ZS5qcyIsInNvdXJjZVJvb3QiOiIiLCJzb3VyY2VzIjpbIi4uL3NyYy9kZWZhdWx0LXN1aXRlLnRzIl0sIm5hbWVzIjpbXSwibWFwcGluZ3MiOiI7Ozs7Ozs7Ozs7OztBQUFBLG1DQUFxQztBQUdyQztJQUE0QixpQ0FBa0I7SUFFMUM7ZUFDSSxrQkFBTSxPQUFPLENBQUM7SUFDbEIsQ0FBQztJQUVNLDZCQUFLLEdBQVosVUFBYSxJQUFVLEVBQUUsR0FBa0I7UUFDdkMsTUFBTSxDQUFDLElBQUksQ0FBQztJQUNoQixDQUFDO0lBRUwsb0JBQUM7QUFBRCxDQUFDLEFBVkQsQ0FBNEIsT0FBTyxDQUFDLFVBQVUsR0FVN0M7QUFFWSxRQUFBLEtBQUssR0FBa0IsSUFBSSxPQUFPLENBQUMsS0FBSyxFQUFFLENBQUM7QUFDeEQsYUFBSyxDQUFDLFlBQVksQ0FBQyxJQUFJLGFBQWEsRUFBRSxDQUFDLENBQUMiLCJzb3VyY2VzQ29udGVudCI6WyJpbXBvcnQgKiBhcyBydW50aW1lIGZyb20gXCIuL3J1bnRpbWVcIjtcclxuXHJcblxyXG5jbGFzcyBGb3JrRGlyZWN0aXZlIGV4dGVuZHMgcnVudGltZS5BRGlyZWN0aXZlIHtcclxuXHJcbiAgICBjb25zdHJ1Y3RvcigpIHtcclxuICAgICAgICBzdXBlcihcIjpmb3JrXCIpO1xyXG4gICAgfVxyXG5cclxuICAgIHB1YmxpYyBwYXJzZShub2RlOiBOb2RlLCBjdHg6IHJ1bnRpbWUuV05vZGUpOiBydW50aW1lLldOb2RlW10ge1xyXG4gICAgICAgIHJldHVybiBudWxsO1xyXG4gICAgfVxyXG5cclxufVxyXG5cclxuZXhwb3J0IGNvbnN0IHN1aXRlOiBydW50aW1lLlN1aXRlID0gbmV3IHJ1bnRpbWUuU3VpdGUoKTtcclxuc3VpdGUuYWRkRGlyZWN0aXZlKG5ldyBGb3JrRGlyZWN0aXZlKCkpOyJdfQ==