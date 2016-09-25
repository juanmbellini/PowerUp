function filteredSearch(filter, value) { //
    var params = {};
    var URL = "/search?name=&";

    params[filter] = [value];


    URL += "filters=" + encodeURIComponent(JSON.stringify(params));
    window.location = URL;
};