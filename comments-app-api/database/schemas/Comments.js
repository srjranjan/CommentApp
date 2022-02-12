const mongoose = require('mongoose')
require('mongoose-type-email')

const CommentsSchema = mongoose.Schema({
    email: {
        type: mongoose.SchemaTypes.Email,
        required: true
    },
    comment: {
        type: String,
        required: true
    }
})

module.exports = mongoose.model("Comments", CommentsSchema, "Comments")