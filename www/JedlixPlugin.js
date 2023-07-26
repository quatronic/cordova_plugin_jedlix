var exec = require('cordova/exec');

exports.coolMethod = function(url, apikey, userid, accesstoken, vehicleid, success, error) {
    exec(success, error, 'JedlixPlugin', 'coolMethod', [url, apikey, userid, accesstoken, vehicleid]);
};

exports.chargerMethod = function(url, apikey, userid, accesstoken, charginglocationid, success, error) {
    exec(success, error, 'JedlixPlugin', 'chargerMethod', [url, apikey, userid, accesstoken, charginglocationid]);
};