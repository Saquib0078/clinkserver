const {AppModel} = require("../../models/appModels");
const {respondSuccess, respondSuccessWithData, respondFailed} = require("../../managers/responseManager");
const {getIndianTime} = require("../../managers/timeManager");
let APP_CONFIGS;

const loadDefaults = async (callback, hardLoad = false) => {
    if (APP_CONFIGS && !hardLoad) {
        return callback();
    }

    APP_CONFIGS = await AppModel.find({active: true});

    callback();
}

const appStatus = async (req, res) => {
    // let s = new AppModel({
    //     version : req.params.version,
    //     versionName : req.params.version + "as",
    //     active: true,
    //     primaryVersion : true,
    //     date : getIndianTime()
    // });
    // await s.save();

    let version = req.params.version;

    if (!version) {
        return respondFailed(res, "000");
    }

    version = Number(version);

    await loadDefaults(() => {

        let config;
        for (let i = 0; i < APP_CONFIGS.length; i++) {
            if (APP_CONFIGS[i]["version"] === version) {
                config = APP_CONFIGS[i];
                break;
            }
        }

        let latestVersionConfig = APP_CONFIGS[APP_CONFIGS.length - 1];
        let latestVersion = latestVersionConfig["version"];


        if (latestVersion !== version) {
            
            let updateData = {
                latestVersion, latestVersionName: latestVersionConfig["versionName"],
            };

            if (!config) {
                return respondFailed(res, "901", updateData);
            }

            if (config["maintenance"]) {
                return respondFailed(res, "900", {
                    msg: config["maintenanceMsg"]
                });
            }

            return respondSuccessWithData(res, updateData)
        }


        respondSuccessWithData(res, APP_CONFIGS);
    });
}

const update = async () => {
    await loadDefaults(() => {

    }, true);
}

module.exports = {
    appStatus
}