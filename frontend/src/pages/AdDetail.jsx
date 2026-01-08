import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { motion, AnimatePresence } from 'framer-motion';
import { Calendar, User, MapPin, Tag, ArrowLeft, Clock } from 'lucide-react';
import Navbar from '../components/Navbar';
import { api } from '../services/api';
import { useAuth } from '../context/AuthContext';

const AdDetail = () => {
    const { id } = useParams();
    const navigate = useNavigate();
    const { user } = useAuth();
    const [ad, setAd] = useState(null);
    const [loading, setLoading] = useState(true);
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [bookingDate, setBookingDate] = useState('');
    const [bookingAmount, setBookingAmount] = useState('');
    const [availableSlots, setAvailableSlots] = useState([]);
    const [selectedSlotIds, setSelectedSlotIds] = useState([]);

    useEffect(() => {
        api.getAdvertisementById(id)
            .then(data => {
                setAd(data);
                setBookingAmount(data.price);
            })
            .catch(console.error)
            .finally(() => setLoading(false));
    }, [id]);

    // Fetch slots when modal opens for Service
    useEffect(() => {
        if (isModalOpen && ad?.type === 'SERVICE') {
            api.getSlotsByAd(id).then(slots => {
                setAvailableSlots(slots.filter(s => s.status === 'AVAILABLE'));
            }).catch(console.error);
        }
    }, [isModalOpen, ad, id]);

    const toggleSlotSelection = (slotId) => {
        if (!slotId) return;
        setSelectedSlotIds(prev =>
            prev.includes(slotId) ? prev.filter(id => id !== slotId) : [...prev, slotId]
        );
    };

    const handleBooking = async (e) => {
        e.preventDefault();
        if (!user) {
            alert('You must be logged in to book.');
            navigate('/login');
            return;
        }

        try {
            if (ad.type === 'SERVICE') {
                if (selectedSlotIds.length === 0) {
                    alert('Please select at least one slot.');
                    return;
                }
                await api.bookTimeSlots(user.id, parseInt(id), selectedSlotIds);
            } else {
                await api.requestTimeSlot({
                    profileId: user.id,
                    advertisementId: parseInt(id),
                    date: bookingDate,
                    amount: bookingAmount
                });
            }
            alert('Booking request sent successfully!');
            setIsModalOpen(false);
            setSelectedSlotIds([]); // Reset selection
        } catch (error) {
            console.error('Booking failed:', error);
            alert('Booking failed. Please try again.');
        }
    };

    if (loading) return <div className="min-h-screen bg-slate-900 flex items-center justify-center text-white">Loading...</div>;
    if (!ad) return <div className="min-h-screen bg-slate-900 flex items-center justify-center text-white">Ad not found</div>;

    return (
        <div className="min-h-screen bg-slate-900 text-slate-100">
            <Navbar />
            <div className="max-w-4xl mx-auto pt-24 px-4 pb-12">
                <button
                    onClick={() => navigate(-1)}
                    className="flex items-center gap-2 text-slate-400 hover:text-white mb-6 transition-colors"
                >
                    <ArrowLeft size={20} /> Back to Marketplace
                </button>

                <motion.div
                    initial={{ opacity: 0, scale: 0.95 }}
                    animate={{ opacity: 1, scale: 1 }}
                    className="glass-panel p-8 rounded-2xl border border-white/10"
                >
                    <div className="flex justify-between items-start mb-6">
                        <h1 className="text-3xl font-bold text-white">{ad.title}</h1>
                        <span className="text-2xl font-bold text-teal-400">{ad.price}€</span>
                    </div>

                    <div className="flex flex-wrap gap-4 mb-8 text-sm text-slate-300">
                        <span className="flex items-center gap-1 bg-white/5 py-1 px-3 rounded-full">
                            <User size={16} className="text-blue-400" /> Posted by: {ad.holder?.firstName} {ad.holder?.lastName}
                        </span>
                        <span className="flex items-center gap-1 bg-white/5 py-1 px-3 rounded-full">
                            <MapPin size={16} className="text-red-400" /> {ad.place?.name}
                        </span>
                        <span className="flex items-center gap-1 bg-white/5 py-1 px-3 rounded-full">
                            <Tag size={16} className="text-yellow-400" /> {ad.type || 'Offer'} <span className="text-[10px] text-slate-500 ml-1">(Debug: {ad.type})</span>
                        </span>
                        {ad.goodState && (
                            <span className="flex items-center gap-1 bg-white/5 py-1 px-3 rounded-full">
                                State: {ad.goodState}
                            </span>
                        )}
                    </div>

                    <p className="text-lg text-slate-300 leading-relaxed mb-8 border-b border-white/10 pb-8">
                        {ad.description}
                    </p>

                    <div className="flex gap-4">
                        <button
                            onClick={async () => {
                                if (!user) {
                                    alert('Please login to message');
                                    navigate('/login');
                                } else {
                                    // Start conversation (naive check: just send greeting or navigate to messages)
                                    const content = `Hi! I'm interested in your ad: ${ad.title}`;
                                    try {
                                        await api.sendMessage({
                                            senderId: user.id,
                                            receiverId: ad.holder.id,
                                            content: content
                                        });
                                        navigate('/messages');
                                    } catch (err) {
                                        alert('Failed to start chat');
                                    }
                                }
                            }}
                            className="flex-1 bg-slate-700 hover:bg-slate-600 text-white font-bold py-3 px-8 rounded-xl shadow-lg transition-all"
                        >
                            Contact Seller
                        </button>
                        <button
                            onClick={() => {
                                if (!user) {
                                    alert('Please login to book');
                                    navigate('/login');
                                } else {
                                    setIsModalOpen(true);
                                }
                            }}
                            className="flex-1 bg-gradient-to-r from-teal-500 to-blue-600 hover:from-teal-400 hover:to-blue-500 text-white font-bold py-3 px-8 rounded-xl shadow-lg shadow-teal-500/20 transition-all transform hover:scale-105"
                        >
                            Book Now
                        </button>
                    </div>
                </motion.div>
            </div>

            {/* Booking Modal */}
            <AnimatePresence>
                {isModalOpen && (
                    <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/60 backdrop-blur-sm">
                        <motion.div
                            initial={{ opacity: 0, y: 50 }}
                            animate={{ opacity: 1, y: 0 }}
                            exit={{ opacity: 0, y: 50 }}
                            className="bg-slate-800 rounded-2xl p-6 w-full max-w-md border border-white/10 shadow-2xl"
                        >
                            <h2 className="text-2xl font-bold text-white mb-4">Book Time Slot</h2>
                            <form onSubmit={handleBooking} className="space-y-4">
                                {ad.type === 'SERVICE' ? (
                                    <>
                                        <div>
                                            <label className="block text-sm text-slate-400 mb-2">Select Available Slots</label>
                                            <div className="space-y-4 max-h-64 overflow-y-auto pr-2 custom-scrollbar">
                                                {availableSlots.length === 0 ? (
                                                    <p className="text-slate-500 text-sm">No available slots found.</p>
                                                ) : (
                                                    // Group slots by Date
                                                    Object.entries(availableSlots.reduce((acc, slot) => {
                                                        const dateStr = new Date(slot.date).toLocaleDateString(undefined, { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' });
                                                        if (!acc[dateStr]) acc[dateStr] = [];
                                                        acc[dateStr].push(slot);
                                                        return acc;
                                                    }, {})).map(([dateStr, slots]) => (
                                                        <div key={dateStr} className="bg-black/20 rounded-xl p-3 border border-white/5">
                                                            <h4 className="text-xs font-bold text-slate-400 mb-2 uppercase tracking-wider">{dateStr}</h4>
                                                            <div className="grid grid-cols-2 gap-2">
                                                                {slots.sort((a, b) => new Date(a.date) - new Date(b.date)).map(slot => {
                                                                    const startTime = new Date(slot.date);
                                                                    const endTime = new Date(startTime.getTime() + 60 * 60 * 1000); // Add 1 hour
                                                                    const timeStr = `${startTime.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })} - ${endTime.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })}`;

                                                                    return (
                                                                        <button
                                                                            key={slot.id}
                                                                            type="button"
                                                                            onClick={() => toggleSlotSelection(slot.id)}
                                                                            className={`px-3 py-2 rounded-lg text-xs font-medium transition-all border ${selectedSlotIds.includes(slot.id)
                                                                                ? 'bg-teal-500 text-white border-teal-500 shadow-lg shadow-teal-500/20'
                                                                                : 'bg-slate-800 text-slate-300 border-white/5 hover:bg-slate-700 hover:border-white/10'
                                                                                }`}
                                                                        >
                                                                            {timeStr}
                                                                        </button>
                                                                    );
                                                                })}
                                                            </div>
                                                        </div>
                                                    ))
                                                )}
                                            </div>
                                        </div>
                                        <div className="mt-4 p-4 bg-white/5 rounded-xl">
                                            <div className="flex justify-between text-sm text-slate-400 mb-1">
                                                <span>Slots Selected:</span>
                                                <span>{selectedSlotIds.length}</span>
                                            </div>
                                            <div className="flex justify-between font-bold text-white text-lg">
                                                <span>Total Price:</span>
                                                <span className="text-teal-400">{(selectedSlotIds.length * ad.price).toFixed(2)}€</span>
                                            </div>
                                        </div>
                                    </>
                                ) : (
                                    <div>
                                        <label className="block text-sm text-slate-400 mb-1">Select Date & Time</label>
                                        <input
                                            type="datetime-local"
                                            required
                                            value={bookingDate}
                                            onChange={(e) => setBookingDate(e.target.value)}
                                            className="w-full bg-black/20 border border-white/10 rounded-lg px-3 py-2 text-white focus:outline-none focus:border-teal-500"
                                        />
                                    </div>
                                )}

                                {ad.type !== 'SERVICE' && (
                                    <div>
                                        <label className="block text-sm text-slate-400 mb-1">Proposed Amount (€)</label>
                                        <input
                                            type="number"
                                            required
                                            value={bookingAmount}
                                            onChange={(e) => setBookingAmount(e.target.value)}
                                            className="w-full bg-black/20 border border-white/10 rounded-lg px-3 py-2 text-white focus:outline-none focus:border-teal-500"
                                        />
                                    </div>
                                )}

                                <div className="flex gap-3 mt-6">
                                    <button
                                        type="button"
                                        onClick={() => setIsModalOpen(false)}
                                        className="flex-1 py-2 rounded-lg bg-slate-700 hover:bg-slate-600 text-white transition-colors"
                                    >
                                        Cancel
                                    </button>
                                    <button
                                        type="submit"
                                        className="flex-1 py-2 rounded-lg bg-teal-500 hover:bg-teal-400 text-white font-bold transition-colors"
                                    >
                                        Confirm Booking
                                    </button>
                                </div>
                            </form>
                        </motion.div>
                    </div>
                )}
            </AnimatePresence>
        </div>
    );
};

export default AdDetail;
