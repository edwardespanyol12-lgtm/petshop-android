const { supabase } = require('./supabaseClient')

async function listTables() {
    console.log('Fetching database schema info...')
    // Note: Supabase doesn't have a direct "list tables" in the JS client easily without RPC or querying pg_catalog
    // But we can try to guess or use a system query if we have permissions
    try {
        const { data, error } = await supabase.rpc('get_tables') // Usually not there by default
        if (error) {
            console.log('RPC failed, trying raw query on users table...')
            const { data: usersData, error: usersError } = await supabase.from('users').select('*').limit(1)
            if (usersError) {
                console.error('Users table check failed:', usersError.message)
            } else {
                console.log('Confirmed: "users" table exists.')
                console.log('Sample data keys:', Object.keys(usersData[0] || {}))
            }
        } else {
            console.log('Tables:', data)
        }
    } catch (err) {
        console.error('Error:', err.message)
    }
}

listTables()
