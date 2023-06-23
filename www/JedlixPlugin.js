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
        alert("Starting from the plugin now");
        exec(success, error, 'JedlixPlugin', 'chargerMethod', [userid, accesstoken, charginglocationid]);
        alert("Ended the plugin call");
    }
    catch(err) {
        error(err);
    }
};