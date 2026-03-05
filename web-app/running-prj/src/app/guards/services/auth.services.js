export const authService = {
    register: async (username, fullName, phoneNumber, password) => {
        try {
            const response = await fetch('/api/auth/register', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ username, fullName, phoneNumber, password })
            });
            if (!response.ok) throw new Error('Registration failed');
            return await response.json();
        } catch (error) {
            console.error('Registration error:', error);
            throw error;
        }
    }
};