package com.srj.commentapp

data class CommentModal(
    val email: String, val comments: String
)

data class responseMessage(
    val message: String
)

data class postComment(
    val message: String,
    val comment: String,
    val email: String
)

