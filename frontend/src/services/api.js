const API_URL = 'http://localhost:8080/api';

// Basic API fetch wrapper
const request = async (endpoint, options = {}) => {
    const response = await fetch(`${API_URL}${endpoint}`, {
        headers: { 'Content-Type': 'application/json' },
        ...options,
    });
    if (!response.ok) {
        const errorText = await response.text();
        throw new Error(errorText || 'Network response was not ok');
    }
    return response.json();
};

export const api = {
    getAdvertisements: () => request('/advertisements'),
    getUniversities: () => request('/universities'),

    createAdvertisement: (data) => request('/advertisements', {
        method: 'POST',
        body: JSON.stringify(data),
    }),

    getAdvertisementById: (id) => request(`/advertisements/${id}`),

    requestTimeSlot: (data) => request('/timeslots', {
        method: 'POST',
        body: JSON.stringify(data),
    }),

    getSlotsByAd: (adId) => request(`/timeslots/advertisement/${adId}`),

    getProfile: (id) => request(`/profiles/${id}`),

    getMyAds: (id) => request(`/profiles/${id}/advertisements`),

    getMyTimeSlots: (id) => request(`/profiles/${id}/timeslots`),

    getIncomingReservations: (id) => request(`/profiles/${id}/reservations`),

    updateTimeSlotStatus: (id, status) => request(`/timeslots/${id}/status`, {
        method: 'PUT',
        body: JSON.stringify({ status })
    }),

    createTimeSlots: (advertisementId, dates) => request('/timeslots/batch-create', {
        method: 'POST',
        body: JSON.stringify({ advertisementId, dates })
    }),

    bookTimeSlots: (profileId, advertisementId, slotIds) => request('/timeslots/batch-book', {
        method: 'POST',
        body: JSON.stringify({ profileId, advertisementId, slotIds })
    }),

    // Auth
    createProfile: (data) => request('/profiles', {
        method: 'POST',
        body: JSON.stringify(data),
    }),

    login: (email) => request('/profiles/login', {
        method: 'POST',
        body: JSON.stringify({ email }),
    }),

    // Messages
    sendMessage: (data) => request('/messages', {
        method: 'POST',
        body: JSON.stringify(data),
    }),

    getConversation: (myId, otherId) => request(`/messages/${myId}/${otherId}`),

    getConversations: (myId) => request(`/messages/${myId}/conversations`),

    // Notifications
    getNotifications: (userId) => request(`/notifications/${userId}`),
    markNotificationRead: (id) => request(`/notifications/${id}/read`, { method: 'PUT' }),

    // Keywords
    addKeyword: (profileId, keyword) => request(`/profiles/${profileId}/keywords`, {
        method: 'POST',
        body: JSON.stringify({ keyword })
    }),
    removeKeyword: (profileId, keyword) => request(`/profiles/${profileId}/keywords?keyword=${keyword}`, {
        method: 'DELETE'
    })
};
