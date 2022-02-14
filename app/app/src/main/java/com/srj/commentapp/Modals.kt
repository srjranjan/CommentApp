package com.srj.commentapp

data class CommentModal(
    var comment: String, val email: String
)

data class PasswordModal(
    val password: String
)

data class responseMessage(
    val message: String
)

data class postComment(
    val message: String,
    val comment: String,
    val email: String
)

