const jwt = require('jsonwebtoken')
const JWT_SECRET = process.env.JWT_SECRET || 'fallback_secret'

const authMiddleware = async (req, res, next) => {
    const authHeader = req.headers['authorization']

    if (!authHeader || !authHeader.startsWith('Bearer ')) {
        return res.status(401).json({ message: 'Unauthorized: No token provided' })
    }

    const token = authHeader.split(' ')[1]

    try {
        // Verify custom JWT token
        const decoded = jwt.verify(token, JWT_SECRET)
        
        if (!decoded) {
            return res.status(401).json({ message: 'Unauthorized: Invalid or expired token' })
        }

        // Attach user info to request
        req.user = decoded
        next()
    } catch (err) {
        console.error('Auth middleware error:', err.message)
        return res.status(401).json({ message: 'Unauthorized: Session expired or invalid' })
    }
}

module.exports = authMiddleware
