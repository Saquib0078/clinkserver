const rateLimit = require("express-rate-limit");

const otpRateLimit = rateLimit({
    windowMs: 5 * 60 * 1000, // 5 min in milliseconds
    max: 15,
    message: {status: "failed", "message": "Max Retry Attempts Exceeded. Try Again After 5 Minutes", code: "005"},
    statusCode: 429,
    headers: true,
});

const loginRateLimit = rateLimit({
    windowMs: 5 * 60 * 1000, // 5 min in milliseconds
    max: 15,
    message: {status: "failed", "message": "Max Retry Attempts Exceeded. Try Again After 5 Minutes", code: "005"},
    statusCode: 429,
    headers: true,
});

const registerRateLimit = rateLimit({
    windowMs: 10 * 60 * 1000, // 5 min in milliseconds
    max: 15,
    message: {status: "failed", "message": "Max Retry Attempts Exceeded. Try Again After 10 Minutes", code: "005"},
    statusCode: 429,
    headers: true,
});

module.exports = {
    otpRateLimit, loginRateLimit, registerRateLimit
}