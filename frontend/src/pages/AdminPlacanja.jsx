import { useState, useEffect } from "react";
import Navbar from "../components/layout/Navbar";
import AdminSidebar from "../components/layout/AdminSidebar";
import api from "../api/api";

export default function AdminPlacanjaPage() {
  const [placanja, setPlacanja] = useState([]);
  const [rezervacije, setRezervacije] = useState([]);
  const [loading, setLoading] = useState(true);
  const [pretraga, setPretraga] = useState("");

  useEffect(() => {
    Promise.all([
      api.get("/api/placanja"),
      api.get("/api/rezervacije"),
    ]).then(([p, r]) => {
      setPlacanja(p.data);
      setRezervacije(r.data);
    }).finally(() => setLoading(false));
  }, []);

  const formatDatum = (dateStr) => {
    if (!dateStr) return "";
    return new Date(dateStr).toLocaleDateString("sr-RS");
  };

  const filtriranaPlacanja = placanja.filter((p) =>
    String(p.rezervacijaId).includes(pretraga) ||
    String(p.iznos).includes(pretraga)
  );

  return (
    <div className="flex min-h-screen bg-[#f8f9fc]">
      <AdminSidebar />
      <div className="flex-1 flex flex-col">
        <Navbar />
        <div className="flex-1 p-8">
          <div className="mb-6">
            <h1 className="text-[#1a237e] text-xl font-medium">Plaćanja</h1>
            <p className="text-gray-400 text-sm mt-0.5">Pregled svih plaćanja</p>
          </div>

          <div className="mb-4">
            <input
              type="text"
              placeholder="🔍 Pretraži po iznosu ili rezervaciji..."
              value={pretraga}
              onChange={(e) => setPretraga(e.target.value)}
              className="w-full border border-gray-200 rounded-xl px-4 py-2.5 text-sm text-[#1a237e] outline-none focus:border-[#1565c0]"
            />
          </div>

          <div className="bg-white rounded-2xl border border-gray-100 overflow-hidden">
            <table className="w-full border-collapse">
              <thead>
                <tr className="bg-[#f8f9fc]">
                  <th className="text-left text-xs text-gray-400 font-medium uppercase tracking-wide px-5 py-3 border-b border-gray-100">#</th>
                  <th className="text-left text-xs text-gray-400 font-medium uppercase tracking-wide px-5 py-3 border-b border-gray-100">Rezervacija</th>
                  <th className="text-left text-xs text-gray-400 font-medium uppercase tracking-wide px-5 py-3 border-b border-gray-100">Iznos</th>
                  <th className="text-left text-xs text-gray-400 font-medium uppercase tracking-wide px-5 py-3 border-b border-gray-100">Datum</th>
                  <th className="text-left text-xs text-gray-400 font-medium uppercase tracking-wide px-5 py-3 border-b border-gray-100">Stripe ID</th>
                </tr>
              </thead>
              <tbody>
                {loading ? (
                  <tr><td colSpan={5} className="text-center text-gray-400 py-12 text-sm">Učitavanje...</td></tr>
                ) : filtriranaPlacanja.length === 0 ? (
                  <tr><td colSpan={5} className="text-center text-gray-400 py-12 text-sm">Nema plaćanja</td></tr>
                ) : (
                  filtriranaPlacanja.map((p) => (
                    <tr key={p.id} className="hover:bg-[#fafbff] transition">
                      <td className="px-5 py-4 text-sm text-gray-400">#{p.id}</td>
                      <td className="px-5 py-4 text-sm text-[#1a237e] font-medium">#{p.rezervacijaId}</td>
                      <td className="px-5 py-4 text-sm font-medium text-[#1a237e]">{p.iznos} RSD</td>
                      <td className="px-5 py-4 text-sm text-gray-500">{formatDatum(p.datumPlacanja)}</td>
                      <td className="px-5 py-4 text-xs text-gray-400 font-mono">{p.stripePaymentId || "—"}</td>
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