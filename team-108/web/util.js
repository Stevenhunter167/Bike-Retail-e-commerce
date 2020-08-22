function getUrlVars() {
    var vars = {};
    var parts = window.location.href.replace(/[?&]+([^=&]+)=([^&]*)/gi, function(m,key,value) {
        vars[key] = value;
    });
    return vars;
}

function getTime() {
    return new Date().getTime() / 1000;
}

function makeParamString(obj) {
    if (Object.keys(obj).length === 0) {
        return "";
    }

    let res = "?";
    for (let k of Object.keys(obj)) {
        res += k + "=" + obj[k] + "&";
    }
    return res.slice(0, res.length - 1);
}

function clone(obj) {
    return JSON.parse(JSON.stringify(obj));
}