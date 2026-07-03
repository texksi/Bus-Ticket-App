import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import Navbar from "../components/layout/Navbar";
import api from "../api/api";

const BRZI_PRISTUP = [
  { ikona: "🚌", naziv: "Putovanja", opis: "Dodaj, izmeni ili obriši autobuska putovanja i linije.", path: "/admin/putovanja", boja: "#e8f0fe" },
  { ikona: "🎫", naziv: "Karte", opis: "Pregled i upravljanje svim prodatim kartama.", path: "/admin/karte", boja: "#e8f5e9" },
  { ikona: "📋", naziv: "Rezervacije", opis: "Upravljanje rezervacijama i statusima plaćanja.", path: "/admin/rezervacije", boja: "#fff3e0" },
  { ikona: "👥", naziv: "Korisnici", opis: "Pregled registrovanih korisnika i upravljanje nalozima.", path: "/admin/korisnici", boja: "#fce8e8" },
  { ikona: "🏢", naziv: "Kompanije", opis: "Upravljanje prevozničkim kompanijama.", path: "/admin/kompanije", boja: "#f3e8ff" },
  { ikona: "🚍", naziv: "Vozila", opis: "Pregled i upravljanje voznim parkom.", path: "/admin/vozila", boja: "#e8f0fe" },
  { ikona: "📍", naziv: "Gradovi", opis: "Upravljanje gradovima i destinacijama.", path: "/admin/gradovi", boja: "#e8f5e9" },
  { ikona: "⭐", naziv: "Ocene", opis: "Pregled ocena i komentara putnika.", path: "/admin/ocene", boja: "#fff3e0" },
  { ikona: "💳", naziv: "Plaćanja", opis: "Pregled i upravljanje plaćanjima.", path: "/admin/placanja", boja: "#fce8e8" },
];

export default function AdminDashboardPage() {
  const navigate = useNavigate();
  const [stats, setStats] = useState({
    putovanja: 0,
    karte: 0,
    rezervacije: 0,
    korisnici: 0,
  });

  useEffect(() => {
    Promise.all([
      api.get("/api/putovanja"),
      api.get("/api/karte"),
      api.get("/api/rezervacije"),
      api.get("/api/korisnici"),
    ]).then(([put, kart, rez, kor]) => {
      setStats({
        putovanja: put.data.length,
        karte: kart.data.length,
        rezervacije: rez.data.length,
        korisnici: kor.data.length,
      });
    }).catch(() => {});
  }, []);

  const getUserFromToken = () => {
    const token = localStorage.getItem("token");
    if (!token) return null;
    try {
      return JSON.parse(atob(token.split(".")[1]));
    } catch {
      return null;
    }
  };

  const user = getUserFromToken();

  return (
    <div className="min-h-screen bg-[#f8f9fc]">
      <Navbar />

      <div className="px-12 py-8">
        {/* WELCOME */}
        <div className="mb-8">
          <h1 className="text-[#1a237e] text-2xl font-medium">
            Dobrodošli, {user?.sub} 👋
          </h1>
          <p className="text-gray-400 text-sm mt-1">
            Pregled stanja sistema i brzi pristup upravljanju
          </p>
        </div>

        {/* STAT KARTICE */}
        <div className="grid grid-cols-4 gap-4 mb-10">
          {[
            { ikona: "🚌", label: "Ukupno putovanja", vrednost: stats.putovanja, boja: "#e8f0fe" },
            { ikona: "🎫", label: "Prodatih karata", vrednost: stats.karte, boja: "#e8f5e9" },
            { ikona: "📋", label: "Rezervacije", vrednost: stats.rezervacije, boja: "#fff3e0" },
            { ikona: "👥", label: "Korisnici", vrednost: stats.korisnici, boja: "#fce8e8" },
          ].map((s, i) => (
            <div key={i} className="bg-white rounded-2xl p-6 border border-gray-100 relative overflow-hidden">
              <div
                className="w-11 h-11 rounded-xl flex items-center justify-center text-xl mb-4"
                style={{ background: s.boja }}
              >
                {s.ikona}
              </div>
              <div className="text-gray-400 text-xs mb-1">{s.label}</div>
              <div className="text-[#1a237e] text-3xl font-medium">{s.vrednost}</div>
              <div className="absolute right-4 bottom-2 text-6xl opacity-5">{s.ikona}</div>
            </div>
          ))}
        </div>

        {/* BRZI PRISTUP */}
        <div className="text-[#1a237e] text-sm font-medium mb-4">Brzi pristup</div>
        <div className="grid grid-cols-3 gap-4">
          {BRZI_PRISTUP.map((item, i) => (
            <div
              key={i}
              onClick={() => navigate(item.path)}
              className="bg-white rounded-2xl p-6 border border-gray-100 cursor-pointer hover:shadow-md hover:border-[#1565c0] hover:-translate-y-0.5 transition-all flex flex-col gap-3"
            >
              <div
                className="w-11 h-11 rounded-xl flex items-center justify-center text-xl"
                style={{ background: item.boja }}
              >
                {item.ikona}
              </div>
              <div className="text-[#1a237e] text-sm font-medium">{item.naziv}</div>
              <div className="text-gray-400 text-xs leading-relaxed">{item.opis}</div>
              <div className="text-[#b3c1e8] text-xs mt-auto">Otvori →</div>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
}