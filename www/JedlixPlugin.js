var exec = require('cordova/exec');

exports.coolMethod = function (userid, accesstoken, success, error) {
    var options = {};
    options.userid = userid;
    options.accesstoken = accesstoken;
    options.vehicleid = "";

    exec(success, error, 'JedlixPlugin', 'coolMethod', [options]);
};
