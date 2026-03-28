<img width="1080" height="2220" alt="Screenshot_20260328_100446" src="https://github.com/user-attachments/assets/fe8bf9fb-15aa-4cc2-8780-410f1c5d897b" /># PetShop App

An Android app for a pet shop that lets users register, login, view their profile, and more.

---

## What the App Can Do

- Register a new account
- Login with email and password
- View the Dashboard
- View your Profile
- Edit your Profile
- Change your Password

---

## Tech Used

- **Android** (Kotlin)
- **Retrofit** — for API calls
- **Supabase** — database and user authentication
- **Node.js + Express** — backend server

---

## API Endpoints

Base URL: `http://10.0.2.2:3000/api` (local) or your Render URL (deployed)

| # | Method | Endpoint | Login Required | What it does |
|---|--------|----------|----------------|--------------|
| 1 | POST | `/register` | No | Create a new account |
| 2 | POST | `/login` | No | Login and get a token |
| 3 | GET | `/dashboard` | Yes | Get welcome message |
| 4 | GET | `/profile` | Yes | Get your profile info |
| 5 | PUT | `/profile` | Yes | Update name and phone |
| 6 | POST | `/change-password` | Yes | Change your password |

> "Login Required" means you need to send `Authorization: Bearer YOUR_TOKEN` in the request header.

---

### Register — POST /register

**Send this:**
```json
{
  "name": "Juan Dela Cruz",
  "email": "juan@email.com",
  "password": "password123"
}
```

**You get back:**
```json
{
  "message": "Registration successful! Please login.",
  "user": {
    "id": "abc123",
    "email": "juan@email.com",
    "name": "Juan Dela Cruz"
  }
}
```

---

### Login — POST /login

**Send this:**
```json
{
  "email": "juan@email.com",
  "password": "password123"
}
```

**You get back:**
```json
{
  "message": "Login successful",
  "token": "eyJhbGci...",
  "user": {
    "id": "abc123",
    "email": "juan@email.com",
    "name": "Juan Dela Cruz"
  }
}
```

---

### Get Profile — GET /profile

No body needed. Just send the token in the header.

**You get back:**
```json
{
  "message": "Profile retrieved successfully",
  "profile": {
    "name": "Juan Dela Cruz",
    "email": "juan@email.com",
    "phone": "09123456789"
  }
}
```

---

### Update Profile — PUT /profile

**Send this:**
```json
{
  "name": "Juan Smith",
  "phone": "09987654321"
}
```

**You get back:**
```json
{
  "message": "Profile updated successfully",
  "profile": { ... }
}
```

---

### Change Password — POST /change-password

**Send this:**
```json
{
  "current_password": "oldpassword",
  "new_password": "newpassword123"
}
```

**You get back:**
```json
{
  "message": "Password changed successfully"
}
```

---

## Error Codes

| Code | Meaning |
|------|---------|
| 200 | Success |
| 400 | Bad request (missing or wrong data) |
| 401 | Wrong credentials or token expired |
| 500 | Server error |

---

## How to Run Locally

### 1. Set up the database (Supabase)

1. Go to [supabase.com](https://supabase.com) and create a free account
2. Create a new project called `petshop`
3. Go to **SQL Editor** and paste + run this:

```sql
create table public.profiles (
  id uuid references auth.users(id) on delete cascade primary key,
  name text,
  email text,
  phone text,
  avatar_url text,
  created_at timestamp with time zone default timezone('utc', now())
);

create or replace function public.handle_new_user()
returns trigger as $$
begin
  insert into public.profiles (id, name, email)
  values (new.id, new.raw_user_meta_data->>'name', new.email);
  return new;
end;
$$ language plpgsql security definer;

create trigger on_auth_user_created
  after insert on auth.users
  for each row execute function public.handle_new_user();

alter table public.profiles enable row level security;
create policy "Users can view own profile" on public.profiles for select using (auth.uid() = id);
create policy "Users can update own profile" on public.profiles for update using (auth.uid() = id);
```

---

### 2. Start the backend server

Open a terminal inside the `petshop-backend` folder and run:

```
node index.js
```

You should see: `PetShop API running on port 3000`

Keep this terminal open while testing.

---

### 3. Run the Android app

1. Open the project in Android Studio
2. Make sure the emulator is running
3. Click **Run**

---

Register Screenshot

<img width="1080" height="2220" alt="Screenshot_20260328_100110" src="https://github.com/user-attachments/assets/66b87a76-23eb-4b9d-9f83-f836cfd4a91e" />

Login Screenshot

<img width="1080" height="2220" alt="Screenshot_20260328_100151" src="https://github.com/user-attachments/assets/b4cfe86b-9d55-48ad-980c-e237bbdd9c7e" />

Dashboard screenshot
<img width="1080" height="2220" alt="Screenshot_20260328_100446" src="https://github.com/user-attachments/assets/e66aafce-bf06-4311-937b-d02de5d17ec9" />


Profile Screenshot
<img width="1080" height="2220" alt="Screenshot_20260328_100454" src="https://github.com/user-attachments/assets/20768d7a-8209-4ba0-943a-d7733a8f22cb" />

Change Password Screenshot
<img width="1080" height="2220" alt="Screenshot_20260328_100509" src="https://github.com/user-attachments/assets/165ada2c-b7d2-4717-ad9e-0b523ee4c663" />





