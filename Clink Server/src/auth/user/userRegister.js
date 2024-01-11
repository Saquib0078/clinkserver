const {
    respondFailed, respondDeclared, MISSING_PARAMETERS_MSG, respondSuccess, throwError, respondSuccessWithData
} = require("../../managers/responseManager");
const {PrimaryUserModel, TemporaryUserModel} = require("../../models/userModels");
const {getJWT, initOTP, verifyOTP} = require("../../helpers/authHelper");
const checkRegister = async (req, res) => {
    let {num} = req.body;

    if (!num) {
        return respondDeclared(res, MISSING_PARAMETERS_MSG);
    }

    if (num.length !== 10) {
        return respondFailed(res, "001");
    }

    try {
        const user = await PrimaryUserModel.findOne({num});
        if (user) {
            return respondFailed(res, "101");
        }

        await initOTP(num)
        respondSuccess(res);
    } catch (err) {
        throwError(res, err);
    }
}



const register = async (req, res) => {
    let {num, fName, lName, otp} = req.body;

    if (!num || !fName || !lName || !otp) {
        return respondDeclared(res, MISSING_PARAMETERS_MSG);
    }

    if (num.length !== 10) {
        return respondFailed(res, "001");
    }

    if (fName.length < 3 || fName.length > 20 || lName.length < 3 || lName.length > 20) {
        return respondFailed(res, "002");
    }

    try {
        const user = await PrimaryUserModel.findOne({num});
        if (user) {
            return respondFailed(res, "101");
        }

        await verifyOTP(num, otp, async (code) => {
            if (code) {
                return respondFailed(res, code);
            }

            let primaryUserModel = new PrimaryUserModel({
                fName, lName, num, completed: false
            });

            await primaryUserModel.save();

            respondSuccessWithData(res, {token: getJWT(num)});

        });

    } catch (err) {
        console.log(err)
        throwError(res, err);
    }
}

module.exports = {
    checkRegister, register
}