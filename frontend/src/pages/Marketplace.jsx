import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { api } from '../services/api';
import { motion } from 'framer-motion';
import { Tag, MapPin, Calendar, Euro, User } from 'lucide-react';

const Marketplace = () => {
    const [ads, setAds] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const loadAds = async () => {
            try {
                const data = await api.getAdvertisements();
                setAds(data);
            } catch (err) {
                console.error("Failed to load ads", err);
            }
            setLoading(false);
        };
        loadAds();
    }, []);

    return (
        <div className="min-h-screen pt-24 px-4 sm:px-6 lg:px-8 bg-gray-900 pb-10">
            <div className="max-w-7xl mx-auto">
                <h2 className="text-3xl font-bold text-white mb-8">Offres Disponibles</h2>

                {loading ? (
                    <div className="text-white text-center">Chargement...</div>
                ) : (
                    <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                        {ads.map((ad, index) => (
                            <motion.div
                                key={ad.id}
                                initial={{ opacity: 0, y: 20 }}
                                animate={{ opacity: 1, y: 0 }}
                                transition={{ duration: 0.3, delay: index * 0.1 }}
                                className="bg-white/5 backdrop-blur-sm border border-white/10 rounded-2xl overflow-hidden hover:border-primary/50 transition-colors group relative"
                            >
                                <Link to={`/ad/${ad.id}`} className="block h-full p-6">
                                    <div className="flex justify-between items-start mb-4">
                                        <h3 className="text-xl font-semibold text-white group-hover:text-primary transition-colors">
                                            {ad.title}
                                        </h3>
                                        <span className="bg-primary/20 text-primary px-3 py-1 rounded-full text-sm font-medium flex items-center gap-1">
                                            <Euro size={14} /> {ad.price}
                                        </span>
                                    </div>

                                    <p className="text-gray-400 mb-6 line-clamp-3">{ad.description}</p>

                                    <div className="flex items-center gap-4 text-sm text-gray-500">
                                        <div className="flex items-center gap-1">
                                            <User size={14} />
                                            <span>{ad.holder?.firstName} {ad.holder?.lastName}</span>
                                        </div>
                                        <div className="flex items-center gap-1">
                                            <MapPin size={14} />
                                            <span>{ad.place?.name || 'USMB'}</span>
                                        </div>
                                        <div className="flex items-center gap-1">
                                            <Calendar size={14} />
                                            <span>{new Date(ad.date).toLocaleDateString()}</span>
                                        </div>
                                    </div>
                                </Link>
                            </motion.div>
                        ))}
                    </div>
                )}
            </div>
        </div>
    );
};

export default Marketplace;
