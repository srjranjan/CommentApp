const User = require('../../database/schemas/User')

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
        if (user.password === req.body.password) {
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