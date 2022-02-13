const User = require('../../database/schemas/User')
var CryptoJS = require("crypto-js");
var encryptedBase64Key = 'bXVzdGJlMTZieXRlc2tleQ==';
var parsedBase64Key = CryptoJS.enc.Base64.parse(encryptedBase64Key);
const login = async (req, res) => {
    const isSignuped = await User.countDocuments({ email: req.body.email }, { limit: 1 })
    // 0 means that the user isn't yet signuped, 1 means that the user has already signuped for the app
    if (isSignuped === 0) {
        res.json({
            "message": "No user found"
        })
    }
    else {
        const user = await User.findOne({ email: req.body.email })
        console.log(user)
        
// Decryption process
var dbPassword = CryptoJS.AES.decrypt( user.password, parsedBase64Key, {
mode: CryptoJS.mode.ECB,
padding: CryptoJS.pad.Pkcs7
} );
// console.log( “DecryptedData = “ + decryptedData );
// this is the decrypted data as a string
var dbPassword = dbPassword.toString( CryptoJS.enc.Utf8 );

var reqPassword = CryptoJS.AES.decrypt( req.body.password, parsedBase64Key, {
    mode: CryptoJS.mode.ECB,
    padding: CryptoJS.pad.Pkcs7
    } );
    // console.log( “DecryptedData = “ + decryptedData );
    // this is the decrypted data as a string
    var reqPassword = reqPassword.toString( CryptoJS.enc.Utf8 );
    
    
        if (dbPassword === reqPassword) {
            res.json({
                "message": "Successfully logged in!"
            })
            if (user.isLogged === false) {
                user.isLogged = true
                user.save()
            }
            else {
                return
            }
        }
        else {
            res.json({
                "message": "Wrong credentials"
            })
        }
    }
}

module.exports = login