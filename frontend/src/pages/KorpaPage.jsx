import { useState } from "react";
import { useNavigate } from "react-router-dom";
import Navbar from "../components/layout/Navbar";
import { useKorpa } from "../context/KorpaContext";
import api from "../api/api";

export default function KorpaPage() {
  const navigate = useNavigate();
  const { korpa, ukloniIzKorpe, ocistiKorpu, ukupanIznos } = useKorpa();
  const [uspesnaRezervacija, setUspesnaRezervacija] = useState(false);

  const getTipBadge = (tip) => {
    switch (tip) {
      case "STUDENT": return "bg-green-50 text-green-700";
      case "VIP": return "bg-orange-50 text-orange-600";
      default: return "bg-[#e8f0fe] text-[#1565c0]";
    }
  };

 const handlePotvrdi = async () => {
  const token = localStorage.getItem("token");
  if (!token) {
    navigate("/login");
    return;
  }

  try {
    const payload = JSON.parse(atob(token.split(".")[1]));
    const korisnikId = payload.id;

    const putovanjaGrupe = korpa.reduce((acc, karta) => {
      if (!acc[karta.putovanjeId]) acc[karta.putovanjeId] = [];
      acc[karta.putovanjeId].push(karta);
      return acc;
    }, {});

    let prvaRezervacijaId = null;
    let ukupnoSve = 0;

    for (const [putovanjeId, karte] of Object.entries(putovanjaGrupe)) {
      const ukupno = karte.reduce((sum, k) => sum + Number(k.finalnaCena), 0);
      ukupnoSve += ukupno;

      const rez = await api.post("/api/rezervacije", {
        nacinPlacanja: "ONLINE",
        status: "AKTIVNA",
        ukupanIznos: ukupno,
        korisnikId,
      });

      const rezervacijaId = rez.data.id;
      if (!prvaRezervacijaId) prvaRezervacijaId = rezervacijaId;

      for (const karta of karte) {
        await api.post("/api/karte", {
          brojSedista: karta.brojSedista,
          tip: karta.tip,
          rezervacijaId,
          putovanjeId: Number(putovanjeId),
        });
      }

      await api.put(`/api/rezervacije/${rezervacijaId}/iznos`);
    }

    ocistiKorpu();
    setUspesnaRezervacija(true);
    setTimeout(() => {
      navigate(`/placanje?rezervacijaId=${prvaRezervacijaId}&iznos=${ukupnoSve}`);
    }, 2000);
  } catch (err) {
    console.error(err);
    alert("Greška pri kreiranju rezervacije. Pokušajte ponovo.");
  }
};

  return (
    <div className="min-h-screen bg-[#f8f9fc]">
      <Navbar />

      <div
        className="px-12 py-6"
        style={{ background: "linear-gradient(135deg, #1a3a8f 0%, #1565c0 100%)" }}
      >
        <h1 className="text-white text-2xl font-medium">🛒 Korpa</h1>
        <p className="text-[#b3cef5] text-sm mt-1">{korpa.length} karata izabrano</p>
      </div>

      {uspesnaRezervacija ? (
        <div className="max-w-lg mx-auto mt-16 bg-white rounded-2xl p-12 text-center border border-gray-100">
          <div className="text-[#1a237e] text-base font-medium mb-2">
            Sistem je kreirao rezervaciju.
          </div>
          <div className="text-gray-400 text-sm">Preusmeravanje na plaćanje...</div>
        </div>
      ) : korpa.length === 0 ? (
        <div className="max-w-lg mx-auto mt-16 bg-white rounded-2xl p-12 text-center border border-gray-100">
          <div className="text-5xl mb-4">🛒</div>
          <div className="text-gray-400 text-sm mb-4">Korpa je prazna</div>
          <button
            onClick={() => navigate("/putovanja")}
            className="bg-[#1565c0] text-white border-none rounded-xl px-6 py-2.5 text-sm font-medium cursor-pointer hover:bg-[#0d47a1] transition"
          >
            Pronađi putovanje
          </button>
        </div>
      ) : (
        <div
          className="grid gap-6 px-12 py-6 mx-auto"
          style={{ gridTemplateColumns: "1fr 320px", maxWidth: "1000px" }}
        >
          {/* KARTE */}
          <div className="flex flex-col gap-3">
            {korpa.map((k, i) => (
              <div
                key={i}
                className="bg-white rounded-2xl px-5 py-4 border border-gray-100 flex justify-between items-center"
              >
                <div className="flex items-center gap-4">
                  <div className="w-11 h-11 bg-[#e8f0fe] rounded-xl flex items-center justify-center text-[#1565c0] font-medium text-sm flex-shrink-0">
                    {k.brojSedista}
                  </div>
                  <div>
                    <div className="text-[#1a237e] text-sm font-medium flex items-center gap-2">
                      {k.putovanjeNaziv}
                      <span className={`text-xs px-2 py-0.5 rounded-full ${getTipBadge(k.tip)}`}>
                        {k.tip}
                      </span>
                    </div>
                    <div className="text-gray-400 text-xs mt-0.5">
                      Sedište {k.brojSedista}
                    </div>
                  </div>
                </div>
                <div className="flex items-center gap-3">
                  <span className="text-[#1a237e] text-base font-medium">{k.finalnaCena} RSD</span>
                  <button
                    onClick={() => ukloniIzKorpe(k.brojSedista, k.putovanjeId)}
                    className="bg-[#fce8e8] text-[#c62828] border-none rounded-lg px-3 py-1.5 text-xs cursor-pointer hover:bg-[#f5c6c6] transition"
                  >
                    Ukloni
                  </button>
                </div>
              </div>
            ))}
          </div>

          {/* SUMMARY */}
          <div className="bg-white rounded-2xl p-6 border border-gray-100 h-fit sticky top-4">
            <h3 className="text-[#1a237e] text-sm font-medium mb-4">Pregled narudžbine</h3>

            <div className="flex justify-between mb-2">
              <span className="text-gray-400 text-sm">Broj karata</span>
              <span className="text-gray-600 text-sm font-medium">{korpa.length}</span>
            </div>

            {["STANDARD", "STUDENT", "VIP"].map((tip) => {
              const karte = korpa.filter((k) => k.tip === tip);
              if (karte.length === 0) return null;
              const iznos = karte.reduce((sum, k) => sum + k.finalnaCena, 0);
              return (
                <div key={tip} className="flex justify-between mb-2">
                  <span className="text-gray-400 text-sm">{tip} ({karte.length}×)</span>
                  <span className="text-gray-600 text-sm font-medium">{iznos} RSD</span>
                </div>
              );
            })}

            <div className="flex justify-between pt-3 mt-2 border-t border-gray-100">
              <span className="text-[#1a237e] text-sm font-medium">Ukupno</span>
              <span className="text-[#1a237e] text-xl font-medium">{ukupanIznos} RSD</span>
            </div>

            <button
              onClick={handlePotvrdi}
              className="w-full bg-[#ffa726] hover:bg-[#fb8c00] text-white border-none rounded-xl py-3 text-sm font-medium cursor-pointer transition mt-4"
            >
              Potvrdi rezervaciju →
            </button>
            <button
              onClick={ocistiKorpu}
              className="w-full bg-gray-100 text-gray-500 border-none rounded-xl py-2.5 text-sm cursor-pointer hover:bg-gray-200 transition mt-2"
            >
              Očisti korpu
            </button>

            <div className="bg-[#e8f0fe] rounded-xl px-4 py-3 text-xs text-[#1565c0] mt-4 leading-relaxed">
              ℹ️ Sedišta su rezervisana tek nakon potvrde. Karte u korpi nisu garantovane.
            </div>
          </div>
        </div>
      )}
    </div>
  );
}