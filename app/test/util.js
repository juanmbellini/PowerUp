'use strict';

/*
    This file contains various utilities or helpers which may be useful for different test suites.
    Polyfills are obtained from MDN web docs, e.g. https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Array/find#Polyfill
 */

/**
 * Parses a request's query params into an object.
 * Obtained from https://stackoverflow.com/a/33530013/2333689
 *
 * @param url
 * @return {Object}
 */
function extractQueryParams(url) {
    var queryParams = url.split('?')[1];
    var match;
    var params = {};

    // replace addition symbol with a space
    var pl = /\+/g;

    // delimit params
    var search = /([^&=]+)=?([^&]*)/g;

    var decode = function (s) {
        return decodeURIComponent(s.replace(pl, ' '));
    };

    while (match = search.exec(queryParams)) {
        params[decode(match[1])] = decode(match[2]);
    }

    return params;
}

// Polyfill for Array.prototype.includes
// https://tc39.github.io/ecma262/#sec-array.prototype.includes
if (!Array.prototype.includes) {
    Object.defineProperty(Array.prototype, 'includes', {
        value: function(searchElement, fromIndex) {

            // 1. Let O be ? ToObject(this value).
            if (this === null) {
                throw new TypeError('"this" is null or not defined');
            }

            var o = Object(this);

            // 2. Let len be ? ToLength(? Get(O, "length")).
            var len = o.length >>> 0;

            // 3. If len is 0, return false.
            if (len === 0) {
                return false;
            }

            // 4. Let n be ? ToInteger(fromIndex).
            //    (If fromIndex is undefined, this step produces the value 0.)
            var n = fromIndex | 0;

            // 5. If n ≥ 0, then
            //  a. Let k be n.
            // 6. Else n < 0,
            //  a. Let k be len + n.
            //  b. If k < 0, let k be 0.
            var k = Math.max(n >= 0 ? n : len - Math.abs(n), 0);

            function sameValueZero(x, y) {
                return x === y || (typeof x === 'number' && typeof y === 'number' && isNaN(x) && isNaN(y));
            }

            // 7. Repeat, while k < len
            while (k < len) {
                // a. Let elementK be the result of ? Get(O, ! ToString(k)).
                // b. If SameValueZero(searchElement, elementK) is true, return true.
                // c. Increase k by 1.
                if (sameValueZero(o[k], searchElement)) {
                    return true;
                }
                k++;
            }

            // 8. Return false
            return false;
        }
    });
}


// Polyfill for Array.prototype.find
// https://tc39.github.io/ecma262/#sec-array.prototype.find
if (!Array.prototype.find) {
    Object.defineProperty(Array.prototype, 'find', {
        value: function(predicate) {
            // 1. Let O be ? ToObject(this value).
            if (this === null) {
                throw new TypeError('"this" is null or not defined');
            }

            var o = Object(this);

            // 2. Let len be ? ToLength(? Get(O, "length")).
            var len = o.length >>> 0;

            // 3. If IsCallable(predicate) is false, throw a TypeError exception.
            if (typeof predicate !== 'function') {
                throw new TypeError('predicate must be a function');
            }

            // 4. If thisArg was supplied, let T be thisArg; else let T be undefined.
            var thisArg = arguments[1];

            // 5. Let k be 0.
            var k = 0;

            // 6. Repeat, while k < len
            while (k < len) {
                // a. Let Pk be ! ToString(k).
                // b. Let kValue be ? Get(O, Pk).
                // c. Let testResult be ToBoolean(? Call(predicate, T, « kValue, k, O »)).
                // d. If testResult is true, return kValue.
                var kValue = o[k];
                if (predicate.call(thisArg, kValue, k, o)) {
                    return kValue;
                }
                // e. Increase k by 1.
                k++;
            }

            // 7. Return undefined.
            return undefined;
        }
    });
}


// Polyfill for Array.prototype.copyWithin
if (!Array.prototype.copyWithin) {
    Array.prototype.copyWithin = function(target, start/* , end*/) {
        // Steps 1-2.
        if (this === null) {
            throw new TypeError('this is null or not defined');
        }

        var O = Object(this);

        // Steps 3-5.
        var len = O.length >>> 0;

        // Steps 6-8.
        var relativeTarget = target >> 0;

        var to = relativeTarget < 0 ?
            Math.max(len + relativeTarget, 0) :
            Math.min(relativeTarget, len);

        // Steps 9-11.
        var relativeStart = start >> 0;

        var from = relativeStart < 0 ?
            Math.max(len + relativeStart, 0) :
            Math.min(relativeStart, len);

        // Steps 12-14.
        var end = arguments[2];
        var relativeEnd = end === undefined ? len : end >> 0;

        var final = relativeEnd < 0 ?
            Math.max(len + relativeEnd, 0) :
            Math.min(relativeEnd, len);

        // Step 15.
        var count = Math.min(final - from, len - to);

        // Steps 16-17.
        var direction = 1;

        if (from < to && to < (from + count)) {
            direction = -1;
            from += count - 1;
            to += count - 1;
        }

        // Step 18.
        while (count > 0) {
            if (from in O) {
                O[to] = O[from];
            } else {
                delete O[to];
            }

            from += direction;
            to += direction;
            count--;
        }

        // Step 19.
        return O;
    };
}


// Polyfill for Array.prototype.findIndex
// https://tc39.github.io/ecma262/#sec-array.prototype.findIndex
if (!Array.prototype.findIndex) {
    Object.defineProperty(Array.prototype, 'findIndex', {
        value: function(predicate) {
            // 1. Let O be ? ToObject(this value).
            if (this === null) {
                throw new TypeError('"this" is null or not defined');
            }

            var o = Object(this);

            // 2. Let len be ? ToLength(? Get(O, "length")).
            var len = o.length >>> 0;

            // 3. If IsCallable(predicate) is false, throw a TypeError exception.
            if (typeof predicate !== 'function') {
                throw new TypeError('predicate must be a function');
            }

            // 4. If thisArg was supplied, let T be thisArg; else let T be undefined.
            var thisArg = arguments[1];

            // 5. Let k be 0.
            var k = 0;

            // 6. Repeat, while k < len
            while (k < len) {
                // a. Let Pk be ! ToString(k).
                // b. Let kValue be ? Get(O, Pk).
                // c. Let testResult be ToBoolean(? Call(predicate, T, « kValue, k, O »)).
                // d. If testResult is true, return k.
                var kValue = o[k];
                if (predicate.call(thisArg, kValue, k, o)) {
                    return k;
                }
                // e. Increase k by 1.
                k++;
            }

            // 7. Return -1.
            return -1;
        }
    });
}


// Polyfill for Object.prototype.assign
// https://developer.mozilla.org/en/docs/Web/JavaScript/Reference/Global_Objects/Object/assign#Polyfill
if (typeof Object.assign != 'function') {
    // Must be writable: true, enumerable: false, configurable: true
    Object.defineProperty(Object, "assign", {
        value: function assign(target, varArgs) { // .length of function is 2
            if (target == null) { // TypeError if undefined or null
                throw new TypeError('Cannot convert undefined or null to object');
            }

            var to = Object(target);

            for (var index = 1; index < arguments.length; index++) {
                var nextSource = arguments[index];

                if (nextSource != null) { // Skip over if undefined or null
                    for (var nextKey in nextSource) {
                        // Avoid bugs when hasOwnProperty is shadowed
                        if (Object.prototype.hasOwnProperty.call(nextSource, nextKey)) {
                            to[nextKey] = nextSource[nextKey];
                        }
                    }
                }
            }
            return to;
        },
        writable: true,
        configurable: true
    });
}
