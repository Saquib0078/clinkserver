const {SecondaryUserModel, PrimaryUserModel} = require("../../models/userModels");
const {respondFailed, respondSuccess, throwError} = require("../../managers/responseManager");
const setUserInfo = async (req, res) => {
    let {lang, edu, intr, dist, teh, vill, lMark, ward, booth, wpn, insta, fb} = req.body;

    if (!lang || !edu || !intr || !dist || !teh || !vill || !lMark || !wpn) {
        return respondFailed(res, "000");
    }

    let num = req.user.num;

    try {
        let userInfo = await SecondaryUserModel.findOne({num})

        if (userInfo) {
            await SecondaryUserModel.deleteOne({num});
        }

        let userDoc = new SecondaryUserModel({
            num, lang, edu, intr, dist, teh, vill, lMark, ward, booth, wpn, insta, fb
        });

        await userDoc.save();

        await PrimaryUserModel.updateOne({num}, {$unset: {completed: ''}});

        respondSuccess(res);

    } catch (e) {
        throwError(res, e)
    }

}

module.exports = {
    setUserInfo
}