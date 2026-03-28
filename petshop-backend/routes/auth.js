const express = require('express')
const router = express.Router()
const { supabase, supabaseAdmin } = require('../supabaseClient')

// POST /api/register
router.post('/register', async (req, res) => {
    const { name, email, password } = req.body

    if (!name || !email || !password) {
        return res.status(400).json({ message: 'Name, email and password are required' })
    }

    if (password.length < 6) {
        return res.status(400).json({ message: 'Password must be at least 6 characters' })
    }

    try {
        const { data, error } = await supabaseAdmin.auth.admin.createUser({
            email,
            password,
            email_confirm: true,
            user_metadata: { name }
        })

        if (error) {
            if (error.message.toLowerCase().includes('already registered') ||
                error.message.toLowerCase().includes('already exists')) {
                return res.status(400).json({ message: 'Email is already registered' })
            }
            return res.status(400).json({ message: error.message })
        }

        return res.status(200).json({
            message: 'Registration successful! Please login.',
            user: {
                id: data.user.id,
                email: data.user.email,
                name
            }
        })
    } catch (err) {
        console.error('Register error:', err.message)
        return res.status(500).json({ message: 'Server error during registration' })
    }
})

// POST /api/login
router.post('/login', async (req, res) => {
    const { email, password } = req.body

    if (!email || !password) {
        return res.status(400).json({ message: 'Email and password are required' })
    }

    try {
        const { data, error } = await supabase.auth.signInWithPassword({ email, password })

        if (error) {
            return res.status(401).json({ message: 'Invalid email or password' })
        }

        return res.status(200).json({
            message: 'Login successful',
            token: data.session.access_token,
            user: {
                id: data.user.id,
                email: data.user.email,
                name: data.user.user_metadata?.name || ''
            }
        })
    } catch (err) {
        console.error('Login error:', err.message)
        return res.status(500).json({ message: 'Server error during login' })
    }
})

module.exports = router
