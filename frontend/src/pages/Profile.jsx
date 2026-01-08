import React, { useState, useEffect } from 'react';
import Navbar from '../components/Navbar';
import { api } from '../services/api';
import { useAuth } from '../context/AuthContext';
import { useNavigate } from 'react-router-dom';
import { motion } from 'framer-motion';
import { User, Package, Calendar, Clock, CheckCircle, XCircle } from 'lucide-react';

const Profile = () => {
    const { user, loading: authLoading } = useAuth();
    const navigate = useNavigate();
    const [activeTab, setActiveTab] = useState('ads');
    const [profile, setProfile] = useState(null);
    const [myAds, setMyAds] = useState([]);
    const [myBookings, setMyBookings] = useState([]);
    const [incomingReservations, setIncomingReservations] = useState([]);

    useEffect(() => {
        if (!authLoading && !user) {
            navigate('/login');
            return;
        }

        if (user) {
            // Load user data
            Promise.all([
                api.getProfile(user.id),
                api.getMyAds(user.id),
                api.getMyTimeSlots(user.id),
                api.getIncomingReservations(user.id)
            ]).then(([profileData, adsData, bookingsData, reservationsData]) => {
                setProfile(profileData);
                setMyAds(adsData);
                setMyBookings(bookingsData);
                setIncomingReservations(reservationsData);
            }).catch(console.error);
        }
    }, [user, authLoading, navigate]);

    const handleStatusUpdate = async (slotId, newStatus) => {
        try {
            await api.updateTimeSlotStatus(slotId, newStatus);
            // Refresh reservations
            const updated = await api.getIncomingReservations(user.id);
            setIncomingReservations(updated);
        } catch (err) {
            console.error("Failed to update status", err);
        }
    };

    const [newKeyword, setNewKeyword] = useState('');

    const handleAddKeyword = async () => {
        if (!newKeyword.trim()) return;
        try {
            const updatedProfile = await api.addKeyword(user.id, newKeyword);
            setProfile(updatedProfile);
            setNewKeyword('');
        } catch (err) {
            console.error("Failed to add keyword", err);
        }
    };

    const handleRemoveKeyword = async (keyword) => {
        try {
            const updatedProfile = await api.removeKeyword(user.id, keyword);
            setProfile(updatedProfile);
        } catch (err) {
            console.error("Failed to remove keyword", err);
        }
    };

    if (authLoading || !profile) return <div className="min-h-screen bg-slate-900 flex items-center justify-center text-white">Loading...</div>;

    return (
        <div className="min-h-screen bg-slate-900 text-slate-100 font-sans">
            <Navbar />
            <div className="max-w-6xl mx-auto pt-24 px-4 pb-12">

                {/* Header */}
                <div className="glass-panel p-8 rounded-2xl border border-white/10 mb-8 flex items-center gap-6">
                    <div className="w-20 h-20 rounded-full bg-gradient-to-tr from-teal-500 to-blue-600 flex items-center justify-center shadow-lg">
                        <User size={40} className="text-white" />
                    </div>
                    <div>
                        <h1 className="text-3xl font-bold text-white">{profile.firstName} {profile.lastName}</h1>
                        <p className="text-slate-400">{profile.email} • {profile.university?.name}</p>
                    </div>
                </div>

                {/* Tabs */}
                <div className="flex gap-4 mb-8 border-b border-white/10 pb-1">
                    {['ads', 'bookings', 'reservations', 'interests'].map(tab => (
                        <button
                            key={tab}
                            onClick={() => setActiveTab(tab)}
                            className={`pb-3 px-4 text-sm font-semibold transition-all relative ${activeTab === tab ? 'text-teal-400' : 'text-slate-400 hover:text-white'
                                }`}
                        >
                            {tab === 'ads' && 'My Ads'}
                            {tab === 'bookings' && 'My Bookings'}
                            {tab === 'reservations' && 'Incoming Requests'}
                            {tab === 'interests' && 'My Interests'}
                            {activeTab === tab && (
                                <motion.div layoutId="activeTab" className="absolute bottom-0 left-0 right-0 h-0.5 bg-teal-400" />
                            )}
                        </button>
                    ))}
                </div>

                {/* Content */}
                <div className="space-y-4">
                    {activeTab === 'ads' && (
                        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                            {myAds.map(ad => (
                                <div key={ad.id} className="bg-white/5 border border-white/10 rounded-xl p-6">
                                    <h3 className="font-bold text-lg mb-2">{ad.title}</h3>
                                    <p className="text-slate-400 text-sm mb-4 line-clamp-2">{ad.description}</p>
                                    <span className="text-teal-400 font-bold">{ad.price}€</span>
                                </div>
                            ))}
                            {myAds.length === 0 && <p className="text-slate-500">No ads posted yet.</p>}
                        </div>
                    )}

                    {activeTab === 'bookings' && (
                        <div className="space-y-4">
                            {myBookings.map(slot => (
                                <div key={slot.id} className="bg-white/5 border border-white/10 rounded-xl p-6 flex justify-between items-center">
                                    <div>
                                        <h3 className="font-bold text-lg mb-1">{slot.advertisement?.title}</h3>
                                        <p className="text-slate-400 text-sm flex items-center gap-2">
                                            <Calendar size={14} /> {new Date(slot.date).toLocaleDateString()}
                                            <Clock size={14} /> {new Date(slot.date).toLocaleTimeString()}
                                        </p>
                                    </div>
                                    <div className={`px-4 py-2 rounded-full text-xs font-bold ${slot.status === 'CONFIRMED' ? 'bg-green-500/20 text-green-400' :
                                        slot.status === 'REJECTED' ? 'bg-red-500/20 text-red-400' :
                                            'bg-yellow-500/20 text-yellow-400'
                                        }`}>
                                        {slot.status}
                                        {slot.amount && (
                                            <div className="mt-1 text-center">
                                                <span className="block text-[10px] text-white/50">{slot.amount}€</span>
                                                {slot.advertisement && slot.amount !== slot.advertisement.price && (
                                                    <span className="block text-[9px] text-rose-400/70 line-through">
                                                        {slot.advertisement.price}€
                                                    </span>
                                                )}
                                            </div>
                                        )}
                                    </div>
                                </div>
                            ))}
                            {myBookings.length === 0 && <p className="text-slate-500">No bookings made yet.</p>}
                        </div>
                    )}

                    {activeTab === 'reservations' && (
                        <div className="space-y-4">
                            {incomingReservations.filter(slot => slot.status !== 'AVAILABLE').map(slot => (
                                <div key={slot.id} className="bg-white/5 border border-white/10 rounded-xl p-6 flex justify-between items-center">
                                    <div>
                                        <h3 className="font-bold text-lg mb-1">Request for: {slot.advertisement?.title}</h3>
                                        <p className="text-slate-400 text-sm mb-2">
                                            From: <span className="font-bold text-white">{slot.profile?.firstName} {slot.profile?.lastName}</span>
                                        </p>
                                        <p className="text-slate-500 text-xs flex items-center gap-2">
                                            <Calendar size={14} /> {new Date(slot.date).toLocaleDateString()}
                                            <Clock size={14} /> {new Date(slot.date).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })}
                                        </p>
                                        <p className="text-teal-400 font-bold text-sm mt-1">
                                            Proposed Price: {slot.amount}€
                                            {slot.advertisement && slot.amount !== slot.advertisement.price && (
                                                <span className="text-rose-400 text-xs ml-2 font-normal">
                                                    (Asked: <span className="line-through">{slot.advertisement.price}€</span>)
                                                </span>
                                            )}
                                        </p>
                                    </div>

                                    {['PENDING', 'pending'].includes(slot.status) ? (
                                        <div className="flex gap-2">
                                            <button
                                                onClick={() => handleStatusUpdate(slot.id, 'CONFIRMED')}
                                                className="p-2 rounded-full bg-green-500/20 text-green-400 hover:bg-green-500/30 transition-colors"
                                            >
                                                <CheckCircle size={20} />
                                            </button>
                                            <button
                                                onClick={() => handleStatusUpdate(slot.id, 'REJECTED')}
                                                className="p-2 rounded-full bg-red-500/20 text-red-400 hover:bg-red-500/30 transition-colors"
                                            >
                                                <XCircle size={20} />
                                            </button>
                                        </div>
                                    ) : (
                                        <div className={`px-4 py-2 rounded-full text-xs font-bold ${slot.status === 'CONFIRMED' ? 'bg-green-500/20 text-green-400' : 'bg-red-500/20 text-red-400'
                                            }`}>
                                            {slot.status}
                                        </div>
                                    )}
                                </div>
                            ))}
                            {incomingReservations.length === 0 && <p className="text-slate-500">No incoming reservation requests.</p>}
                        </div>
                    )}
                    {activeTab === 'interests' && (
                        <div className="bg-white/5 border border-white/10 rounded-xl p-8">
                            <h3 className="text-xl font-bold mb-4 flex items-center gap-2">
                                <Package size={20} className="text-teal-400" /> Followed Topics
                            </h3>
                            <p className="text-slate-400 text-sm mb-6">
                                Add keywords to get notified when new matching ads are posted (e.g., "bike", "maths", "cleaning").
                            </p>

                            <div className="flex gap-2 mb-6">
                                <input
                                    type="text"
                                    value={newKeyword}
                                    onChange={(e) => setNewKeyword(e.target.value)}
                                    placeholder="Enter a topic..."
                                    className="flex-1 bg-black/20 border border-white/10 rounded-lg px-4 py-2 text-white focus:outline-none focus:border-teal-500"
                                    onKeyDown={(e) => e.key === 'Enter' && handleAddKeyword()}
                                />
                                <button
                                    onClick={handleAddKeyword}
                                    className="bg-teal-500 hover:bg-teal-400 text-white px-6 py-2 rounded-lg font-bold transition-colors"
                                >
                                    Add
                                </button>
                            </div>

                            <div className="flex flex-wrap gap-2">
                                {profile.followedKeywords && profile.followedKeywords.map((kw, index) => (
                                    <span key={index} className="flex items-center gap-2 bg-slate-800 text-slate-200 px-3 py-1 rounded-full text-sm border border-white/10">
                                        {kw}
                                        <button
                                            onClick={() => handleRemoveKeyword(kw)}
                                            className="text-slate-500 hover:text-red-400 transition-colors"
                                        >
                                            <XCircle size={14} />
                                        </button>
                                    </span>
                                ))}
                                {(!profile.followedKeywords || profile.followedKeywords.length === 0) && (
                                    <p className="text-slate-500 italic">No topics followed yet.</p>
                                )}
                            </div>
                        </div>
                    )}
                </div>
            </div>
        </div>
    );
};

export default Profile;
