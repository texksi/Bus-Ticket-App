import { useState, useEffect } from "react";
import Navbar from "../components/layout/Navbar";
import AdminSidebar from "../components/layout/AdminSidebar";
import api from "../api/api";

export default function AdminOcenePage() {
  const [ocene, setOcene] = useState([]);
  const [korisnici, setKorisnici] = useState([]);
  const [putovanja, setPutovanja] = useState([]);
  const [gradovi, setGradovi] = useState([]);
  const [loading, setLoading] = useState(true);
  const [pretraga, setPretraga] = useState("");
  const [filterOcena, setFilterOcena] = useState("");

  useEffect(() => {
    Promise.all([
      api.get("/api/ocene"),
      api.get("/api/korisnici"),
      api.get("/api/putovanja"),
      api.get("/api/gradovi"),
    ]).then(([o, k, p, g]) => {
      setOcene(o.data);
      setKorisnici(k.data);
      setPutovanja(p.data);
      setGradovi(g.data);
    }).finally(() => setLoading(false));
  }, []);

  const getKorisnikNaziv = (id) => {
    const k = korisnici.find((k) => k.id === Number(id));
    return k ? `${k.ime} ${k.prezime}` : "";
  };

  const getGradNaziv = (id) => gradovi.find((g) => g.id === Number(id))?.naziv || "";

  const getPutovanjeNaziv = (id) => {
    const p = putovanja.find((p) => p.id === Number(id));
    if (!p) return "";
    return `${getGradNaziv(p.polazisteId)} → ${getGradNaziv(p.odredisteId)}`;
  };

  const getZvezdice = (ocena) => "★".repeat(ocena) + "☆".repeat(5 - ocena);

  const handleObrisi = async (id) => {
    if (!window.confirm("Da li ste sigurni da želite da obrišete ovu ocenu?")) return;
    try {
      await api.delete(`/api/ocene/${id}`);
      setOcene(ocene.filter((o) => o.id !== id));
    } catch (err) {
      console.error(err);
    }
  };

  const filtriraneOcene = ocene.filter((o) => {
    const korisnikMatch = getKorisnikNaziv(o.korisnikId).toLowerCase().includes(pretraga.toLowerCase());
    const ocenaMatch = filterOcena ? o.ocena === Number(filterOcena) : true;
    return korisnikMatch && ocenaMatch;
  });

  return (
    <div className="flex min-h-screen bg-[#f8f9fc]">
      <AdminSidebar />
      <div className="flex-1 flex flex-col">
        <Navbar />
        <div className="flex-1 p-8">
          <div className="mb-6">
            <h1 className="text-[#1a237e] text-xl font-medium">Ocene</h1>
            <p className="text-gray-400 text-sm mt-0.5">Pregled ocena i komentara putnika</p>
          </div>

          <div className="flex gap-3 mb-4">
            <input
              type="text"
              placeholder="🔍 Pretraži po korisniku..."
              value={pretraga}
              onChange={(e) => setPretraga(e.target.value)}
              className="flex-1 border border-gray-200 rounded-xl px-4 py-2.5 text-sm text-[#1a237e] outline-none focus:border-[#1565c0]"
            />
            <select
              value={filterOcena}
              onChange={(e) => setFilterOcena(e.target.value)}
              className="border border-gray-200 rounded-xl px-4 py-2.5 text-sm text-[#666] outline-none cursor-pointer"
            >
              <option value="">Sve ocene</option>
              <option value="5">★★★★★</option>
              <option value="4">★★★★☆</option>
              <option value="3">★★★☆☆</option>
              <option value="2">★★☆☆☆</option>
              <option value="1">★☆☆☆☆</option>
            </select>
          </div>

          <div className="bg-white rounded-2xl border border-gray-100 overflow-hidden">
            <table className="w-full border-collapse">
              <thead>
                <tr className="bg-[#f8f9fc]">
                  <th className="text-left text-xs text-gray-400 font-medium uppercase tracking-wide px-5 py-3 border-b border-gray-100">Korisnik</th>
                  <th className="text-left text-xs text-gray-400 font-medium uppercase tracking-wide px-5 py-3 border-b border-gray-100">Putovanje</th>
                  <th className="text-left text-xs text-gray-400 font-medium uppercase tracking-wide px-5 py-3 border-b border-gray-100">Ocena</th>
                  <th className="text-left text-xs text-gray-400 font-medium uppercase tracking-wide px-5 py-3 border-b border-gray-100">Komentar</th>
                  <th className="text-left text-xs text-gray-400 font-medium uppercase tracking-wide px-5 py-3 border-b border-gray-100">Akcije</th>
                </tr>
              </thead>
              <tbody>
                {loading ? (
                  <tr><td colSpan={5} className="text-center text-gray-400 py-12 text-sm">Učitavanje...</td></tr>
                ) : filtriraneOcene.length === 0 ? (
                  <tr><td colSpan={5} className="text-center text-gray-400 py-12 text-sm">Nema ocena</td></tr>
                ) : (
                  filtriraneOcene.map((o) => (
                    <tr key={o.id} className="hover:bg-[#fafbff] transition">
                      <td className="px-5 py-4 text-sm font-medium text-[#1a237e]">{getKorisnikNaziv(o.korisnikId)}</td>
                      <td className="px-5 py-4 text-sm text-gray-500">{getPutovanjeNaziv(o.putovanjeId)}</td>
                      <td className="px-5 py-4 text-yellow-500 text-sm">{getZvezdice(o.ocena)}</td>
                      <td className="px-5 py-4 text-sm text-gray-500 max-w-xs truncate">{o.komentar}</td>
                      <td className="px-5 py-4">
                        <button
                          onClick={() => handleObrisi(o.id)}
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