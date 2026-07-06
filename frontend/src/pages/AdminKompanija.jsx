import { useState, useEffect } from "react";
import Navbar from "../components/layout/Navbar";
import AdminSidebar from "../components/layout/AdminSidebar";
import api from "../api/api";

export default function AdminKompanijaPage() {
  const [kompanije, setKompanije] = useState([]);
  const [loading, setLoading] = useState(true);
  const [pretraga, setPretraga] = useState("");
  const [modalOtvoren, setModalOtvoren] = useState(false);
  const [editKompanija, setEditKompanija] = useState(null);
  const [forma, setForma] = useState({ naziv: "", kontakt: "" });
  const [greska, setGreska] = useState(null);

  useEffect(() => {
    api.get("/api/kompanije")
      .then((res) => setKompanije(res.data))
      .finally(() => setLoading(false));
  }, []);

  const handleOtvoriModal = (kompanija = null) => {
    if (kompanija) {
      setEditKompanija(kompanija);
      setForma({ naziv: kompanija.naziv, kontakt: kompanija.kontakt });
    } else {
      setEditKompanija(null);
      setForma({ naziv: "", kontakt: "" });
    }
    setGreska(null);
    setModalOtvoren(true);
  };

  const handleSacuvaj = async () => {
    try {
      if (editKompanija) {
        await api.put(`/api/kompanije/${editKompanija.id}`, forma);
      } else {
        await api.post("/api/kompanije", forma);
      }
      const res = await api.get("/api/kompanije");
      setKompanije(res.data);
      setModalOtvoren(false);
    } catch (err) {
      setGreska(err.response?.data?.message || "Greška pri čuvanju kompanije");
    }
  };

  const handleObrisi = async (id) => {
    if (!window.confirm("Da li ste sigurni da želite da obrišete ovu kompaniju?")) return;
    try {
      await api.delete(`/api/kompanije/${id}`);
      setKompanije(kompanije.filter((k) => k.id !== id));
    } catch (err) {
      console.error(err);
    }
  };

  const filtriraneKompanije = kompanije.filter((k) =>
    k.naziv?.toLowerCase().includes(pretraga.toLowerCase()) ||
    k.kontakt?.toLowerCase().includes(pretraga.toLowerCase())
  );

  return (
    <div className="flex min-h-screen bg-[#f8f9fc]">
      <AdminSidebar />

      <div className="flex-1 flex flex-col">
        <Navbar />

        <div className="flex-1 p-8">
          <div className="flex justify-between items-center mb-6">
            <div>
              <h1 className="text-[#1a237e] text-xl font-medium">Kompanije</h1>
              <p className="text-gray-400 text-sm mt-0.5">Upravljanje prevozničkim kompanijama</p>
            </div>
            <button
              onClick={() => handleOtvoriModal()}
              className="bg-[#ffa726] hover:bg-[#fb8c00] text-white border-none rounded-xl px-5 py-2.5 text-sm font-medium cursor-pointer transition"
            >
              + Dodaj kompaniju
            </button>
          </div>

          <div className="mb-4">
            <input
              type="text"
              placeholder="🔍 Pretraži kompanije..."
              value={pretraga}
              onChange={(e) => setPretraga(e.target.value)}
              className="w-full border border-gray-200 rounded-xl px-4 py-2.5 text-sm text-[#1a237e] outline-none focus:border-[#1565c0]"
            />
          </div>

          <div className="bg-white rounded-2xl border border-gray-100 overflow-hidden">
            <table className="w-full border-collapse">
              <thead>
                <tr className="bg-[#f8f9fc]">
                  <th className="text-left text-xs text-gray-400 font-medium uppercase tracking-wide px-5 py-3 border-b border-gray-100">Naziv</th>
                  <th className="text-left text-xs text-gray-400 font-medium uppercase tracking-wide px-5 py-3 border-b border-gray-100">Kontakt</th>
                  <th className="text-left text-xs text-gray-400 font-medium uppercase tracking-wide px-5 py-3 border-b border-gray-100">Akcije</th>
                </tr>
              </thead>
              <tbody>
                {loading ? (
                  <tr><td colSpan={3} className="text-center text-gray-400 py-12 text-sm">Učitavanje...</td></tr>
                ) : filtriraneKompanije.length === 0 ? (
                  <tr><td colSpan={3} className="text-center text-gray-400 py-12 text-sm">Nema kompanija</td></tr>
                ) : (
                  filtriraneKompanije.map((k) => (
                    <tr key={k.id} className="hover:bg-[#fafbff] transition">
                      <td className="px-5 py-4">
                        <div className="flex items-center gap-3">
                          <div className="w-9 h-9 bg-[#e8f0fe] rounded-xl flex items-center justify-center text-sm font-medium text-[#1565c0]">
                            {k.naziv?.charAt(0).toUpperCase()}
                          </div>
                          <span className="text-[#1a237e] font-medium text-sm">{k.naziv}</span>
                        </div>
                      </td>
                      <td className="px-5 py-4 text-sm text-gray-500">{k.kontakt}</td>
                      <td className="px-5 py-4">
                        <div className="flex gap-2">
                          <button
                            onClick={() => handleOtvoriModal(k)}
                            className="bg-[#e8f0fe] text-[#1565c0] border-none rounded-lg px-3 py-1.5 text-xs cursor-pointer hover:bg-[#c8d8f8] transition"
                          >
                            Izmeni
                          </button>
                          <button
                            onClick={() => handleObrisi(k.id)}
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

      {modalOtvoren && (
        <div className="fixed inset-0 bg-black/35 flex items-center justify-center z-50">
          <div className="bg-white rounded-2xl w-96 p-8">
            <div className="flex justify-between items-center mb-6">
              <h2 className="text-[#1a237e] text-base font-medium">
                {editKompanija ? "Izmeni kompaniju" : "Dodaj kompaniju"}
              </h2>
              <button
                onClick={() => setModalOtvoren(false)}
                className="bg-gray-100 border-none rounded-lg w-8 h-8 cursor-pointer text-gray-500 hover:bg-gray-200 transition"
              >
                ✕
              </button>
            </div>

            {greska && (
              <div className="bg-red-50 border border-red-200 text-red-600 text-xs rounded-xl px-4 py-3 mb-4">
                {greska}
              </div>
            )}

            <div className="flex flex-col gap-4">
              <div className="flex flex-col gap-1">
                <label className="text-xs text-gray-500 font-medium">Naziv kompanije</label>
                <input
                  type="text"
                  placeholder="npr. Niš Ekspres"
                  value={forma.naziv}
                  onChange={(e) => setForma({ ...forma, naziv: e.target.value })}
                  className="border border-gray-200 rounded-xl px-4 py-2.5 text-sm text-[#1a237e] outline-none focus:border-[#1565c0]"
                />
              </div>
              <div className="flex flex-col gap-1">
                <label className="text-xs text-gray-500 font-medium">Kontakt</label>
                <input
                  type="text"
                  placeholder="npr. info@nisekspres.rs"
                  value={forma.kontakt}
                  onChange={(e) => setForma({ ...forma, kontakt: e.target.value })}
                  className="border border-gray-200 rounded-xl px-4 py-2.5 text-sm text-[#1a237e] outline-none focus:border-[#1565c0]"
                />
              </div>
            </div>

            <div className="flex justify-end gap-3 mt-6">
              <button
                onClick={() => setModalOtvoren(false)}
                className="bg-gray-100 text-gray-500 border-none rounded-xl px-5 py-2.5 text-sm cursor-pointer hover:bg-gray-200 transition"
              >
                Otkaži
              </button>
              <button
                onClick={handleSacuvaj}
                className="bg-[#1565c0] hover:bg-[#0d47a1] text-white border-none rounded-xl px-5 py-2.5 text-sm font-medium cursor-pointer transition"
              >
                Sačuvaj
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}