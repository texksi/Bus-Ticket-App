import { useState, useEffect } from "react";
import Navbar from "../components/layout/Navbar";
import AdminSidebar from "../components/layout/AdminSidebar";
import api from "../api/api";

export default function AdminRezervacijePage() {
  const [rezervacije, setRezervacije] = useState([]);
  const [korisnici, setKorisnici] = useState([]);
  const [loading, setLoading] = useState(true);
  const [pretraga, setPretraga] = useState("");
  const [filterStatus, setFilterStatus] = useState("");

  useEffect(() => {
    Promise.all([
      api.get("/api/rezervacije"),
      api.get("/api/korisnici"),
    ]).then(([r, k]) => {
      setRezervacije(r.data);
      setKorisnici(k.data);
    }).finally(() => setLoading(false));
  }, []);

  const getKorisnikNaziv = (id) => {
    const k = korisnici.find((k) => k.id === Number(id));
    return k ? `${k.ime} ${k.prezime}` : "";
  };

  const formatDatum = (dateStr) => {
    if (!dateStr) return "";
    return new Date(dateStr).toLocaleDateString("sr-RS");
  };

  const getStatusBadge = (status) => {
    switch (status) {
      case "AKTIVNA": return "bg-green-50 text-green-700 border border-green-200";
      case "OTKAZANA": return "bg-red-50 text-red-600 border border-red-200";
      case "ZAVRSENA": return "bg-gray-100 text-gray-500 border border-gray-200";
      default: return "bg-blue-50 text-blue-600 border border-blue-200";
    }
  };

  const getNacinPlacanjaBadge = (nacin) => {
    switch (nacin) {
      case "KARTICA": return "💳";
      case "GOTOVINA": return "💵";
      case "ONLINE": return "🌐";
      default: return "💳";
    }
  };

  const handleObrisi = async (id) => {
    if (!window.confirm("Da li ste sigurni da želite da obrišete ovu rezervaciju?")) return;
    try {
      await api.delete(`/api/rezervacije/${id}`);
      setRezervacije(rezervacije.filter((r) => r.id !== id));
    } catch (err) {
      console.error(err);
    }
  };

  const handlePromeniStatus = async (rezervacija, noviStatus) => {
    try {
      await api.put(`/api/rezervacije/${rezervacija.id}`, {
        ...rezervacija,
        status: noviStatus,
      });
      const res = await api.get("/api/rezervacije");
      setRezervacije(res.data);
    } catch (err) {
      console.error(err);
    }
  };

  const filtriraneRezervacije = rezervacije.filter((r) => {
    const korisnikMatch = getKorisnikNaziv(r.korisnikId).toLowerCase().includes(pretraga.toLowerCase());
    const statusMatch = filterStatus ? r.status === filterStatus : true;
    return korisnikMatch && statusMatch;
  });

  return (
    <div className="flex min-h-screen bg-[#f8f9fc]">
      <AdminSidebar />

      <div className="flex-1 flex flex-col">
        <Navbar />

        <div className="flex-1 p-8">
          <div className="mb-6">
            <h1 className="text-[#1a237e] text-xl font-medium">Rezervacije</h1>
            <p className="text-gray-400 text-sm mt-0.5">Upravljanje svim rezervacijama</p>
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
              value={filterStatus}
              onChange={(e) => setFilterStatus(e.target.value)}
              className="border border-gray-200 rounded-xl px-4 py-2.5 text-sm text-[#666] outline-none cursor-pointer"
            >
              <option value="">Svi statusi</option>
              <option value="AKTIVNA">AKTIVNA</option>
              <option value="OTKAZANA">OTKAZANA</option>
              <option value="ZAVRSENA">ZAVRSENA</option>
            </select>
          </div>

          <div className="bg-white rounded-2xl border border-gray-100 overflow-hidden">
            <table className="w-full border-collapse">
              <thead>
                <tr className="bg-[#f8f9fc]">
                  <th className="text-left text-xs text-gray-400 font-medium uppercase tracking-wide px-5 py-3 border-b border-gray-100">#</th>
                  <th className="text-left text-xs text-gray-400 font-medium uppercase tracking-wide px-5 py-3 border-b border-gray-100">Korisnik</th>
                  <th className="text-left text-xs text-gray-400 font-medium uppercase tracking-wide px-5 py-3 border-b border-gray-100">Datum</th>
                  <th className="text-left text-xs text-gray-400 font-medium uppercase tracking-wide px-5 py-3 border-b border-gray-100">Iznos</th>
                  <th className="text-left text-xs text-gray-400 font-medium uppercase tracking-wide px-5 py-3 border-b border-gray-100">Plaćanje</th>
                  <th className="text-left text-xs text-gray-400 font-medium uppercase tracking-wide px-5 py-3 border-b border-gray-100">Status</th>
                  <th className="text-left text-xs text-gray-400 font-medium uppercase tracking-wide px-5 py-3 border-b border-gray-100">Akcije</th>
                </tr>
              </thead>
              <tbody>
                {loading ? (
                  <tr><td colSpan={7} className="text-center text-gray-400 py-12 text-sm">Učitavanje...</td></tr>
                ) : filtriraneRezervacije.length === 0 ? (
                  <tr><td colSpan={7} className="text-center text-gray-400 py-12 text-sm">Nema rezervacija</td></tr>
                ) : (
                  filtriraneRezervacije.map((r) => (
                    <tr key={r.id} className="hover:bg-[#fafbff] transition">
                      <td className="px-5 py-4 text-sm text-gray-400">#{r.id}</td>
                      <td className="px-5 py-4">
                        <div className="text-[#1a237e] text-sm font-medium">{getKorisnikNaziv(r.korisnikId)}</div>
                      </td>
                      <td className="px-5 py-4 text-sm text-gray-500">{formatDatum(r.datumKreiranja)}</td>
                      <td className="px-5 py-4 text-sm font-medium text-[#1a237e]">{r.ukupanIznos} RSD</td>
                      <td className="px-5 py-4 text-sm text-gray-500">
                        {getNacinPlacanjaBadge(r.nacinPlacanja)} {r.nacinPlacanja}
                      </td>
                      <td className="px-5 py-4">
                        <span className={`text-xs px-3 py-1 rounded-full ${getStatusBadge(r.status)}`}>
                          {r.status}
                        </span>
                      </td>
                      <td className="px-5 py-4">
                        <div className="flex gap-2">
                          {r.status === "AKTIVNA" && (
                            <>
                              <button
                                onClick={() => handlePromeniStatus(r, "ZAVRSENA")}
                                className="bg-green-50 text-green-700 border-none rounded-lg px-3 py-1.5 text-xs cursor-pointer hover:bg-green-100 transition"
                              >
                                Završi
                              </button>
                              <button
                                onClick={() => handlePromeniStatus(r, "OTKAZANA")}
                                className="bg-orange-50 text-orange-600 border-none rounded-lg px-3 py-1.5 text-xs cursor-pointer hover:bg-orange-100 transition"
                              >
                                Otkaži
                              </button>
                            </>
                          )}
                          <button
                            onClick={() => handleObrisi(r.id)}
                            className="bg-[#fce8e8] text-[#c62828] border-none rounded-lg px-3 py-1.5 text-xs cursor-pointer hover:bg-[#f5c6c6] transition"
                          >
                            Obriši
                          </button>
                        </div>
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