const express = require('express')
const router = express.Router()
const authMiddleware = require('../middleware/authMiddleware')
const { supabaseAdmin } = require('../supabaseClient')
const bcrypt = require('bcrypt')

// POST /api/change-password
router.post('/change-password', authMiddleware, async (req, res) => {
    const { current_password, new_password } = req.body

    if (!current_password || !new_password) {
        return res.status(400).json({ message: 'Current and new password are required' })
    }

    try {
        // Fetch current user from DB
        const { data: user, error } = await supabaseAdmin
            .from('users')
            .select('password')
            .eq('id', req.user.id)
            .single()

        if (error || !user) {
            return res.status(401).json({ message: 'Unauthorized' })
        }

        // Verify current password
        let isMatch = false
        if (user.password.startsWith('$2a$') || user.password.startsWith('$2b$')) {
            isMatch = await bcrypt.compare(current_password, user.password)
        } else {
            // Legacy plain text check
            isMatch = (current_password === user.password)
        }

        if (!isMatch) {
            return res.status(401).json({ message: 'Current password is incorrect' })
        }

        // Hash new password
        const hashedPassword = await bcrypt.hash(new_password, 10)

        // Update in DB
        const { error: updateError } = await supabaseAdmin
            .from('users')
            .update({ password: hashedPassword })
            .eq('id', req.user.id)

        if (updateError) {
            return res.status(400).json({ message: updateError.message })
        }

        return res.status(200).json({ message: 'Password changed successfully' })
    } catch (err) {
        console.error('Change password error:', err.message)
        return res.status(500).json({ message: 'Server error' })
    }
})

module.exports = router
