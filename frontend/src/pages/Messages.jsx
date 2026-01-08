import React, { useState, useEffect } from 'react';
import { useAuth } from '../context/AuthContext';
import { api } from '../services/api';
import Navbar from '../components/Navbar';
import { useNavigate } from 'react-router-dom';
import { User, Send, MessageCircle } from 'lucide-react';
import { motion } from 'framer-motion';

const Messages = () => {
    const { user, loading: authLoading } = useAuth();
    const navigate = useNavigate();
    const [partners, setPartners] = useState([]);
    const [selectedPartner, setSelectedPartner] = useState(null);
    const [messages, setMessages] = useState([]);
    const [newMessage, setNewMessage] = useState('');
    const [loading, setLoading] = useState(true);

    // Initial load
    useEffect(() => {
        if (!authLoading && !user) {
            navigate('/login');
            return;
        }
        if (user) {
            loadPartners();
        }
    }, [user, authLoading]);

    // Load messages when partner selected
    useEffect(() => {
        if (user && selectedPartner) {
            loadConversation(selectedPartner.id);
            // Poll for new messages every 3s
            const interval = setInterval(() => {
                loadConversation(selectedPartner.id);
            }, 3000);
            return () => clearInterval(interval);
        }
    }, [selectedPartner, user]);

    const loadPartners = async () => {
        try {
            const data = await api.getConversations(user.id);
            setPartners(data);
            setLoading(false);
        } catch (err) {
            console.error(err);
        }
    };

    const loadConversation = async (partnerId) => {
        try {
            const data = await api.getConversation(user.id, partnerId);
            setMessages(data);
        } catch (err) {
            console.error(err);
        }
    };

    const handleSend = async (e) => {
        e.preventDefault();
        if (!newMessage.trim() || !selectedPartner) return;

        try {
            await api.sendMessage({
                senderId: user.id,
                receiverId: selectedPartner.id,
                content: newMessage
            });
            setNewMessage('');
            loadConversation(selectedPartner.id); // Refresh
        } catch (err) {
            console.error('Failed to send', err);
        }
    };

    if (loading) return <div className="min-h-screen bg-slate-900 flex items-center justify-center text-white">Loading...</div>;

    return (
        <div className="min-h-screen bg-slate-900 text-slate-100 font-sans">
            <Navbar />
            <div className="max-w-6xl mx-auto pt-20 px-4 h-[calc(100vh-20px)] flex gap-6">

                {/* Sidebar: Conversation List */}
                <div className="w-1/3 glass-panel rounded-2xl border border-white/10 p-4 h-full overflow-y-auto">
                    <h2 className="text-xl font-bold text-white mb-4 flex items-center gap-2">
                        <MessageCircle className="text-teal-400" /> Messages
                    </h2>
                    <div className="space-y-2">
                        {partners.map(p => (
                            <div
                                key={p.id}
                                onClick={() => setSelectedPartner(p)}
                                className={`p-4 rounded-xl cursor-pointer transition-colors flex items-center gap-3 ${selectedPartner?.id === p.id
                                        ? 'bg-teal-500/20 border border-teal-500/50'
                                        : 'bg-white/5 hover:bg-white/10 border border-transparent'
                                    }`}
                            >
                                <div className="w-10 h-10 rounded-full bg-slate-700 flex items-center justify-center">
                                    <User size={20} className="text-slate-300" />
                                </div>
                                <div className="overflow-hidden">
                                    <h3 className="font-bold text-white truncate">{p.firstName} {p.lastName}</h3>
                                    <p className="text-xs text-slate-400 truncate">{p.university?.name}</p>
                                </div>
                            </div>
                        ))}
                        {partners.length === 0 && (
                            <p className="text-slate-500 text-center mt-10">No conversations yet.</p>
                        )}
                    </div>
                </div>

                {/* Chat Window */}
                <div className="w-2/3 glass-panel rounded-2xl border border-white/10 flex flex-col h-full overflow-hidden">
                    {selectedPartner ? (
                        <>
                            {/* Chat Header */}
                            <div className="p-4 border-b border-white/10 bg-black/20 flex items-center gap-3">
                                <div className="w-10 h-10 rounded-full bg-teal-600 flex items-center justify-center">
                                    <User size={20} className="text-white" />
                                </div>
                                <div>
                                    <h3 className="font-bold text-white">{selectedPartner.firstName} {selectedPartner.lastName}</h3>
                                </div>
                            </div>

                            {/* Chat Messages */}
                            <div className="flex-1 overflow-y-auto p-4 space-y-4">
                                {messages.map((msg, idx) => {
                                    const isMe = msg.sender?.id === user.id;
                                    return (
                                        <div key={idx} className={`flex ${isMe ? 'justify-end' : 'justify-start'}`}>
                                            <div className={`max-w-[70%] p-3 rounded-2xl text-sm ${isMe
                                                    ? 'bg-teal-600 text-white rounded-tr-none'
                                                    : 'bg-slate-700 text-white rounded-tl-none'
                                                }`}>
                                                <p>{msg.content}</p>
                                                <span className="text-[10px] opacity-50 block text-right mt-1">
                                                    {new Date(msg.timestamp).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })}
                                                </span>
                                            </div>
                                        </div>
                                    );
                                })}
                            </div>

                            {/* Input */}
                            <form onSubmit={handleSend} className="p-4 border-t border-white/10 bg-black/20 flex gap-2">
                                <input
                                    type="text"
                                    value={newMessage}
                                    onChange={(e) => setNewMessage(e.target.value)}
                                    placeholder="Type a message..."
                                    className="flex-1 bg-white/5 border border-white/10 rounded-xl px-4 py-2 text-white focus:outline-none focus:border-teal-500 transition-colors"
                                />
                                <button
                                    type="submit"
                                    className="bg-teal-500 hover:bg-teal-400 text-white p-3 rounded-xl transition-colors"
                                >
                                    <Send size={20} />
                                </button>
                            </form>
                        </>
                    ) : (
                        <div className="flex-1 flex flex-col items-center justify-center text-slate-500">
                            <MessageCircle size={48} className="mb-4 opacity-50" />
                            <p>Select a conversation to start chatting</p>
                        </div>
                    )}
                </div>
            </div>
        </div>
    );
};

export default Messages;
