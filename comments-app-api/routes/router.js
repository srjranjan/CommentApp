const router = require('express').Router()
const signup = require('../controllers/auth/signup')
const login = require('../controllers/auth/login')
const forgotPassword = require('../controllers/auth/forgotPassword')
const logout = require('../controllers/auth/logout')
const postComment = require('../controllers/post-comment')
const viewComments = require('../controllers/view-comments')

router.post('/auth/login', login)
router.post('/auth/signup', signup)
router.post('/auth/forgot-password', forgotPassword)
router.post('/auth/logout', logout)
router.post('/post-comment', postComment)
router.get('/view-comments', viewComments)

module.exports = router
