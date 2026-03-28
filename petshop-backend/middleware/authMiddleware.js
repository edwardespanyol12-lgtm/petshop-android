const { supabaseAdmin } = require('../supabaseClient')

const authMiddleware = async (req, res, next) => {
    const authHeader = req.headers['authorization']

    if (!authHeader || !authHeader.startsWith('Bearer ')) {
        return res.status(401).json({ message: 'Unauthorized: No token provided' })
    }

    const token = authHeader.split(' ')[1]

    try {
        const { data: { user }, error } = await supabaseAdmin.auth.getUser(token)

        if (error || !user) {
            return res.status(401).json({ message: 'Unauthorized: Invalid or expired token' })
        }

        req.user = user
        next()
    } catch (err) {
        console.error('Auth middleware error:', err.message)
        return res.status(500).json({ message: 'Server error during authentication' })
    }
}

module.exports = authMiddleware
