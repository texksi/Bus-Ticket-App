import { Link, useNavigate } from "react-router-dom";
import { useKorpa } from "../../context/KorpaContext";

function getUserFromToken() {
  const token = localStorage.getItem("token");
  if (!token) return null;
  try {
    const payload = JSON.parse(atob(token.split(".")[1]));
    return payload;
  } catch {
    return null;
  }
}

export default function Navbar() {
  const navigate = useNavigate();
  const user = getUserFromToken();
  const { korpa } = useKorpa();

  const handleLogout = () => {
    localStorage.removeItem("token");
    navigate("/login");
  };

  return (
    <nav className="bg-[#1a237e] flex items-center justify-between px-10 h-16">
      <Link to="/" className="text-white text-xl font-medium no-underline flex items-center gap-2">
        🚌 BusTicket
      </Link>

      <div className="flex gap-8">
        <Link to="/" className="text-white/80 text-sm no-underline hover:text-[#ffa726] transition">Početna</Link>
        <Link to="/putovanja" className="text-white/80 text-sm no-underline hover:text-[#ffa726] transition">Putovanja</Link>
        <Link to="/o-nama" className="text-white/80 text-sm no-underline hover:text-[#ffa726] transition">O nama</Link>
       {user && (
  <Link to="/moje-rezervacije" className="text-white/80 text-sm no-underline hover:text-[#ffa726] transition">
    Moje rezervacije
  </Link>
)}
       <Link to="/korpa" className="relative text-white/80 text-sm no-underline hover:text-[#ffa726] transition flex items-center gap-1">
  🛒
  {korpa.length > 0 && (
    <span className="bg-[#ffa726] text-white text-xs w-4 h-4 rounded-full flex items-center justify-center">
      {korpa.length}
    </span>
  )}
</Link>
        {user?.role === "ADMIN" && (
  <Link to="/admin" className="text-white/80 text-sm no-underline hover:text-[#ffa726] transition">
    Dashboard
  </Link>
)}
      </div>

      <div className="flex gap-3 items-center">
        {user ? (
          <>
            <div className="flex items-center gap-2 bg-white/10 px-4 py-2 rounded-xl">
              <div className="w-7 h-7 bg-[#ffa726] rounded-full flex items-center justify-center text-white text-xs font-medium">
                {user.sub?.charAt(0).toUpperCase()}
              </div>
              <span className="text-white text-sm">{user.sub}</span>
            </div>
            <button
              onClick={handleLogout}
              className="border border-white/40 text-white bg-transparent rounded-lg px-4 py-2 text-xs cursor-pointer hover:bg-white/10 transition"
            >
              Odjavi se
            </button>
          </>
        ) : (
          <>
            <Link to="/login" className="border border-white/40 text-white bg-transparent rounded-lg px-4 py-2 text-xs cursor-pointer hover:bg-white/10 transition">
              Prijava
            </Link>
            <Link to="/register" className="bg-[#ffa726] text-white border-none rounded-lg px-4 py-2 text-xs cursor-pointer hover:bg-[#fb8c00] transition">
              Registracija
            </Link>
          </>
        )}
      </div>
    </nav>
  );
}