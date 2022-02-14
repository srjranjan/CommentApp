# CommentApp
built in requirement for ZOHO recuitment assigment

The API base URL: https://comments-app-api.deta.dev/

# Endpoints

The available endpoints:

- `auth/login` (POST): This would take in `email` and `password` of the user and validates them.
- `auth/signup` (POST) : This would take in `email`, `password` and `secretCode` and creates a new document in the database for the user.
- `auth/forgot-password` (POST) : This would take in `secretCode` and `email` and return the password.
- `auth/logout` (POST) : This would change `isLogged` to `false`.
- `post-comment` (POST) : This would take in `email` and the `comment` and creates a new comment.
- `view-comments` (GET) : This returns all the comments.
