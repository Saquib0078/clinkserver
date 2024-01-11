const express = require('express');
const {appStatus} = require("../controllers/app/appController");
const router = express.Router();

router.post("/appStatus/v/:version", appStatus)


module.exports = router;
