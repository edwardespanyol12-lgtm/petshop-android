const express = require('express')
const router = express.Router()
const authMiddleware = require('../middleware/authMiddleware')
const { supabaseAdmin } = require('../supabaseClient')

// GET /api/dashboard
router.get('/dashboard', authMiddleware, async (req, res) => {
    try {
        const { data: profile } = await supabaseAdmin
            .from('profiles')
            .select('name')
            .eq('id', req.user.id)
            .single()

        return res.status(200).json({
            message: 'Dashboard data retrieved',
            data: {
                welcome_message: `Welcome back, ${profile?.name || 'Pet Lover'}!`,
                user_id: req.user.id,
                categories: [
                    { id: 1, name: 'Animals', icon: 'ic_animals' },
                    { id: 2, name: 'Clothing', icon: 'ic_clothing' },
                    { id: 3, name: 'Shoes', icon: 'ic_shoes' },
                    { id: 4, name: 'Swimming', icon: 'ic_swimming' },
                    { id: 5, name: 'Drink', icon: 'ic_drink' },
                    { id: 6, name: 'Snack', icon: 'ic_snack' },
                    { id: 7, name: 'Other', icon: 'ic_other' },
                    { id: 8, name: 'Health', icon: 'ic_health' }
                ]
            }
        })
    } catch (err) {
        console.error('Dashboard error:', err.message)
        return res.status(500).json({ message: 'Server error' })
    }
})

module.exports = router
