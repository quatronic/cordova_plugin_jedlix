var exec = require('cordova/exec');

exports.coolMethod = function(apikey, userid, accesstoken, vehicleid, success, error) {
    exec(success, error, 'JedlixPlugin', 'coolMethod', [apikey, userid, accesstoken, vehicleid]);
};

exports.chargerMethod = function(apikey, userid, accesstoken, charginglocationid, success, error) {
    exec(success, error, 'JedlixPlugin', 'chargerMethod', [apikey, userid, accesstoken, charginglocationid]);
};