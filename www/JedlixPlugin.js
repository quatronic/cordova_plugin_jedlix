var exec = require('cordova/exec');

exports.coolMethod = function(url, apikey, userid, accesstoken, vehicleid, opensessionid, success, error) {
    exec(success, error, 'JedlixPlugin', 'coolMethod', [url, apikey, userid, accesstoken, vehicleid, opensessionid]);
};

exports.chargerMethod = function(url, apikey, userid, accesstoken, charginglocationid, opensessionid, success, error) {
    exec(success, error, 'JedlixPlugin', 'chargerMethod', [url, apikey, userid, accesstoken, charginglocationid, opensessionid]);
};