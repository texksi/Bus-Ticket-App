import { useNavigate, useLocation } from "react-router-dom";

const SIDEBAR_ITEMS = [
  { ikona: "📊", naziv: "Dashboard", path: "/admin" },
  { ikona: "🚌", naziv: "Putovanja", path: "/admin/putovanja" },
  { ikona: "🎫", naziv: "Karte", path: "/admin/karte" },
  { ikona: "📋", naziv: "Rezervacije", path: "/admin/rezervacije" },
  { ikona: "👥", naziv: "Korisnici", path: "/admin/korisnici" },
  { ikona: "🏢", naziv: "Kompanije", path: "/admin/kompanije" },
  { ikona: "🚍", naziv: "Vozila", path: "/admin/vozila" },
  { ikona: "📍", naziv: "Gradovi", path: "/admin/gradovi" },
  { ikona: "⭐", naziv: "Ocene", path: "/admin/ocene" },
  { ikona: "💳", naziv: "Plaćanja", path: "/admin/placanja" },
];

export default function AdminSidebar() {
  const navigate = useNavigate();
  const location = useLocation();

  return (
    <div className="bg-[#1a237e] min-h-screen w-56 flex-shrink-0">
      <div className="text-white text-lg font-medium px-6 py-5 border-b border-white/10">
        🚌 BusTicket Admin
      </div>
      <div className="py-4">
        {SIDEBAR_ITEMS.map((item, i) => (
          <div
            key={i}
            onClick={() => navigate(item.path)}
            className={`flex items-center gap-3 px-6 py-2.5 cursor-pointer text-sm transition ${
              location.pathname === item.path
                ? "bg-white/12 text-white border-r-4 border-[#ffa726]"
                : "text-white/70 hover:bg-white/8 hover:text-white"
            }`}
          >
            <span className="w-5 text-center">{item.ikona}</span>
            {item.naziv}
          </div>
        ))}
      </div>
    </div>
  );
}