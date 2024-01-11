const mongoose = require("mongoose");


const appSchema = new mongoose.Schema({
    maintenance: {type: Boolean, default: false},
    maintenanceMsg: {type: String, required: false},
    version: {type: Number, unique: true},
    versionName: {type: String, unique: true},
    active: {type: Boolean, default: true},
    date: {type: String, required: true},
});

const AppModel = new mongoose.model("app-infos", appSchema);

module.exports = {
    AppModel
}
