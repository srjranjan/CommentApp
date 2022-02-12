const User = require('../../database/schemas/User')

const signup = async (req, res) => {
    const isSignuped = await User.countDocuments({ email: req.body.email }, { limit: 1 })
    // 0 means that the user isn't yet signuped, 1 means that the user has already signuped for the app
    if (isSignuped === 0) {
        const newUser = new User({
            email: req.body.email,
            password: req.body.password,
            secretCode: req.body.secretCode,
            isLogged: req.body.isLogged
        })
        newUser.save()
        res.json({
            "message": "Successfully created a new user in the database"
        })
    }
    else {
        res.json({
            "message": "User already exists"
        })
    }
}

module.exports = signup