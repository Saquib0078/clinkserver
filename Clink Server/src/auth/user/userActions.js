const {
    respondDeclared,
    MISSING_PARAMETERS_MSG,
    respondFailed,
    respondSuccess,
    throwError
} = require("../../managers/responseManager");
const {initOTP} = require("../../helpers/authHelper");
const resendOTP = async (req, res) => {
    let {num} = req.body;

    if (!num) {
        return respondDeclared(res, MISSING_PARAMETERS_MSG);
    }

    if (num.length !== 10) {
        return respondFailed(res, "001");
    }

    try {
        await initOTP(num)
        respondSuccess(res);
    } catch (err) {
        throwError(res, err);
    }

}

module.exports = {
    resendOTP
}