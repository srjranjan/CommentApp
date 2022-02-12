const Comments = require('../database/schemas/Comments')

const viewComments = async (req, res) => {
    const allComments = await Comments.find({})
    res.json(allComments)
}

module.exports = viewComments