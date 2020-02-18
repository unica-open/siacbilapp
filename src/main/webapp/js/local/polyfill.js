/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
!function() {

    // String.prototype.trim
    String.prototype.trim=String.prototype.trim||function(){return this.replace(/^[\s\uFEFF\xA0]+|[\s\uFEFF\xA0]+$/g,'');};

    // Function.prototype.bind
    Function.prototype.bind=Function.prototype.bind||function(b){if(typeof this!=="function"){throw new TypeError("Function.prototype.bind - what is trying to be bound is not callable");}
    var a=[].slice,f=a.call(arguments,1),e=this,c=function(){},d=function(){return e.apply(this instanceof c?this:b||window,f.concat(a.call(arguments)));};
    c.prototype=this.prototype;d.prototype=new c();return d;};

    // Array.prototype.forEach
    Array.prototype.forEach=Array.prototype.forEach||function(r,t){var n,o;if(this==null){throw new TypeError("this is null or not defined")}var i=Object(this);var e=i.length>>>0;
    if(typeof r!=="function"){throw new TypeError(r+" is not a function")}if(arguments.length>1){n=t}o=0;while(o<e){var f;if(o in i){f=i[o];r.call(n,f,o,i)}o++}};

    // Array.protytype.reduce
    Array.prototype.reduce=Array.prototype.reduce||function(r){"use strict";if(this==null){throw new TypeError("Array.prototype.reduce called on null or undefined")}
    if(typeof r!=="function"){throw new TypeError(r+" is not a function")}var e=Object(this),t=e.length>>>0,n=0,o;
    if(arguments.length==2){o=arguments[1]}else{while(n<t&&!(n in e)){n++}if(n>=t){throw new TypeError("Reduce of empty array with no initial value")}o=e[n++]}
    for(;n<t;n++){if(n in e){o=r(o,e[n],n,e)}}return o};

    // Array.prototype.map
    Array.prototype.map=Array.prototype.map||function(r,t){var n,o,e;if(null==this)throw new TypeError(" this is null or not defined");var i=Object(this),a=i.length>>>0;
    if("function"!=typeof r)throw new TypeError(r+" is not a function");for(arguments.length>1&&(n=t),o=new Array(a),e=0;a>e;){var p,f;e in i&&(p=i[e],f=r.call(n,p,e,i),o[e]=f),e++}
    return o};

    // Array.prototype.indexOf
    Array.prototype.indexOf=Array.prototype.indexOf||function(r,t){var n;if(null==this)throw new TypeError('"this" is null or not defined');var e=Object(this),i=e.length>>>0;
    if(0===i)return-1;var a=+t||0;if(Math.abs(a)===1/0&&(a=0),a>=i)return-1;for(n=Math.max(a>=0?a:i-Math.abs(a),0);i>n;){if(n in e&&e[n]===r)return n;n++}return-1};

    // Array.prototype.filter
    Array.prototype.filter=Array.prototype.filter||function(r){"use strict";if(void 0===this||null===this)throw new TypeError;var t=Object(this),e=t.length>>>0;
    if("function"!=typeof r)throw new TypeError;for(var i=[],o=arguments.length>=2?arguments[1]:void 0,n=0;e>n;n++)if(n in t){var f=t[n];r.call(o,f,n,t)&&i.push(f)}return i};

    // Object.create
    if(typeof Object.create!="function"){Object.create=function(t){var e=function(){};return function(n,r){if(n!==Object(n)&&n!==null){throw TypeError("Argument must be an object, or null")}
    e.prototype=n||{};var o=new e;e.prototype=null;if(r!==t){Object.defineProperties(o,r)}if(n===null){o.__proto__=null}return o}}()}

    // Object.keys
    Object.keys=Object.keys||function(){"use strict";var t=Object.prototype.hasOwnProperty,r=!{toString:null}.propertyIsEnumerable("toString"),
    e=["toString","toLocaleString","valueOf","hasOwnProperty","isPrototypeOf","propertyIsEnumerable","constructor"],o=e.length;
    return function(n){if("function"!=typeof n&&("object"!=typeof n||null===n))throw new TypeError("Object.keys called on non-object");
    var c,l,p=[];for(c in n)t.call(n,c)&&p.push(c);if(r)for(l=0;l<o;l++)t.call(n,e[l])&&p.push(e[l]);return p}}();
}();