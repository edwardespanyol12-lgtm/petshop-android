const express = require('express')
const router = express.Router()
const { supabaseAdmin } = require('../supabaseClient')
const bcrypt = require('bcrypt')
const jwt = require('jsonwebtoken')

const JWT_SECRET = process.env.JWT_SECRET || 'fallback_secret'

// POST /api/register
router.post('/register', async (req, res) => {
    const { name, email, password } = req.body

    if (!name || !email || !password) {
        return res.status(400).json({ message: 'Name, email and password are required' })
    }

    try {
        // Check if user already exists
        const { data: existingUser } = await supabaseAdmin
            .from('users')
            .select('id')
            .eq('email', email)
            .single()

        if (existingUser) {
            return res.status(400).json({ message: 'Email is already registered' })
        }

        // Hash password
        const hashedPassword = await bcrypt.hash(password, 10)

        // Insert into custom users table
        const { data, error } = await supabaseAdmin
            .from('users')
            .insert([
                { 
                    full_name: name, 
                    email: email, 
                    password: hashedPassword,
                    role: 'USER'
                }
            ])
            .select()
            .single()

        if (error) {
            return res.status(400).json({ message: error.message })
        }

        return res.status(200).json({
            message: 'Registration successful!',
            user: {
                id: data.id,
                email: data.email,
                name: data.full_name
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
        // Find user by email
        const { data: user, error } = await supabaseAdmin
            .from('users')
            .select('*')
            .eq('email', email)
            .single()

        if (error || !user) {
            return res.status(401).json({ message: 'Invalid email or password' })
        }

        // Check password
        // Note: supporting both plain text (for existing data) and bcrypt
        let isMatch = false
        if (user.password.startsWith('$2a$') || user.password.startsWith('$2b$')) {
            isMatch = await bcrypt.compare(password, user.password)
        } else {
            // Plain text check for legacy data seen in your screenshot
            isMatch = (password === user.password)
            // Tip: We should hash this plain text password now that we found it
            if (isMatch) {
                const newHash = await bcrypt.hash(password, 10)
                await supabaseAdmin.from('users').update({ password: newHash }).eq('id', user.id)
            }
        }

        if (!isMatch) {
            return res.status(401).json({ message: 'Invalid email or password' })
        }

        // Generate custom JWT
        const token = jwt.sign(
            { id: user.id, email: user.email, name: user.full_name },
            JWT_SECRET,
            { expiresIn: '7d' }
        )

        return res.status(200).json({
            message: 'Login successful',
            token: token,
            user: {
                id: user.id,
                email: user.email,
                name: user.full_name
            }
        })
    } catch (err) {
        console.error('Login error:', err.message)
        return res.status(500).json({ message: 'Server error during login' })
    }
})

module.exports = router
