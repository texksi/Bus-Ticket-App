import { useState, useEffect } from "react";
import Navbar from "../components/layout/Navbar";
import AdminSidebar from "../components/layout/AdminSidebar";
import api from "../api/api";

export default function AdminKartePage() {
  const [karte, setKarte] = useState([]);
  const [putovanja, setPutovanja] = useState([]);
  const [gradovi, setGradovi] = useState([]);
  const [loading, setLoading] = useState(true);
  const [pretraga, setPretraga] = useState("");
  const [filterTip, setFilterTip] = useState("");

  useEffect(() => {
    Promise.all([
      api.get("/api/karte"),
      api.get("/api/putovanja"),
      api.get("/api/gradovi"),
    ]).then(([k, p, g]) => {
      setKarte(k.data);
      setPutovanja(p.data);
      setGradovi(g.data);
    }).finally(() => setLoading(false));
  }, []);

  const getGradNaziv = (id) => gradovi.find((g) => g.id === id)?.naziv || "";

  const getPutovanjeNaziv = (id) => {
    const p = putovanja.find((p) => p.id === id);
    if (!p) return "";
    return `${getGradNaziv(p.polazisteId)} → ${getGradNaziv(p.odredisteId)}`;
  };

  const formatDatum = (dateStr) => {
    if (!dateStr) return "";
    return new Date(dateStr).toLocaleDateString("sr-RS");
  };

  const getTipBadge = (tip) => {
    switch (tip) {
      case "STUDENT": return "bg-green-50 text-green-700 border border-green-200";
      case "VIP": return "bg-orange-50 text-orange-600 border border-orange-200";
      default: return "bg-[#e8f0fe] text-[#1565c0] border border-blue-200";
    }
  };

  const handleObrisi = async (id) => {
    if (!window.confirm("Da li ste sigurni da želite da obrišete ovu kartu?")) return;
    try {
      await api.delete(`/api/karte/${id}`);
      setKarte(karte.filter((k) => k.id !== id));
    } catch (err) {
      console.error(err);
    }
  };

  const filtriraneKarte = karte.filter((k) => {
    const sediste = k.brojSedista?.toLowerCase().includes(pretraga.toLowerCase());
    const putovanje = getPutovanjeNaziv(k.putovanjeId).toLowerCase().includes(pretraga.toLowerCase());
    const tipOk = filterTip ? k.tip === filterTip : true;
    return (sediste || putovanje) && tipOk;
  });

  return (
    <div className="flex min-h-screen bg-[#f8f9fc]">
      <AdminSidebar />

      <div className="flex-1 flex flex-col">
        <Navbar />

        <div className="px-12 py-8">
          <div className="mb-6">
            <h1 className="text-[#1a237e] text-xl font-medium">Karte</h1>
            <p className="text-gray-400 text-sm mt-0.5">Pregled svih izdatih karata</p>
          </div>

          <div className="flex gap-3 mb-4">
            <input
              type="text"
              placeholder="🔍 Pretraži karte..."
              value={pretraga}
              onChange={(e) => setPretraga(e.target.value)}
              className="flex-1 border border-gray-200 rounded-xl px-4 py-2.5 text-sm text-[#1a237e] outline-none focus:border-[#1565c0]"
            />
            <select
              value={filterTip}
              onChange={(e) => setFilterTip(e.target.value)}
              className="border border-gray-200 rounded-xl px-4 py-2.5 text-sm text-[#666] outline-none cursor-pointer"
            >
              <option value="">Svi tipovi</option>
              <option value="STANDARD">Standard</option>
              <option value="STUDENT">Student</option>
              <option value="VIP">VIP</option>
            </select>
          </div>

          <div className="bg-white rounded-2xl border border-gray-100 overflow-hidden">
            <table className="w-full border-collapse">
              <thead>
                <tr className="bg-[#f8f9fc]">
                  <th className="text-left text-xs text-gray-400 font-medium uppercase tracking-wide px-5 py-3 border-b border-gray-100">Broj sedišta</th>
                  <th className="text-left text-xs text-gray-400 font-medium uppercase tracking-wide px-5 py-3 border-b border-gray-100">Tip</th>
                  <th className="text-left text-xs text-gray-400 font-medium uppercase tracking-wide px-5 py-3 border-b border-gray-100">Finalna cena</th>
                  <th className="text-left text-xs text-gray-400 font-medium uppercase tracking-wide px-5 py-3 border-b border-gray-100">Datum izdavanja</th>
                  <th className="text-left text-xs text-gray-400 font-medium uppercase tracking-wide px-5 py-3 border-b border-gray-100">Rezervacija</th>
                  <th className="text-left text-xs text-gray-400 font-medium uppercase tracking-wide px-5 py-3 border-b border-gray-100">Putovanje</th>
                  <th className="text-left text-xs text-gray-400 font-medium uppercase tracking-wide px-5 py-3 border-b border-gray-100">Akcije</th>
                </tr>
              </thead>
              <tbody>
                {loading ? (
                  <tr><td colSpan={7} className="text-center text-gray-400 py-12 text-sm">Učitavanje...</td></tr>
                ) : filtriraneKarte.length === 0 ? (
                  <tr><td colSpan={7} className="text-center text-gray-400 py-12 text-sm">Nema karata</td></tr>
                ) : (
                  filtriraneKarte.map((k) => (
                    <tr key={k.id} className="hover:bg-[#fafbff] transition">
                      <td className="px-5 py-4">
                        <span className="bg-[#f8f9fc] border border-gray-200 rounded-lg px-3 py-1 text-xs text-[#1a237e] font-medium">
                          {k.brojSedista}
                        </span>
                      </td>
                      <td className="px-5 py-4">
                        <span className={`text-xs px-3 py-1 rounded-full ${getTipBadge(k.tip)}`}>
                          {k.tip}
                        </span>
                      </td>
                      <td className="px-5 py-4 text-sm font-medium text-[#1a237e]">{k.finalnaCena} RSD</td>
                      <td className="px-5 py-4 text-sm text-gray-500">{formatDatum(k.datumIzdavanja)}</td>
                      <td className="px-5 py-4 text-sm text-gray-500">#{k.rezervacijaId}</td>
                      <td className="px-5 py-4 text-sm text-gray-600">{getPutovanjeNaziv(k.putovanjeId)}</td>
                      <td className="px-5 py-4">
                        <button
                          onClick={() => handleObrisi(k.id)}
                          className="bg-[#fce8e8] text-[#c62828] border-none rounded-lg px-3 py-1.5 text-xs cursor-pointer hover:bg-[#f5c6c6] transition"
                        >
                          Obriši
                        </button>
                      </td>
                    </tr>
                  ))
                )}
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
  );
}