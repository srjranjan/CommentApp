const User = require('../../database/schemas/User')

const logout = async (req, res) => {
    const isSignuped = await User.countDocuments({ email: req.body.email }, { limit: 1 })
    // 0 means that the user isn't yet signuped, 1 means that the user has already signuped for the app
    if (isSignuped === 0) {
        res.json({
            "message": "User not found"
        })
    }
    else {
        const user = await User.findOne({ email: req.body.email })
        if (user.isLogged === true) {
            user.isLogged = false
            user.save()
            res.json({
                "message": "Successfully logged out"
            })
        }
        else {
            res.json({
                "message": "User already logged out"
            })
        }
    }
}

module.exports = logout