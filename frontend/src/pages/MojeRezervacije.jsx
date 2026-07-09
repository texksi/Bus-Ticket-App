import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import Navbar from "../components/layout/Navbar";
import Footer from "../components/layout/Footer";
import api from "../api/api";

export default function MojeRezervacijePage() {
  const navigate = useNavigate();
  const [rezervacije, setRezervacije] = useState([]);
  const [karte, setKarte] = useState([]);
  const [putovanja, setPutovanja] = useState([]);
  const [gradovi, setGradovi] = useState([]);
  const [loading, setLoading] = useState(true);
  const [ocenaModal, setOcenaModal] = useState(null);
  const [ocenaForma, setOcenaForma] = useState({ ocena: 5, komentar: "" });
  const [ocenjenaRezervacija, setOcenjenaRezervacija] = useState([]);

  const getUserId = () => {
    const token = localStorage.getItem("token");
    if (!token) return null;
    try {
      return JSON.parse(atob(token.split(".")[1])).id;
    } catch {
      return null;
    }
  };

  const ucitajRezervacije = async (korisnikId) => {
    const res = await api.get(`/api/korisnici/${korisnikId}/rezervacije`);
    setRezervacije(res.data);

    const kartePoRez = await Promise.all(
      res.data.map((rez) => api.get(`/api/rezervacije/${rez.id}/karte`))
    );
    const sveKarte = kartePoRez.flatMap((r) => r.data);
    setKarte(sveKarte);
  };

  useEffect(() => {
    const korisnikId = getUserId();
    if (!korisnikId) {
      navigate("/login");
      return;
    }

    Promise.all([
      api.get("/api/putovanja"),
      api.get("/api/gradovi"),
    ]).then(async ([p, g]) => {
      setPutovanja(p.data);
      setGradovi(g.data);
      await ucitajRezervacije(korisnikId);
    }).finally(() => setLoading(false));
  }, []);

  const getGradNaziv = (id) => gradovi.find((g) => g.id === Number(id))?.naziv || "";

  const getPutovanjeNaziv = (id) => {
    const p = putovanja.find((p) => p.id === Number(id));
    if (!p) return "";
    return `${getGradNaziv(p.polazisteId)} → ${getGradNaziv(p.odredisteId)}`;
  };

  const getPutovanjeId = (rezervacijaId) => {
    const karteRez = karte.filter((k) => k.rezervacijaId === Number(rezervacijaId));
    return karteRez.length > 0 ? karteRez[0].putovanjeId : null;
  };

  const getKarteZaRezervaciju = (rezervacijaId) =>
    karte.filter((k) => k.rezervacijaId === Number(rezervacijaId));

  const formatDatum = (dateStr) => {
    if (!dateStr) return "";
    return new Date(dateStr).toLocaleDateString("sr-RS", {
      day: "2-digit",
      month: "long",
      year: "numeric",
    });
  };

  const getStatusBadge = (status) => {
    switch (status) {
      case "AKTIVNA": return "bg-green-50 text-green-700";
      case "OTKAZANA": return "bg-red-50 text-red-600";
      case "ZAVRSENA": return "bg-gray-100 text-gray-500";
      default: return "bg-blue-50 text-blue-600";
    }
  };

  const getTipBadge = (tip) => {
    switch (tip) {
      case "STUDENT": return "bg-green-50 text-green-700";
      case "VIP": return "bg-orange-50 text-orange-600";
      default: return "bg-[#e8f0fe] text-[#1565c0]";
    }
  };

  const getNacinPlacanja = (nacin) => {
    switch (nacin) {
      case "ONLINE": return "Online plaćanje";
      case "KARTICA": return "Plaćanje karticom";
      case "GOTOVINA": return "Plaćanje gotovinom";
      default: return nacin;
    }
  };

  const handleOtkazi = async (rezervacijaId) => {
    if (!window.confirm("Da li ste sigurni da želite da otkažete rezervaciju?")) return;
    try {
      const rez = rezervacije.find((r) => r.id === rezervacijaId);
      await api.put(`/api/rezervacije/${rezervacijaId}`, {
        status: "OTKAZANA",
        nacinPlacanja: rez.nacinPlacanja,
        ukupanIznos: rez.ukupanIznos,
        korisnikId: getUserId(),
      });
      await ucitajRezervacije(getUserId());
    } catch (err) {
      console.error(err);
    }
  };

  const handleOceni = async () => {
    if (!ocenaModal) return;
    try {
      const putovanjeId = getPutovanjeId(ocenaModal.id);
      await api.post("/api/ocene", {
        ocena: ocenaForma.ocena,
        komentar: ocenaForma.komentar,
        korisnikId: getUserId(),
        putovanjeId,
      });
      setOcenjenaRezervacija([...ocenjenaRezervacija, ocenaModal.id]);
      setOcenaModal(null);
      setOcenaForma({ ocena: 5, komentar: "" });
    } catch (err) {
      console.error(err);
    }
  };

  return (
    <div className="min-h-screen bg-[#f8f9fc]">
      <Navbar />

      <div
        className="px-12 py-6"
        style={{ background: "linear-gradient(135deg, #1a3a8f 0%, #1565c0 100%)" }}
      >
        <h1 className="text-white text-2xl font-medium">Moje rezervacije</h1>
        <p className="text-[#b3cef5] text-sm mt-1">Pregled svih vaših rezervacija</p>
      </div>

      <div className="max-w-3xl mx-auto px-6 py-6">
        {loading ? (
          <div className="text-center text-gray-400 py-16">Učitavanje...</div>
        ) : rezervacije.length === 0 ? (
          <div className="bg-white rounded-2xl p-16 text-center border border-gray-100">
            <div className="text-5xl mb-4">🎫</div>
            <div className="text-gray-400 text-sm mb-4">Nemate nijednu rezervaciju</div>
            <button
              onClick={() => navigate("/putovanja")}
              className="bg-[#1565c0] text-white border-none rounded-xl px-6 py-2.5 text-sm font-medium cursor-pointer hover:bg-[#0d47a1] transition"
            >
              Pronađi putovanje
            </button>
          </div>
        ) : (
          rezervacije.map((r) => {
            const karteRez = getKarteZaRezervaciju(r.id);
            const vecOcenjena = ocenjenaRezervacija.includes(r.id);
            return (
              <div
                key={r.id}
                className="bg-white rounded-2xl border border-gray-100 mb-4 overflow-hidden"
              >
                {/* HEADER */}
                <div className="flex justify-between items-center px-5 py-4 border-b border-gray-50">
                  <div>
                    <div className="text-[#1a237e] text-sm font-medium">Rezervacija #{r.id}</div>
                    <div className="text-gray-400 text-xs mt-0.5">{formatDatum(r.datumKreiranja)}</div>
                  </div>
                  <div className="flex items-center gap-3">
                    <span className={`text-xs px-3 py-1 rounded-full ${getStatusBadge(r.status)}`}>
                      {r.status}
                    </span>
                    {r.status === "AKTIVNA" && (
                      <button
                        onClick={() => handleOtkazi(r.id)}
                        className="bg-red-50 text-red-600 border-none rounded-lg px-3 py-1.5 text-xs cursor-pointer hover:bg-red-100 transition"
                      >
                        Otkaži
                      </button>
                    )}
                    {r.status === "ZAVRSENA" && !vecOcenjena && (
                      <button
                        onClick={() => setOcenaModal(r)}
                        className="bg-[#e8f0fe] text-[#1565c0] border-none rounded-lg px-3 py-1.5 text-xs cursor-pointer hover:bg-[#c8d8f8] transition"
                      >
                        ⭐ Oceni
                      </button>
                    )}
                    {vecOcenjena && (
                      <span className="text-yellow-500 text-xs">⭐ Ocenjeno</span>
                    )}
                  </div>
                </div>

                {/* KARTE */}
                <div className="px-5 py-4">
                  <div className="flex flex-col gap-2 mb-4">
                    {karteRez.length === 0 ? (
                      <div className="text-gray-400 text-xs">Nema karata</div>
                    ) : (
                      karteRez.map((k) => (
                        <div
                          key={k.id}
                          className="flex justify-between items-center bg-[#f8f9fc] rounded-xl px-4 py-2.5"
                        >
                          <div className="flex items-center gap-3">
                            <span className="bg-[#e8f0fe] text-[#1565c0] text-xs font-medium px-2.5 py-1 rounded-lg">
                              {k.brojSedista}
                            </span>
                            <span className="text-gray-600 text-xs">
                              {getPutovanjeNaziv(k.putovanjeId)}
                              <span className={`text-xs px-2 py-0.5 rounded-full ml-2 ${getTipBadge(k.tip)}`}>
                                {k.tip}
                              </span>
                            </span>
                          </div>
                          <span className="text-[#1a237e] text-sm font-medium">{k.finalnaCena} RSD</span>
                        </div>
                      ))
                    )}
                  </div>

                  {/* FOOTER */}
                  <div className="flex justify-between items-center pt-3 border-t border-gray-100">
                    <span className="text-gray-400 text-xs">{getNacinPlacanja(r.nacinPlacanja)}</span>
                    <span className="text-[#1a237e] text-base font-medium">{r.ukupanIznos} RSD</span>
                  </div>
                </div>
              </div>
            );
          })
        )}
      </div>

      {/* MODAL ZA OCENU */}
      {ocenaModal && (
        <div className="fixed inset-0 bg-black/40 flex items-center justify-center z-50 p-4">
          <div className="bg-white rounded-2xl w-full max-w-md p-8">
            <div className="flex justify-between items-center mb-6">
              <h2 className="text-[#1a237e] text-base font-medium">Ocenite putovanje</h2>
              <button
                onClick={() => setOcenaModal(null)}
                className="bg-gray-100 border-none rounded-lg w-8 h-8 cursor-pointer text-gray-500 hover:bg-gray-200 transition"
              >
                ✕
              </button>
            </div>

            <div className="flex flex-col gap-5">
              <div>
                <label className="text-xs text-gray-500 font-medium block mb-3">Ocena</label>
                <div className="flex gap-2">
                  {[1, 2, 3, 4, 5].map((zvezda) => (
                    <button
                      key={zvezda}
                      onClick={() => setOcenaForma({ ...ocenaForma, ocena: zvezda })}
                      className={`text-2xl border-none bg-transparent cursor-pointer transition ${
                        zvezda <= ocenaForma.ocena ? "text-yellow-400" : "text-gray-200"
                      }`}
                    >
                      ★
                    </button>
                  ))}
                </div>
              </div>

              <div>
                <label className="text-xs text-gray-500 font-medium block mb-2">
                  Komentar (opciono)
                </label>
                <textarea
                  value={ocenaForma.komentar}
                  onChange={(e) => setOcenaForma({ ...ocenaForma, komentar: e.target.value })}
                  placeholder="Opišite vaše iskustvo..."
                  rows={4}
                  className="w-full border border-gray-200 rounded-xl px-4 py-2.5 text-sm text-[#1a237e] outline-none focus:border-[#1565c0] resize-none"
                />
              </div>

              <div className="flex justify-end gap-3">
                <button
                  onClick={() => setOcenaModal(null)}
                  className="bg-gray-100 text-gray-500 border-none rounded-xl px-5 py-2.5 text-sm cursor-pointer hover:bg-gray-200 transition"
                >
                  Otkaži
                </button>
                <button
                  onClick={handleOceni}
                  className="bg-[#1565c0] hover:bg-[#0d47a1] text-white border-none rounded-xl px-5 py-2.5 text-sm font-medium cursor-pointer transition"
                >
                  Potvrdi ocenu
                </button>
              </div>
            </div>
          </div>
        </div>
      )}

      <Footer />
    </div>
  );
}