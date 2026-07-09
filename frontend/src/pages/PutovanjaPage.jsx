import { useState, useEffect } from "react";
import { useSearchParams, useNavigate } from "react-router-dom";
import Navbar from "../components/layout/Navbar";
import Footer from "../components/layout/Footer";
import RezervacijaModal from "../components/modals/RezervacijaModal";
import api from "../api/api";

export default function PutovanjaPage() {
  const [searchParams] = useSearchParams();
  const navigate = useNavigate();

  const [gradovi, setGradovi] = useState([]);
  const [putovanja, setPutovanja] = useState([]);
  const [loading, setLoading] = useState(true);
  const [modalPutovanje, setModalPutovanje] = useState(null);

  const [forma, setForma] = useState({
    polazisteId: searchParams.get("polazisteId") || "",
    odredisteId: searchParams.get("odredisteId") || "",
    datum: searchParams.get("datum") || "",
    putnici: searchParams.get("putnici") || 1,
  });

  const [filteri, setFilteri] = useState({
    maxCena: 5000,
    sortBy: "cena-asc",
  });

 useEffect(() => {
  api.get("/api/gradovi").then((res) => setGradovi(res.data));

  setLoading(true);
  api.get("/api/putovanja/pretraga")
    .then((res) => setPutovanja(res.data))
    .finally(() => setLoading(false));
}, []);

const handleSearch = (e) => {
  e.preventDefault();
  console.log("forma:", forma);
  setLoading(true);
  const params = new URLSearchParams();
  if (forma.polazisteId) params.append("polazisteId", forma.polazisteId);
  if (forma.odredisteId) params.append("odredisteId", forma.odredisteId);
  if (forma.datum) params.append("datum", forma.datum);

  console.log("params:", params.toString());

  api.get(`/api/putovanja/pretraga?${params.toString()}`)
    .then((res) => {
      console.log("rezultati:", res.data);
      setPutovanja(res.data);
    })
    .finally(() => setLoading(false));
};
  const getGradNaziv = (id) => {
    const grad = gradovi.find((g) => g.id === Number(id));
    return grad ? grad.naziv : "";
  };

  const sortovanaPutovanja = putovanja
    .filter((p) => p.osnovnaCena <= filteri.maxCena)
    .sort((a, b) => {
      if (filteri.sortBy === "cena-asc") return a.osnovnaCena - b.osnovnaCena;
      if (filteri.sortBy === "cena-desc") return b.osnovnaCena - a.osnovnaCena;
      if (filteri.sortBy === "vreme-asc") return new Date(a.vremePolaska) - new Date(b.vremePolaska);
      if (filteri.sortBy === "vreme-desc") return new Date(b.vremePolaska) - new Date(a.vremePolaska);
      return 0;
    });

  const formatVreme = (dateStr) => {
    if (!dateStr) return "";
    return new Date(dateStr).toLocaleTimeString("sr-RS", { hour: "2-digit", minute: "2-digit" });
  };

  const formatDatum = (dateStr) => {
    if (!dateStr) return "";
    return new Date(dateStr).toLocaleDateString("sr-RS", {
      day: "2-digit",
      month: "2-digit",
      year: "numeric",
    });
  };

  const formatTrajanje = (polazak, dolazak) => {
    if (!polazak || !dolazak) return "";
    const diff = new Date(dolazak) - new Date(polazak);
    const sati = Math.floor(diff / 3600000);
    const minuti = Math.floor((diff % 3600000) / 60000);
    return `${sati}h ${minuti}min`;
  };

  return (
    <div className="min-h-screen bg-[#f8f9fc]">
      <Navbar />

      {/* HEADER */}
      <div
        className="px-12 py-8"
        style={{ background: "linear-gradient(135deg, #1a3a8f 0%, #1565c0 100%)" }}
      >
        <h1 className="text-white text-2xl font-medium">Pretraga putovanja</h1>
        <p className="text-[#b3cef5] text-sm mt-1">
          {getGradNaziv(forma.polazisteId)} {forma.polazisteId && "→"} {getGradNaziv(forma.odredisteId)}
          {forma.datum && ` · ${forma.datum}`}
          {` · ${forma.putnici} putnik`}
        </p>
      </div>

      {/* SEARCH BAR */}
      <div className="bg-[#1249a0] px-12 pb-5">
        <form onSubmit={handleSearch} className="bg-white rounded-2xl px-5 py-4 flex gap-3 items-end">
          <div className="flex flex-col gap-1 flex-1">
            <label className="text-xs text-gray-400 font-medium">📍 Polazište</label>
            <select
              value={forma.polazisteId}
              onChange={(e) => setForma({ ...forma, polazisteId: e.target.value })}
              className="border-none text-[#1a237e] text-sm font-medium outline-none bg-transparent cursor-pointer"
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
              className="border-none text-[#1a237e] text-sm font-medium outline-none bg-transparent cursor-pointer"
            >
              <option value="">Izaberi odredište</option>
              {gradovi.map((g) => (
                <option key={g.id} value={g.id}>{g.naziv} ({g.skracenica})</option>
              ))}
            </select>
          </div>
          <div className="w-px self-stretch bg-gray-100" />
          <div className="flex flex-col gap-1 flex-1">
            <label className="text-xs text-gray-400 font-medium">📅 Datum</label>
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
            onClick={handleSearch}
            className="bg-[#1565c0] hover:bg-[#0d47a1] text-white rounded-xl px-6 py-3 text-sm font-medium border-none cursor-pointer transition flex-shrink-0"
          >
            🔍 Pretraži
          </button>
        </form>
      </div>

      {/* CONTENT */}
      <div
        className="grid gap-6 px-12 py-6 mx-auto"
        style={{ gridTemplateColumns: "260px 1fr", maxWidth: "1100px" }}
      >
        {/* FILTERI */}
        <div className="bg-white rounded-2xl p-6 border border-gray-100 h-fit">
          <div className="flex justify-between items-center mb-5">
            <span className="text-[#1a237e] text-sm font-medium">Filteri</span>
            <span
              onClick={() => setFilteri({ maxCena: 5000, sortBy: "cena-asc" })}
              className="text-gray-400 text-xs cursor-pointer hover:text-[#1565c0]"
            >
              Resetuj
            </span>
          </div>

          <div className="mb-5">
            <div className="text-gray-600 text-xs font-medium mb-3">Maksimalna cena</div>
            <input
              type="range"
              min="0"
              max="5000"
              value={filteri.maxCena}
              onChange={(e) => setFilteri({ ...filteri, maxCena: Number(e.target.value) })}
              className="w-full accent-[#1565c0]"
            />
            <div className="flex justify-between text-xs text-gray-400 mt-1">
              <span>0 RSD</span>
              <span>{filteri.maxCena} RSD</span>
            </div>
          </div>
        </div>

        {/* REZULTATI */}
        <div>
          <div className="flex justify-between items-center mb-4">
            <span className="text-gray-400 text-sm">
              Pronađeno <strong className="text-[#1a237e]">{sortovanaPutovanja.length} putovanja</strong>
            </span>
            <select
              value={filteri.sortBy}
              onChange={(e) => setFilteri({ ...filteri, sortBy: e.target.value })}
              className="border border-gray-200 rounded-lg px-3 py-2 text-xs text-[#1a237e] outline-none cursor-pointer"
            >
              <option value="cena-asc">Sortiraj: Najjeftinije</option>
              <option value="cena-desc">Sortiraj: Najskuplje</option>
              <option value="vreme-asc">Sortiraj: Najranije</option>
              <option value="vreme-desc">Sortiraj: Najkasnije</option>
            </select>
          </div>

          {loading ? (
            <div className="text-center text-gray-400 py-16">Učitavanje...</div>
          ) : sortovanaPutovanja.length === 0 ? (
            <div className="text-center text-gray-400 py-16 bg-white rounded-2xl border border-gray-100">
              <div className="text-4xl mb-3">🚌</div>
              <div className="text-sm">Nema dostupnih putovanja.</div>
            </div>
          ) : (
            sortovanaPutovanja.map((p) => (
              <div
                key={p.id}
                className="bg-white rounded-2xl p-5 border border-gray-100 mb-3 flex justify-between items-center"
              >
                <div>
                  <div className="flex items-center gap-3 mb-3">
                    <span className="text-[#1a237e] text-lg font-medium">{getGradNaziv(p.polazisteId)}</span>
                    <span className="text-[#1565c0]">→</span>
                    <span className="text-[#1a237e] text-lg font-medium">{getGradNaziv(p.odredisteId)}</span>
                  </div>
                  <div className="flex gap-6">
                    <div className="flex flex-col gap-0.5">
                      <span className="text-gray-300 text-xs">Polazak</span>
                      <span className="text-gray-600 text-sm font-medium">
                        {formatDatum(p.vremePolaska)} · {formatVreme(p.vremePolaska)}
                      </span>
                    </div>
                    <div className="flex flex-col gap-0.5">
                      <span className="text-gray-300 text-xs">Dolazak</span>
                      <span className="text-gray-600 text-sm font-medium">
                        {formatDatum(p.vremeDolaska)} · {formatVreme(p.vremeDolaska)}
                      </span>
                    </div>
                    <div className="flex flex-col gap-0.5">
                      <span className="text-gray-300 text-xs">Trajanje</span>
                      <span className="text-gray-600 text-sm font-medium">
                        {formatTrajanje(p.vremePolaska, p.vremeDolaska)}
                      </span>
                    </div>
                  </div>
                </div>

                <div className="flex flex-col items-end gap-3">
                  <div className="text-right">
                    <div className="text-gray-300 text-xs">od</div>
                    <div className="text-[#1a237e] text-2xl font-medium">{p.osnovnaCena} RSD</div>
                  </div>
                  <div className="flex gap-1.5">
                    <span className="text-xs px-2 py-0.5 rounded-full border border-[#1565c0] text-[#1565c0] bg-[#e8f0fe]">Standard</span>
                    <span className="text-xs px-2 py-0.5 rounded-full border border-green-600 text-green-600 bg-green-50">Student</span>
                    <span className="text-xs px-2 py-0.5 rounded-full border border-orange-500 text-orange-500 bg-orange-50">VIP</span>
                  </div>
                  <button
                    onClick={() => setModalPutovanje(p)}
                    className="bg-[#ffa726] hover:bg-[#fb8c00] text-white border-none rounded-xl px-5 py-2.5 text-sm font-medium cursor-pointer transition"
                  >
                    Rezerviši →
                  </button>
                </div>
              </div>
            ))
          )}
        </div>
      </div>

      {/* MODAL */}
      {modalPutovanje && (
        <RezervacijaModal
          putovanje={modalPutovanje}
          gradovi={gradovi}
          onClose={() => setModalPutovanje(null)}
        />
      )}

      <Footer />
    </div>
  );
}