var exec = require('cordova/exec');

exports.coolMethod = function(userid, accesstoken, vehicleid, success, error) {
    try{
        exec(success, error, 'JedlixPlugin', 'coolMethod', [userid, accesstoken, vehicleid]);
    }
    catch(err) {
        error(err);
    }
    
};

exports.chargerMethod = function(userid, accesstoken, charginglocationid, success, error) {
    try{
        exec(success, error, 'JedlixPlugin', 'chargerMethod', [userid, accesstoken, charginglocationid]);
    }
    catch(err) {
        error(err);
    }
};