const express = require('express')
const router = express.Router()
const authMiddleware = require('../middleware/authMiddleware')
const { supabase, supabaseAdmin } = require('../supabaseClient')

// POST /api/change-password
router.post('/change-password', authMiddleware, async (req, res) => {
    const { current_password, new_password } = req.body

    if (!current_password || !new_password) {
        return res.status(400).json({ message: 'Current and new password are required' })
    }

    if (new_password.length < 6) {
        return res.status(400).json({ message: 'New password must be at least 6 characters' })
    }

    if (current_password === new_password) {
        return res.status(400).json({ message: 'New password must be different from current password' })
    }

    try {
        // Verify current password by attempting sign-in
        const { error: signInError } = await supabase.auth.signInWithPassword({
            email: req.user.email,
            password: current_password
        })

        if (signInError) {
            return res.status(401).json({ message: 'Current password is incorrect' })
        }

        // Update password using admin client
        const { error: updateError } = await supabaseAdmin.auth.admin.updateUserById(
            req.user.id,
            { password: new_password }
        )

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
