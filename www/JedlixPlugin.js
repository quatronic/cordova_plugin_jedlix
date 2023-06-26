var exec = require('cordova/exec');

exports.coolMethod = function(userid, accesstoken, vehicleid, success, error) {
    exec(success, error, 'JedlixPlugin', 'coolMethod', [userid, accesstoken, vehicleid]);
};

exports.chargerMethod = function(userid, accesstoken, charginglocationid, success, error) {
    exec(success, error, 'JedlixPlugin', 'chargerMethod', [userid, accesstoken, charginglocationid]);
};

exports.testMethod = function(success, error) {
    alert(typeof(success));
    exec(success, error, 'JedlixPlugin', 'testMethod');
    alert("Finished testmethod");
};