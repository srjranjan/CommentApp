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


## Android App

`(Note: This app requires min API 26/ Android Oreo version to work Properly)`

To run this app, Please Download the APK from link below:

Android APK [Downlaod](/Comment_App.apk)

- **Comment app** is built using  [**Kotlin**](https://kotlinlang.org/). 
- It is `single activity` based architecture which uses  `Retrofit` lilbrary for networking and `moshi` for GSON converter. 
- `Jetpack navigation` is also used to navigate between fragments. `ViewBinding` is used instead of expensive `findviewById`.


## Backend

**Our Backend is fully functional and deployed on a server. The tech stacks we used:**

- [**NodeJS**](https://nodejs.org/) - Runtime environment
- [**Express**](https://expressjs.com/) - Framework used to build the API
- [**MongoDB**](https://mongodb.com/) - No SQL database which is used for the storing the todo
- [**Mongoose**](https://mongoosejs.com/) - Library which is used to interact with the MongoDB database 
- [**Deta**]( https://deta.sh/ ) - Platform where the API is been hosted

