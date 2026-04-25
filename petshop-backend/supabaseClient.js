const { createClient } = require('@supabase/supabase-js')
require('dotenv').config()

const supabaseUrl = process.env.SUPABASE_URL
const supabaseServiceKey = process.env.SUPABASE_SERVICE_KEY
const supabaseAnonKey = process.env.SUPABASE_ANON_KEY


const supabaseAdmin = createClient(supabaseUrl, supabaseServiceKey, {
    auth: {
        autoRefreshToken: false,
        persistSession: false
    }
})

const supabase = createClient(supabaseUrl, supabaseAnonKey)

module.exports = { supabase, supabaseAdmin }