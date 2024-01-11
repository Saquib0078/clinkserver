const mongoose = require("mongoose");


const meetSchema = new mongoose.Schema({
    meetName:{type:String},
    meetDescription:{type:String},
    time:{type:String},
    live: {type: Boolean, default: true},
    date: {type: String},
    imageID: {type: String},

},{
    timestamps: true
});

module.exports = mongoose.model("meeting", meetSchema);