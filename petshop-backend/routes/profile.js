const express = require('express')
const router = express.Router()
const authMiddleware = require('../middleware/authMiddleware')
const { supabaseAdmin } = require('../supabaseClient')

// GET /api/profile
router.get('/profile', authMiddleware, async (req, res) => {
    try {
        const { data, error } = await supabaseAdmin
            .from('profiles')
            .select('*')
            .eq('id', req.user.id)
            .single()

        if (error) {
            return res.status(404).json({ message: 'Profile not found' })
        }

        return res.status(200).json({
            message: 'Profile retrieved successfully',
            profile: data
        })
    } catch (err) {
        console.error('Get profile error:', err.message)
        return res.status(500).json({ message: 'Server error' })
    }
})

// PUT /api/profile
router.put('/profile', authMiddleware, async (req, res) => {
    const { name, phone } = req.body

    if (!name) {
        return res.status(400).json({ message: 'Name is required' })
    }

    try {
        const { data, error } = await supabaseAdmin
            .from('profiles')
            .update({ name, phone: phone || null })
            .eq('id', req.user.id)
            .select()
            .single()

        if (error) {
            return res.status(400).json({ message: error.message })
        }

        return res.status(200).json({
            message: 'Profile updated successfully',
            profile: data
        })
    } catch (err) {
        console.error('Update profile error:', err.message)
        return res.status(500).json({ message: 'Server error' })
    }
})

module.exports = router
