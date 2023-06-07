var exec = require('cordova/exec');

exports.coolMethod = function(userid, accesstoken, vehicleid, success, error) {
    exec(success, error, 'JedlixPlugin', 'coolMethod', [userid, accesstoken, vehicleid]);
};

exports.chargerMethod = function(userid, charginglocationid, success, error) {
    exec(success, error, 'JedlixPlugin', 'chargerMethod', [userid, charginglocationid]);
};