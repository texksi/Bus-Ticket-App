import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import api from "../../api/api";

export default function Hero() {
  const navigate = useNavigate();
  const [gradovi, setGradovi] = useState([]);
  const [forma, setForma] = useState({
    polazisteId: "",
    odredisteId: "",
    datum: "",
    putnici: 1,
  });

  useEffect(() => {
    api.get("/api/gradovi").then((res) => setGradovi(res.data));
  }, []);

  const handleSearch = (e) => {
    e.preventDefault();
    navigate(`/putovanja?polazisteId=${forma.polazisteId}&odredisteId=${forma.odredisteId}&datum=${forma.datum}&putnici=${forma.putnici}`);
  };

  return (
    <div className="bg-[#1249a0] px-6 py-6 flex flex-col gap-4">
      {/* HERO */}
      <div
        className="min-h-[340px] grid grid-cols-2 overflow-hidden rounded-2xl w-[90%] mx-auto mb-8"
        style={{ background: "linear-gradient(135deg, #1a3a8f 0%, #1565c0 50%, #1976d2 100%)" }}
      >
        {/* LEFT */}
        <div className="flex flex-col justify-center gap-4 px-10 py-12">
          <span className="inline-block w-fit bg-white/15 border border-white/25 text-white text-xs tracking-widest uppercase px-3 py-1 rounded-full">
            🚌 Vaš autobuski partner
          </span>
          <h1 className="text-white text-5xl font-medium leading-tight m-0">
            Bus<span className="text-[#ffa726]">Ticket</span>
            <br />
            <span className="text-white text-2xl font-normal">Srbija</span>
          </h1>
          <p className="text-[#b3cef5] text-sm leading-relaxed max-w-xs m-0">
            Rezervišite kartu za vaše sledeće putovanje brzo i jednostavno.
            Stotine linija, povoljne cene.
          </p>
          <div className="flex gap-3 mt-1">
            <button
              onClick={handleSearch}
              className="bg-[#ffa726] hover:bg-[#fb8c00] text-white border-none rounded-xl px-5 py-2.5 text-sm font-medium cursor-pointer transition"
            >
              Rezerviši kartu
            </button>
            <button
              onClick={() => navigate("/o-nama")}
              className="bg-transparent text-white border border-white/35 rounded-xl px-5 py-2.5 text-sm cursor-pointer hover:bg-white/10 transition"
            >
              Saznaj više
            </button>
          </div>
        </div>

        {/* RIGHT - SVG */}
        <div className="relative overflow-hidden">
          <svg viewBox="0 0 460 340" xmlns="http://www.w3.org/2000/svg" className="absolute inset-0 w-full h-full">
            <rect width="460" height="340" fill="#1249a0"/>
            <ellipse cx="340" cy="70" rx="120" ry="60" fill="#1e6dd5" opacity="0.35"/>
            <polygon points="0,270 80,160 160,230 240,130 320,210 400,95 460,150 460,340 0,340" fill="#0d3d8a"/>
            <polygon points="0,310 70,220 140,268 210,180 280,245 360,155 430,210 460,195 460,340 0,340" fill="#0a3070"/>
            <polygon points="12,310 28,225 44,310" fill="#083060"/>
            <polygon points="8,330 28,240 48,330" fill="#072b58"/>
            <polygon points="32,315 50,235 68,315" fill="#083060"/>
            <rect x="222" y="110" width="26" height="120" fill="#1a5cb5" rx="1"/>
            <rect x="227" y="100" width="16" height="15" fill="#1e6dd5" rx="1"/>
            <rect x="282" y="95" width="22" height="135" fill="#1565c0" rx="1"/>
            <polygon points="282,95 293,78 304,95" fill="#1e6dd5"/>
            <rect x="368" y="118" width="24" height="112" fill="#1e6dd5" rx="1"/>
            <polygon points="368,118 380,100 392,118" fill="#2481f0"/>
            <rect x="340" y="128" width="20" height="102" fill="#1a5cb5" rx="1"/>
            <rect x="400" y="140" width="18" height="90" fill="#1565c0" rx="1"/>
            <rect x="225" y="118" width="5" height="5" fill="#81d4fa" rx="1" opacity="0.9"/>
            <rect x="234" y="118" width="5" height="5" fill="#4fc3f7" rx="1" opacity="0.7"/>
            <rect x="225" y="130" width="5" height="5" fill="#4fc3f7" rx="1" opacity="0.8"/>
            <rect x="234" y="130" width="5" height="5" fill="#81d4fa" rx="1" opacity="0.9"/>
            <rect x="286" y="105" width="5" height="5" fill="#81d4fa" rx="1" opacity="0.9"/>
            <rect x="295" y="105" width="5" height="5" fill="#4fc3f7" rx="1" opacity="0.7"/>
            <rect x="286" y="118" width="5" height="5" fill="#4fc3f7" rx="1" opacity="0.8"/>
            <rect x="295" y="118" width="5" height="5" fill="#81d4fa" rx="1" opacity="0.9"/>
            <rect x="372" y="128" width="5" height="5" fill="#81d4fa" rx="1" opacity="0.9"/>
            <rect x="381" y="128" width="5" height="5" fill="#4fc3f7" rx="1" opacity="0.7"/>
            <rect x="372" y="141" width="5" height="5" fill="#4fc3f7" rx="1" opacity="0.8"/>
            <rect x="381" y="141" width="5" height="5" fill="#81d4fa" rx="1" opacity="0.9"/>
            <rect x="0" y="278" width="460" height="62" fill="#0a2d6e"/>
            <rect x="0" y="273" width="460" height="9" fill="#0d3880"/>
            <rect x="0" y="298" width="55" height="3" fill="rgba(255,255,255,0.1)" rx="2"/>
            <rect x="85" y="298" width="55" height="3" fill="rgba(255,255,255,0.1)" rx="2"/>
            <rect x="175" y="298" width="55" height="3" fill="rgba(255,255,255,0.1)" rx="2"/>
            <rect x="265" y="298" width="55" height="3" fill="rgba(255,255,255,0.1)" rx="2"/>
            <rect x="355" y="298" width="55" height="3" fill="rgba(255,255,255,0.1)" rx="2"/>
            <ellipse cx="232" cy="292" rx="112" ry="5" fill="#071e4a" opacity="0.5"/>
            <rect x="88" y="226" width="272" height="62" fill="#ecf0f7" rx="9"/>
            <rect x="88" y="226" width="272" height="7" fill="#dde3ef" rx="9"/>
            <path d="M360,226 Q378,229 383,243 L383,278 Q378,285 360,290 Z" fill="#dde3ef"/>
            <path d="M362,233 Q375,236 379,244 L379,264 Q375,270 362,272 Z" fill="#5b9bd5" opacity="0.85"/>
            <line x1="366" y1="236" x2="366" y2="269" stroke="rgba(255,255,255,0.3)" strokeWidth="1.5"/>
            <rect x="97" y="234" width="30" height="22" fill="#5b9bd5" rx="3" opacity="0.88"/>
            <rect x="133" y="234" width="30" height="22" fill="#5b9bd5" rx="3" opacity="0.88"/>
            <rect x="169" y="234" width="30" height="22" fill="#5b9bd5" rx="3" opacity="0.88"/>
            <rect x="205" y="234" width="30" height="22" fill="#5b9bd5" rx="3" opacity="0.88"/>
            <rect x="241" y="234" width="30" height="22" fill="#5b9bd5" rx="3" opacity="0.88"/>
            <rect x="277" y="234" width="30" height="22" fill="#5b9bd5" rx="3" opacity="0.88"/>
            <rect x="313" y="234" width="22" height="22" fill="#5b9bd5" rx="3" opacity="0.88"/>
            <rect x="88" y="262" width="300" height="7" fill="#1976d2" rx="2"/>
            <rect x="102" y="259" width="18" height="31" fill="#d8dde8" rx="3"/>
            <rect x="111" y="261" width="1" height="27" fill="#c0c8d4"/>
            <rect x="88" y="278" width="300" height="11" fill="#d0d8e8" rx="3"/>
            <rect x="378" y="246" width="7" height="9" fill="#fff9c4" rx="2" opacity="0.95"/>
            <rect x="86" y="247" width="4" height="9" fill="#ef5350" rx="1" opacity="0.9"/>
            <circle cx="325" cy="292" r="18" fill="#1c2a3a"/>
            <circle cx="325" cy="292" r="12" fill="#263545"/>
            <circle cx="325" cy="292" r="5" fill="#37474f"/>
            <circle cx="325" cy="292" r="2" fill="#90a4ae"/>
            <circle cx="150" cy="292" r="18" fill="#1c2a3a"/>
            <circle cx="150" cy="292" r="12" fill="#263545"/>
            <circle cx="150" cy="292" r="5" fill="#37474f"/>
            <circle cx="150" cy="292" r="2" fill="#90a4ae"/>
            <ellipse cx="415" cy="292" rx="50" ry="4" fill="#071e4a" opacity="0.4"/>
            <rect x="378" y="248" width="74" height="42" fill="#c17f6b" rx="5"/>
            <path d="M432,248 Q447,244 453,252 L453,280 Q447,288 432,290 Z" fill="#b5705c"/>
            <path d="M434,252 Q444,248 449,256 L449,274 Q444,280 434,282 Z" fill="#78b4d8" opacity="0.85"/>
            <rect x="384" y="253" width="22" height="16" fill="#78b4d8" rx="2" opacity="0.85"/>
            <rect x="409" y="253" width="19" height="16" fill="#78b4d8" rx="2" opacity="0.85"/>
            <rect x="378" y="275" width="78" height="5" fill="#a0685a" rx="1"/>
            <circle cx="432" cy="292" r="12" fill="#1c2a3a"/>
            <circle cx="432" cy="292" r="7" fill="#263545"/>
            <circle cx="432" cy="292" r="3" fill="#37474f"/>
            <circle cx="395" cy="292" r="12" fill="#1c2a3a"/>
            <circle cx="395" cy="292" r="7" fill="#263545"/>
            <circle cx="395" cy="292" r="3" fill="#37474f"/>
          </svg>
        </div>
      </div>

      {/* SEARCH BAR */}
      <form onSubmit={handleSearch} className="bg-white rounded-2xl px-5 py-4 flex gap-3 items-end w-[90%] mx-auto mb-8">
        <div className="flex flex-col gap-1 flex-1">
          <label className="text-xs text-gray-400 font-medium">📍 Polazište</label>
          <select
            value={forma.polazisteId}
            onChange={(e) => setForma({ ...forma, polazisteId: e.target.value })}
            className="border-none text-[#1a237e] text-sm font-medium outline-none bg-transparent w-full cursor-pointer"
          >
            <option value="">Izaberi polazište</option>
            {gradovi.map((g) => (
              <option key={g.id} value={g.id}>{g.naziv} ({g.skracenica})</option>
            ))}
          </select>
        </div>
        <div className="w-px self-stretch bg-gray-100" />
        <div className="flex flex-col gap-1 flex-1">
          <label className="text-xs text-gray-400 font-medium">📍 Odredište</label>
          <select
            value={forma.odredisteId}
            onChange={(e) => setForma({ ...forma, odredisteId: e.target.value })}
            className="border-none text-[#1a237e] text-sm font-medium outline-none bg-transparent w-full cursor-pointer"
          >
            <option value="">Izaberi odredište</option>
            {gradovi.map((g) => (
              <option key={g.id} value={g.id}>{g.naziv} ({g.skracenica})</option>
            ))}
          </select>
        </div>
        <div className="w-px self-stretch bg-gray-100" />
        <div className="flex flex-col gap-1 flex-1">
          <label className="text-xs text-gray-400 font-medium">📅 Datum polaska</label>
          <input
            type="date"
            value={forma.datum}
            onChange={(e) => setForma({ ...forma, datum: e.target.value })}
            className="border-none text-[#1a237e] text-sm font-medium outline-none bg-transparent"
          />
        </div>
        <div className="w-px self-stretch bg-gray-100" />
        <div className="flex flex-col gap-1 w-20">
          <label className="text-xs text-gray-400 font-medium">👥 Putnici</label>
          <input
            type="number"
            min="1"
            max="10"
            value={forma.putnici}
            onChange={(e) => setForma({ ...forma, putnici: e.target.value })}
            className="border-none text-[#1a237e] text-sm font-medium outline-none bg-transparent w-full"
          />
        </div>
        <button
          type="submit"
          className="bg-[#1565c0] hover:bg-[#0d47a1] text-white rounded-xl px-6 py-3 text-sm font-medium flex items-center gap-2 transition flex-shrink-0"
        >
          🔍 Pretraži
        </button>
      </form>

      {/* STATS */}
      <div className="flex gap-0 px-2 pb-2">
        <div className="flex-1">
          <div className="text-[#ffa726] text-2xl font-medium">10K+</div>
          <div className="text-[#7a9fd4] text-xs mt-1">Putnika mesečno</div>
        </div>
        <div className="w-px bg-white/10 mx-6" />
        <div className="flex-1">
          <div className="text-[#ffa726] text-2xl font-medium">100K+</div>
          <div className="text-[#7a9fd4] text-xs mt-1">Destinacija</div>
        </div>
        <div className="w-px bg-white/10 mx-6" />
        <div className="flex-1">
          <div className="text-[#ffa726] text-2xl font-medium">50+</div>
          <div className="text-[#7a9fd4] text-xs mt-1">Linija</div>
        </div>
        <div className="w-px bg-white/10 mx-6" />
        <div className="flex-1">
          <div className="text-[#ffa726] text-2xl font-medium">4.8★</div>
          <div className="text-[#7a9fd4] text-xs mt-1">Prosečna ocena</div>
        </div>
      </div>
    </div>
  );
}