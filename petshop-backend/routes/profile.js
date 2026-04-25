const express = require('express')
const router = express.Router()
const authMiddleware = require('../middleware/authMiddleware')
const { supabaseAdmin } = require('../supabaseClient')

// GET /api/profile
router.get('/profile', authMiddleware, async (req, res) => {
    try {
        const { data, error } = await supabaseAdmin
            .from('users')
            .select('id, email, full_name, role, profile_picture_url')
            .eq('id', req.user.id)
            .single()

        if (error) {
            return res.status(404).json({ message: 'Profile not found' })
        }

        // Map database columns to app expected format
        const profile = {
            id: data.id,
            email: data.email,
            name: data.full_name,
            role: data.role,
            profile_picture_url: data.profile_picture_url
        }

        return res.status(200).json({
            message: 'Profile retrieved successfully',
            profile: profile
        })
    } catch (err) {
        console.error('Get profile error:', err.message)
        return res.status(500).json({ message: 'Server error' })
    }
})

// PUT /api/profile
router.put('/profile', authMiddleware, async (req, res) => {
    const { name, phone } = req.body // phone column doesn't exist in users table currently, we can skip or add it

    if (!name) {
        return res.status(400).json({ message: 'Name is required' })
    }

    try {
        const { data, error } = await supabaseAdmin
            .from('users')
            .update({ full_name: name })
            .eq('id', req.user.id)
            .select()
            .single()

        if (error) {
            return res.status(400).json({ message: error.message })
        }

        return res.status(200).json({
            message: 'Profile updated successfully',
            profile: {
                id: data.id,
                email: data.email,
                name: data.full_name
            }
        })
    } catch (err) {
        console.error('Update profile error:', err.message)
        return res.status(500).json({ message: 'Server error' })
    }
})

module.exports = router
