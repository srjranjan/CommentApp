const express = require("express")
const cors = require("cors")
const dotenv = require("dotenv")
const mongoinit = require('./database/mongo')
const router = require('./routes/router')

dotenv.config()
const app = express()
mongoinit(process.env.MONGODB_URL)

app.get('/', (req, res) => {
    res.json({ "status": "Alive!" })
})

app.use(cors());
app.use(express.json());
app.use(express.urlencoded({ extended: false }));
app.use(router)

const port = process.env.PORT || 3000
app.listen(port, () => {
    console.log(`Server is listening at port ${port}`)
})

module.exports = app