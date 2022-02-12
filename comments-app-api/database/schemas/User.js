const mongoose = require("mongoose")
require('mongoose-type-email')

const UserSchema = mongoose.Schema({
    email: {
        type: mongoose.SchemaTypes.Email,
        required: true,
        unique: true
    },
    password: {
        type: String,
        required: true
    },
    secretCode: {
        type: String,
        required: true
    },
    isLogged: {
        type: Boolean
    }
})

module.exports = mongoose.model("User", UserSchema, "User")