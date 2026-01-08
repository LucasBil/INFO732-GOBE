import React, { useState, useEffect } from 'react';
import { motion } from 'framer-motion';
import { useNavigate } from 'react-router-dom';
import Navbar from '../components/Navbar';
import { useAuth } from '../context/AuthContext';

import { api } from '../services/api';

const PostAd = () => {
    const navigate = useNavigate();
    const { user } = useAuth();
    const [universities, setUniversities] = useState([]);
    const [formData, setFormData] = useState({
        title: '',
        description: '',
        price: '',
        universityId: '',
        type: 'GOOD',
        goodState: 'B'
    });

    const [availableDates, setAvailableDates] = useState([]);
    const [newDate, setNewDate] = useState('');
    const [isRecurring, setIsRecurring] = useState(false);
    const [recurrence, setRecurrence] = useState({
        startDate: '',
        endDate: '',
        time: '12:00',
        frequency: 'DAILY'
    });

    useEffect(() => {
        api.getUniversities().then(setUniversities).catch(console.error);
    }, []);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData(prev => ({ ...prev, [name]: value }));
    };

    const handleAddDate = () => {
        if (!newDate) return;
        setAvailableDates(prev => [...prev, newDate]);
        setNewDate('');
    };

    const generateRecurringDates = () => {
        if (!recurrence.startDate || !recurrence.endDate || !recurrence.time) {
            alert("Please fill all recurrence fields");
            return;
        }

        const start = new Date(recurrence.startDate);
        const end = new Date(recurrence.endDate);

        if (end < start) {
            alert("End date cannot be before start date");
            return;
        }

        const [hours, minutes] = recurrence.time.split(':');

        let current = new Date(start);
        current.setHours(parseInt(hours), parseInt(minutes), 0, 0);

        // Adjust end date to include time
        const endDateTime = new Date(end);
        endDateTime.setHours(23, 59, 59, 999);

        const newDates = [];
        while (current <= endDateTime) {
            newDates.push(current.toISOString()); // Use ISO string for consistency

            // Increment based on frequency
            if (recurrence.frequency === 'DAILY') {
                current.setDate(current.getDate() + 1);
            } else if (recurrence.frequency === 'WEEKLY') {
                current.setDate(current.getDate() + 7);
            }
            // Reset time to ensure no drift (setDate handles month rollover)
            current.setHours(parseInt(hours), parseInt(minutes), 0, 0);
        }

        setAvailableDates(prev => [...prev, ...newDates]);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (!user) {
            alert('You must be logged in to post an ad.');
            navigate('/login');
            return;
        }

        try {
            const payload = {
                ...formData,
                holderId: user.id,
                date: new Date().toISOString(),
                expire: new Date(Date.now() + 86400000 * 30).toISOString() // +30 days
            };

            const createdAd = await api.createAdvertisement(payload);

            // If Service and slots added, create them
            if (formData.type === 'SERVICE' && availableDates.length > 0) {
                const timestamps = availableDates.map(d => new Date(d).getTime().toString());
                await api.createTimeSlots(createdAd.id, timestamps);
            }

            navigate('/marketplace');
        } catch (error) {
            console.error('Failed to post ad:', error);
            alert('Failed to post ad. Please try again.');
        }
    };

    return (
        <div className="min-h-screen bg-slate-900 text-slate-100">
            <Navbar />
            <div className="max-w-2xl mx-auto pt-24 px-4 pb-12">
                <motion.div
                    initial={{ opacity: 0, y: 20 }}
                    animate={{ opacity: 1, y: 0 }}
                    className="glass-panel p-8 rounded-2xl border border-white/10"
                >
                    <h1 className="text-3xl font-bold bg-gradient-to-r from-teal-400 to-blue-500 bg-clip-text text-transparent mb-8">
                        Post an Advertisement
                    </h1>

                    <form onSubmit={handleSubmit} className="space-y-6">
                        {/* Title */}
                        <div>
                            <label className="block text-sm font-medium text-slate-400 mb-2">Title</label>
                            <input
                                type="text"
                                name="title"
                                required
                                value={formData.title}
                                onChange={handleChange}
                                className="w-full bg-black/20 border border-white/10 rounded-lg px-4 py-3 focus:ring-2 focus:ring-teal-500 focus:border-transparent transition-all outline-none"
                                placeholder="What are you offering?"
                            />
                        </div>

                        {/* Type */}
                        <div className="grid grid-cols-2 gap-4">
                            <button
                                type="button"
                                onClick={() => setFormData({ ...formData, type: 'GOOD' })}
                                className={`p-4 rounded-xl border transition-all ${formData.type === 'GOOD'
                                    ? 'bg-teal-500/20 border-teal-500 text-teal-400'
                                    : 'bg-black/20 border-white/10 text-slate-400 hover:bg-white/5'
                                    }`}
                            >
                                Selling a Good
                            </button>
                            <button
                                type="button"
                                onClick={() => setFormData({ ...formData, type: 'SERVICE' })}
                                className={`p-4 rounded-xl border transition-all ${formData.type === 'SERVICE'
                                    ? 'bg-blue-500/20 border-blue-500 text-blue-400'
                                    : 'bg-black/20 border-white/10 text-slate-400 hover:bg-white/5'
                                    }`}
                            >
                                Offering a Service
                            </button>
                        </div>

                        {/* Description */}
                        <div>
                            <label className="block text-sm font-medium text-slate-400 mb-2">Description</label>
                            <textarea
                                name="description"
                                required
                                rows="4"
                                value={formData.description}
                                onChange={handleChange}
                                className="w-full bg-black/20 border border-white/10 rounded-lg px-4 py-3 focus:ring-2 focus:ring-teal-500 focus:border-transparent transition-all outline-none resize-none"
                                placeholder="Describe your item or service..."
                            />
                        </div>

                        <div className="grid grid-cols-2 gap-6">
                            {/* Price */}
                            <div>
                                <label className="block text-sm font-medium text-slate-400 mb-2">Price (€)</label>
                                <input
                                    type="number"
                                    name="price"
                                    required
                                    min="0"
                                    step="0.5"
                                    value={formData.price}
                                    onChange={handleChange}
                                    className="w-full bg-black/20 border border-white/10 rounded-lg px-4 py-3 focus:ring-2 focus:ring-teal-500 focus:border-transparent transition-all outline-none"
                                    placeholder="0.00"
                                />
                            </div>

                            {/* University */}
                            <div>
                                <label className="block text-sm font-medium text-slate-400 mb-2">Location</label>
                                <select
                                    name="universityId"
                                    required
                                    value={formData.universityId}
                                    onChange={handleChange}
                                    className="w-full bg-black/20 border border-white/10 rounded-lg px-4 py-3 focus:ring-2 focus:ring-teal-500 focus:border-transparent transition-all outline-none text-slate-100"
                                >
                                    <option value="">Select University</option>
                                    {universities.map(uni => (
                                        <option key={uni.id} value={uni.id} className="bg-slate-900">
                                            {uni.name} ({uni.city})
                                        </option>
                                    ))}
                                </select>
                            </div>
                        </div>

                        {/* Good State (Only if Good) */}
                        {formData.type === 'GOOD' && (
                            <div>
                                <label className="block text-sm font-medium text-slate-400 mb-2">Condition</label>
                                <select
                                    name="goodState"
                                    value={formData.goodState}
                                    onChange={handleChange}
                                    className="w-full bg-black/20 border border-white/10 rounded-lg px-4 py-3 focus:ring-2 focus:ring-teal-500 focus:border-transparent transition-all outline-none text-slate-100"
                                >
                                    <option value="A" className="bg-slate-900">New / Mint Condition</option>
                                    <option value="B" className="bg-slate-900">Good Condition</option>
                                    <option value="C" className="bg-slate-900">Used / Fair Condition</option>
                                </select>
                            </div>
                        )}

                        {/* Service Availability (Only if Service) */}
                        {formData.type === 'SERVICE' && (
                            <div className="space-y-4 bg-white/5 p-6 rounded-xl border border-white/5">
                                <h3 className="text-lg font-bold text-white mb-4">Availability</h3>

                                <div className="flex gap-4 mb-4">
                                    <button
                                        type="button"
                                        onClick={() => setIsRecurring(false)}
                                        className={`flex-1 py-2 rounded-lg transition-colors ${!isRecurring ? 'bg-teal-500 text-white' : 'bg-black/20 text-slate-400'}`}
                                    >
                                        Manual Entry
                                    </button>
                                    <button
                                        type="button"
                                        onClick={() => setIsRecurring(true)}
                                        className={`flex-1 py-2 rounded-lg transition-colors ${isRecurring ? 'bg-teal-500 text-white' : 'bg-black/20 text-slate-400'}`}
                                    >
                                        Recurring Slots
                                    </button>
                                </div>

                                {!isRecurring ? (
                                    <div className="flex gap-2">
                                        <input
                                            type="datetime-local"
                                            value={newDate}
                                            onChange={(e) => setNewDate(e.target.value)}
                                            className="flex-1 bg-black/20 border border-white/10 rounded-lg px-4 py-2 focus:ring-2 focus:ring-teal-500 transition-all outline-none"
                                        />
                                        <button
                                            type="button"
                                            onClick={handleAddDate}
                                            className="px-4 py-2 bg-teal-500/20 text-teal-400 rounded-lg hover:bg-teal-500/30 transition-colors"
                                        >
                                            Add
                                        </button>
                                    </div>
                                ) : (
                                    <div className="space-y-4">
                                        <div className="grid grid-cols-2 gap-4">
                                            <div>
                                                <label className="block text-xs text-slate-400 mb-1">Start Date</label>
                                                <input
                                                    type="date"
                                                    value={recurrence.startDate}
                                                    onChange={(e) => setRecurrence(prev => ({ ...prev, startDate: e.target.value }))}
                                                    className="w-full bg-black/20 border border-white/10 rounded-lg px-3 py-2 outline-none focus:border-teal-500"
                                                />
                                            </div>
                                            <div>
                                                <label className="block text-xs text-slate-400 mb-1">End Date</label>
                                                <input
                                                    type="date"
                                                    value={recurrence.endDate}
                                                    min={recurrence.startDate}
                                                    onChange={(e) => setRecurrence(prev => ({ ...prev, endDate: e.target.value }))}
                                                    className="w-full bg-black/20 border border-white/10 rounded-lg px-3 py-2 outline-none focus:border-teal-500"
                                                />
                                            </div>
                                        </div>
                                        <div className="grid grid-cols-2 gap-4">
                                            <div>
                                                <label className="block text-xs text-slate-400 mb-1">Time</label>
                                                <input
                                                    type="time"
                                                    value={recurrence.time}
                                                    onChange={(e) => setRecurrence(prev => ({ ...prev, time: e.target.value }))}
                                                    className="w-full bg-black/20 border border-white/10 rounded-lg px-3 py-2 outline-none focus:border-teal-500"
                                                />
                                            </div>
                                            <div>
                                                <label className="block text-xs text-slate-400 mb-1">Frequency</label>
                                                <select
                                                    value={recurrence.frequency}
                                                    onChange={(e) => setRecurrence(prev => ({ ...prev, frequency: e.target.value }))}
                                                    className="w-full bg-black/20 border border-white/10 rounded-lg px-3 py-2 outline-none focus:border-teal-500 text-slate-100"
                                                >
                                                    <option value="DAILY" className="bg-slate-900">Daily</option>
                                                    <option value="WEEKLY" className="bg-slate-900">Weekly</option>
                                                    {/* <option value="MONTHLY" className="bg-slate-900">Monthly</option> */}
                                                </select>
                                            </div>
                                        </div>
                                        <button
                                            type="button"
                                            onClick={generateRecurringDates}
                                            className="w-full py-2 bg-teal-500/20 text-teal-400 rounded-lg hover:bg-teal-500/30 transition-colors border border-teal-500/50"
                                        >
                                            Generate Slots
                                        </button>
                                    </div>
                                )}

                                {availableDates.length > 0 && (
                                    <div className="bg-black/20 p-4 rounded-lg mt-4">
                                        <div className="flex justify-between items-center mb-2">
                                            <span className="text-sm text-slate-400">{availableDates.length} slots added</span>
                                            <button
                                                type="button"
                                                onClick={() => setAvailableDates([])}
                                                className="text-xs text-red-400 hover:text-red-300"
                                            >
                                                Clear All
                                            </button>
                                        </div>
                                        <div className="flex flex-wrap gap-2 max-h-40 overflow-y-auto">
                                            {availableDates.map((date, index) => (
                                                <div key={index} className="flex items-center gap-2 bg-slate-800 px-3 py-1 rounded-full text-xs border border-white/5">
                                                    <span>{new Date(date).toLocaleDateString()} {new Date(date).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })}</span>
                                                    <button
                                                        type="button"
                                                        onClick={() => setAvailableDates(prev => prev.filter((_, i) => i !== index))}
                                                        className="text-red-400 hover:text-red-300 ml-1"
                                                    >
                                                        ×
                                                    </button>
                                                </div>
                                            ))}
                                        </div>
                                    </div>
                                )}
                                <p className="text-xs text-slate-500 mt-2">
                                    Adding slots will make them available for booking. Each slot is 1 hour long.
                                </p>
                            </div>
                        )}

                        {/* Submit */}
                        <button
                            type="submit"
                            className="w-full bg-gradient-to-r from-teal-500 to-blue-600 hover:from-teal-400 hover:to-blue-500 text-white font-bold py-4 rounded-xl shadow-lg shadow-teal-500/20 transition-all transform hover:scale-[1.02]"
                        >
                            Post Advertisement
                        </button>
                    </form>
                </motion.div>
            </div>
        </div>
    );
};

export default PostAd;
