const { createClient } = require('@supabase/supabase-js')
require('dotenv').config()

const supabaseUrl = process.env.SUPABASE_URL
const supabaseServiceKey = process.env.SUPABASE_SERVICE_KEY
const supabaseAnonKey = process.env.SUPABASE_ANON_KEY

// Admin client — uses service_role key, bypasses Row Level Security
const supabaseAdmin = createClient(supabaseUrl, supabaseServiceKey, {
    auth: {
        autoRefreshToken: false,
        persistSession: false
    }
})

// Public client — uses anon key, for auth sign-in operations
const supabase = createClient(supabaseUrl, supabaseAnonKey)

module.exports = { supabase, supabaseAdmin }
