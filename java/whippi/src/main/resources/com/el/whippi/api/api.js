var whippi = {};

whippi.callAction = function(action) {
    var form = document.createElement("form");
    form.method = "post";
    form.target = "_top";
    
    var input = document.createElement("input");
    input.type = "hidden";
    input.name = "action";
    input.value = action;
    form.appendChild(input);
    
    if (model != null) {
        input = document.createElement("input");
        input.type = "hidden";
        input.name = "modelType";
        input.value = modelType;
        form.appendChild(input);

        input = document.createElement("input");
        input.type = "hidden";
        input.name = "model";
        input.value = JSON.stringify(model);
        form.appendChild(input);
    }
    
    input = document.createElement("input");
    input.type = "hidden";
    input.name = "parameters";
    input.value = JSON.stringify(params);
    form.appendChild(input);
    
    document.body.appendChild(form);
    
    form.submit();
};
