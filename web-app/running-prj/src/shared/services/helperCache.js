const ONE_DAY = 24 * 60 * 60 * 1000;
const ONE_WEEK = ONE_DAY * 7;

export const getCache = (key, timeExpire = ONE_DAY) => {
    try {
        const raw = localStorage.getItem(key);
        if (!raw) return null;

        const {
            data,
            timestamp
        } = JSON.parse(raw);

        if (Date.now() - timestamp > timeExpire) {
            localStorage.removeItem(key);
            return null;
        }

        return data;
    } catch (e) {
        localStorage.removeItem(key);
        return null;
    }
};

export const setCache = (key, data) => {
    localStorage.setItem(
        key,
        JSON.stringify({
            data,
            timestamp: Date.now()
        })
    );
};