const { supabase } = require('./supabaseClient')

async function testConnection() {
    console.log('Testing Supabase connection...')
    try {
        const { data, error } = await supabase.from('profiles').select('count', { count: 'exact', head: true })
        
        if (error) {
            console.error('Connection failed:', error.message)
            if (error.message.includes('FetchError') || error.message.includes('failed to fetch')) {
                console.log('TIP: Check your SUPABASE_URL and internet connection.')
            } else if (error.message.includes('relation "profiles" does not exist')) {
                console.log('TIP: The "profiles" table does not exist in your Supabase database.')
            } else if (error.code === 'PGRST301') {
                console.log('TIP: Invalid API key or URL.')
            }
        } else {
            console.log('Connection successful! Database is reachable.')
        }
    } catch (err) {
        console.error('Unexpected error:', err.message)
    }
}

testConnection()
