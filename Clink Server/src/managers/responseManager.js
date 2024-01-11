// const {sendInternalError} = require("../support/serverSupport");
const MISSING_PARAMETERS_MSG = {status: "failed", code: "000", msg: "Missing Parameters"};
const CODE_MESSAGES = {
    "001": "Invalid Number",
    "002": "Invalid Name",
    "003": "Invalid OTP",
    "101": "Number Already Exists",
    "102": "Number Not Exists",
    "201": "Account Incomplete",
    "202": "Account Not Approved Yet",
    "203": "Account Banned",
    "301": "OTP Expired",
    "302": "Session Expired",
    "900": "App Under Maintenance",
    "901": "Update Required"
}
const throwError = (res, err) => {
    console.log(err);
    res.status(200).send({status: "error", error: err});
}

const throwInternalError = (req, res, err) => {
    console.log(err);
    res.status(500).send({status: "error", error: err});
    // sendInternalError(req, res, err)
}

const respondSuccess = (res) => {
    res.status(200).send({status: "success"});
}

const respondSuccessWithData = (res, ...data) => {
    /* if (data.length === 1) {
           data = data[0];
   } */

    if (Array.isArray(data[0]) === true) {
        data = data[0];
    }

    res.status(200).send({status: "success", data});
}

const respondFailed = (res, code, data) => {
    if (code === "000") {
        return respondDeclared(res, MISSING_PARAMETERS_MSG);
    }
    res.status(200).send({status: "failed", code, msg: CODE_MESSAGES[code], data});
}

const respond = (res, data) => {
    res.status(200).send(data);
}

const respondDeclared = (res, msg) => {
    res.status(200).send(msg);
}

module.exports = {
    throwError,
    throwInternalError,
    respondSuccess,
    respondSuccessWithData,
    respondFailed,
    respond,
    respondDeclared,
    MISSING_PARAMETERS_MSG
}