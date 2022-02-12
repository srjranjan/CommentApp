const Comments = require('../database/schemas/Comments')
const User = require('../database/schemas/User')

const postComment = async (req, res) => {
    const isSignuped = await User.countDocuments({ email: req.body.email }, { limit: 1 })
    // 0 means that the user isn't yet signuped, 1 means that the user has already signuped for the app
    if (isSignuped === 0) {
        res.json({
            "message": "User not signuped"
        })
    }
    else {
        const newComment = new Comments({
            email: req.body.email,
            comment: req.body.comment
        })
        newComment.save()
        res.json({
            "message": "Successfully created a new comment",
            "comment": {
                email: req.body.email,
                comment: req.body.comment
            }
        })
    }
}

module.exports = postComment