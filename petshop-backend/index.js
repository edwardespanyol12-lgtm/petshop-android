require('dotenv').config()
const express = require('express')
const cors = require('cors')

const authRoutes = require('./routes/auth')
const profileRoutes = require('./routes/profile')
const passwordRoutes = require('./routes/password')
const dashboardRoutes = require('./routes/dashboard')

const app = express()

app.use(cors())
app.use(express.json())

// Health check
app.get('/health', (req, res) => {
    res.json({ status: 'ok', app: 'PetShop API', timestamp: new Date().toISOString() })
})

// API routes
app.use('/api', authRoutes)
app.use('/api', profileRoutes)
app.use('/api', passwordRoutes)
app.use('/api', dashboardRoutes)

// 404 handler
app.use((req, res) => {
    res.status(404).json({ message: 'Route not found' })
})

// Global error handler
app.use((err, req, res, next) => {
    console.error(err.stack)
    res.status(500).json({ message: 'Internal server error' })
})

const PORT = process.env.PORT || 3000
app.listen(PORT, () => {
    console.log(`PetShop API running on port ${PORT}`)
})
