const User = require('../../database/schemas/User')

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
        if (req.body.secretCode === user.secretCode) {
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