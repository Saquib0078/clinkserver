const express = require('express');
const {getBroadcastMedia} = require("../controllers/user/broadcastController");
const router = express.Router();

router.get("/getBroadcastMedia/:broadcastMediaID", getBroadcastMedia);


module.exports = router;