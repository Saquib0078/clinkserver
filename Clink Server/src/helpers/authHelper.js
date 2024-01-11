const jwt = require('jsonwebtoken');
const {TemporaryUserModel} = require("../models/userModels");
const {generateOTP, sendOTP} = require("../managers/otpManager");
const {respondSuccess, respondFailed} = require("../managers/responseManager");

const getJWT = (num) => {
    let data = {
        time: Date(), num
    }

    let jwtSecretKey = process.env.JWT_SECRET_KEY;
    return jwt.sign(data, jwtSecretKey, {
        expiresIn: "100d"
    });
}

const initOTP = async (num) => {

    /** Check if any old temporary data exists or not */
    const oldTemp = await TemporaryUserModel.findOne({num});
    if (oldTemp) {
        /** Delete old temporary data */
        await TemporaryUserModel.deleteOne({num});
    }

    let otp = generateOTP();

    const expirationTime = new Date(Date.now() + 5 * 60 * 1000); // 5 minutes from now

    const temp = new TemporaryUserModel({
        num, otp, time: expirationTime.toISOString()
    });

    await temp.save();
    console.log(`OTP for number ${num} is ${otp}`);

    /** Send OTP to number */
    sendOTP(num, otp);

}

const verifyOTP = async (num, otp, callback) => {
    /** Check if any old temporary data exists or not */
    const oldTemp = await TemporaryUserModel.findOne({num});
    if (!oldTemp) {
        /** Invalid Attempt */
        return callback("301");
    }

    const expirationTime = new Date(oldTemp.time);
    const currentTime = new Date();
    if (oldTemp.otp !== otp) {
        return callback("003");
    }

    if (currentTime > expirationTime) {
        return callback("301");
    }

    /** Delete old temporary data */
    await TemporaryUserModel.deleteOne({num});

    callback()
}

module.exports = {
    getJWT, initOTP, verifyOTP
}