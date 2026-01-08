import { Link, useNavigate } from 'react-router-dom';
import { ShoppingBag, User, Menu, X, Home, PlusCircle, LogIn, LogOut, Bell } from 'lucide-react';
import { useState, useEffect, useRef } from 'react';
import { motion, AnimatePresence } from 'framer-motion';
import { useAuth } from '../context/AuthContext';
import { api } from '../services/api';

const NotificationBell = ({ user }) => {
    const [notifications, setNotifications] = useState([]);
    const [isOpen, setIsOpen] = useState(false);
    const [unreadCount, setUnreadCount] = useState(0);
    const dropdownRef = useRef(null);

    const fetchNotifications = async () => {
        try {
            const data = await api.getNotifications(user.id);
            setNotifications(data);
            setUnreadCount(data.filter(n => !n.read).length);
        } catch (e) { console.error(e); }
    };

    useEffect(() => {
        fetchNotifications();
        const interval = setInterval(fetchNotifications, 10000); // Poll every 10s
        return () => clearInterval(interval);
    }, [user.id]);

    const handleRead = async (n) => {
        if (!n.read) {
            try {
                await api.markNotificationRead(n.id);
                setNotifications(prev => prev.map(item => item.id === n.id ? { ...item, read: true } : item));
                setUnreadCount(prev => Math.max(0, prev - 1));
            } catch (e) { }
        }
    };

    return (
        <div className="relative" ref={dropdownRef}>
            <button
                onClick={() => setIsOpen(!isOpen)}
                className="relative text-gray-300 hover:text-white p-2 rounded-full hover:bg-white/10 transition-all"
            >
                <Bell size={20} />
                {unreadCount > 0 && (
                    <span className="absolute top-1 right-1 h-2 w-2 bg-red-500 rounded-full animate-pulse" />
                )}
            </button>

            <AnimatePresence>
                {isOpen && (
                    <motion.div
                        initial={{ opacity: 0, y: 10 }}
                        animate={{ opacity: 1, y: 0 }}
                        exit={{ opacity: 0, y: 10 }}
                        className="absolute right-0 mt-2 w-80 bg-slate-800 border border-white/10 rounded-xl shadow-2xl overflow-hidden z-[60]"
                    >
                        <div className="p-3 border-b border-white/10 flex justify-between items-center">
                            <h3 className="font-bold text-white">Notifications</h3>
                            <button onClick={fetchNotifications} className="text-xs text-teal-400 hover:text-teal-300">Refresh</button>
                        </div>
                        <div className="max-h-64 overflow-y-auto">
                            {notifications.length === 0 ? (
                                <p className="p-4 text-center text-slate-400 text-sm">No notifications</p>
                            ) : (
                                notifications.map(n => (
                                    <div
                                        key={n.id}
                                        className={`p-3 border-b border-white/5 hover:bg-white/5 cursor-pointer transition-colors ${!n.read ? 'bg-teal-500/10' : ''}`}
                                        onClick={() => handleRead(n)}
                                    >
                                        <p className="text-sm text-slate-200">{n.message}</p>
                                        <span className="text-[10px] text-slate-500 block mt-1">
                                            {new Date(n.createdAt).toLocaleString()}
                                        </span>
                                    </div>
                                ))
                            )}
                        </div>
                    </motion.div>
                )}
            </AnimatePresence>
        </div>
    );
};

const Navbar = () => {
    const [isOpen, setIsOpen] = useState(false);
    const { user, logout } = useAuth();
    const navigate = useNavigate();

    const handleLogout = () => {
        logout();
        navigate('/');
    };

    const links = [
        { name: 'Home', path: '/', icon: Home },
        { name: 'Marketplace', path: '/marketplace', icon: ShoppingBag },
    ];

    if (user) {
        links.push({ name: 'Post Ad', path: '/post-ad', icon: PlusCircle });
        links.push({ name: 'Messages', path: '/messages', icon: ShoppingBag }); // Reusing icon for now or need import
        links.push({ name: 'Profile', path: '/profile', icon: User });
    }

    return (
        <nav className="fixed top-0 w-full z-50 bg-white/10 backdrop-blur-md border-b border-white/20 shadow-lg">
            <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
                <div className="flex items-center justify-between h-16">
                    <div className="flex-shrink-0">
                        <Link to="/" className="text-2xl font-bold bg-gradient-to-r from-primary to-accent bg-clip-text text-transparent">
                            GOBE
                        </Link>
                    </div>
                    <div className="hidden md:block">
                        <div className="ml-10 flex items-baseline space-x-4">
                            {links.map((link) => (
                                <Link
                                    key={link.name}
                                    to={link.path}
                                    className="text-gray-300 hover:text-white hover:bg-white/10 px-3 py-2 rounded-md text-sm font-medium transition-all duration-300 flex items-center gap-2"
                                >
                                    <link.icon size={16} />
                                    {link.name}
                                </Link>
                            ))}

                            {/* Notification Bell */}
                            {user && <NotificationBell user={user} />}

                            {!user ? (
                                <Link
                                    to="/login"
                                    className="bg-primary/20 hover:bg-primary/30 text-primary border border-primary/50 px-4 py-2 rounded-full text-sm font-bold transition-all flex items-center gap-2"
                                >
                                    <LogIn size={16} /> Login
                                </Link>
                            ) : (
                                <button
                                    onClick={handleLogout}
                                    className="text-gray-400 hover:text-red-400 px-3 py-2 rounded-md text-sm font-medium transition-all flex items-center gap-2"
                                >
                                    <LogOut size={16} /> Logout
                                </button>
                            )}
                        </div>
                    </div>
                    <div className="-mr-2 flex md:hidden">
                        <button
                            onClick={() => setIsOpen(!isOpen)}
                            className="inline-flex items-center justify-center p-2 rounded-md text-gray-400 hover:text-white hover:bg-gray-700 focus:outline-none transition-colors"
                        >
                            {isOpen ? <X size={24} /> : <Menu size={24} />}
                        </button>
                    </div>
                </div>
            </div>

            <AnimatePresence>
                {isOpen && (
                    <motion.div
                        initial={{ opacity: 0, height: 0 }}
                        animate={{ opacity: 1, height: 'auto' }}
                        exit={{ opacity: 0, height: 0 }}
                        className="md:hidden bg-gray-900/90 backdrop-blur-xl border-b border-white/10"
                    >
                        <div className="px-2 pt-2 pb-3 space-y-1 sm:px-3">
                            {links.map((link) => (
                                <Link
                                    key={link.name}
                                    to={link.path}
                                    onClick={() => setIsOpen(false)}
                                    className="text-gray-300 hover:text-white block px-3 py-2 rounded-md text-base font-medium flex items-center gap-2"
                                >
                                    <link.icon size={18} />
                                    {link.name}
                                </Link>
                            ))}
                            {!user ? (
                                <Link
                                    to="/login"
                                    onClick={() => setIsOpen(false)}
                                    className="text-primary hover:text-white block px-3 py-2 rounded-md text-base font-bold flex items-center gap-2"
                                >
                                    <LogIn size={18} /> Login
                                </Link>
                            ) : (
                                <button
                                    onClick={() => { handleLogout(); setIsOpen(false); }}
                                    className="text-red-400 hover:text-red-300 block w-full text-left px-3 py-2 rounded-md text-base font-medium flex items-center gap-2"
                                >
                                    <LogOut size={18} /> Logout
                                </button>
                            )}
                        </div>
                    </motion.div>
                )}
            </AnimatePresence>
        </nav>
    );
};

export default Navbar;
