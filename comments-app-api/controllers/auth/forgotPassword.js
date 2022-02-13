const User = require('../../database/schemas/User')
var CryptoJS = require("crypto-js");
var encryptedBase64Key = 'bXVzdGJlMTZieXRlc2tleQ==';
var parsedBase64Key = CryptoJS.enc.Base64.parse(encryptedBase64Key);

const forgotPassword = async (req, res) => {
    const isSignuped = await User.countDocuments({ email: req.body.email }, { limit: 1 })
    // 0 means that the user isn't yet signuped, 1 means that the user has already signuped for the app
    if (isSignuped === 0) {
        res.json({
            "message": "No user found"
        })
    }
    else {
        const user = await User.findOne({ email: req.body.email })

            var dbPassCode = CryptoJS.AES.decrypt( user.secretCode, parsedBase64Key, {
                mode: CryptoJS.mode.ECB,
                padding: CryptoJS.pad.Pkcs7
                } );
                // console.log( “DecryptedData = “ + decryptedData );
                // this is the decrypted data as a string
                var dbPassCode = dbPassCode.toString( CryptoJS.enc.Utf8 );

            var reqPassCode = CryptoJS.AES.decrypt( req.body.secretCode, parsedBase64Key, {
                mode: CryptoJS.mode.ECB,
                padding: CryptoJS.pad.Pkcs7
                } );
                // console.log( “DecryptedData = “ + decryptedData );
                // this is the decrypted data as a string
                var reqPassCode = reqPassCode.toString( CryptoJS.enc.Utf8 );
                

        if (reqPassCode === dbPassCode) {
            res.json({
                "password": user.password
            })
        }
        else {
            res.json({
                "message": "Invalid credentials"
            })
        }
    }
}

module.exports = forgotPassword