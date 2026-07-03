import { useState, useEffect } from "react";
import Navbar from "../components/layout/Navbar";
import AdminSidebar from "../components/layout/AdminSidebar";
import api from "../api/api";

export default function AdminGradoviPage() {
  const [gradovi, setGradovi] = useState([]);
  const [loading, setLoading] = useState(true);
  const [pretraga, setPretraga] = useState("");
  const [modalOtvoren, setModalOtvoren] = useState(false);
  const [editGrad, setEditGrad] = useState(null);
  const [forma, setForma] = useState({ naziv: "", skracenica: "" });
  const [greska, setGreska] = useState(null);

  useEffect(() => {
    api.get("/api/gradovi")
      .then((res) => setGradovi(res.data))
      .finally(() => setLoading(false));
  }, []);

  const handleOtvoriModal = (grad = null) => {
    if (grad) {
      setEditGrad(grad);
      setForma({ naziv: grad.naziv, skracenica: grad.skracenica });
    } else {
      setEditGrad(null);
      setForma({ naziv: "", skracenica: "" });
    }
    setGreska(null);
    setModalOtvoren(true);
  };

  const handleSacuvaj = async () => {
    try {
      if (editGrad) {
        await api.put(`/api/gradovi/${editGrad.id}`, forma);
      } else {
        await api.post("/api/gradovi", forma);
      }
      const res = await api.get("/api/gradovi");
      setGradovi(res.data);
      setModalOtvoren(false);
    } catch (err) {
      setGreska(err.response?.data?.message || "Greška pri čuvanju grada");
    }
  };

  const handleObrisi = async (id) => {
    if (!window.confirm("Da li ste sigurni da želite da obrišete ovaj grad?")) return;
    try {
      await api.delete(`/api/gradovi/${id}`);
      setGradovi(gradovi.filter((g) => g.id !== id));
    } catch (err) {
      console.error(err);
    }
  };

  const filtriraniGradovi = gradovi.filter((g) =>
    g.naziv?.toLowerCase().includes(pretraga.toLowerCase()) ||
    g.skracenica?.toLowerCase().includes(pretraga.toLowerCase())
  );

  return (
    <div className="flex min-h-screen bg-[#f8f9fc]">
      <AdminSidebar />

      <div className="flex-1 flex flex-col">
        <Navbar />

        <div className="px-12 py-8">
          {/* HEADER */}
          <div className="flex justify-between items-center mb-6">
            <div>
              <h1 className="text-[#1a237e] text-xl font-medium">Gradovi</h1>
              <p className="text-gray-400 text-sm mt-0.5">Upravljanje gradovima i destinacijama</p>
            </div>
            <button
              onClick={() => handleOtvoriModal()}
              className="bg-[#ffa726] hover:bg-[#fb8c00] text-white border-none rounded-xl px-5 py-2.5 text-sm font-medium cursor-pointer transition"
            >
              + Dodaj grad
            </button>
          </div>

          {/* TOOLBAR */}
          <div className="mb-6">
            <input
              type="text"
              placeholder="🔍 Pretraži gradove..."
              value={pretraga}
              onChange={(e) => setPretraga(e.target.value)}
              className="w-full border border-gray-200 rounded-xl px-4 py-2.5 text-sm text-[#1a237e] outline-none focus:border-[#1565c0]"
            />
          </div>

          {/* GRID */}
          {loading ? (
            <div className="text-center text-gray-400 py-12">Učitavanje...</div>
          ) : filtriraniGradovi.length === 0 ? (
            <div className="text-center text-gray-400 py-12">Nema gradova</div>
          ) : (
            <div className="grid grid-cols-4 gap-4">
              {filtriraniGradovi.map((g) => (
                <div
                  key={g.id}
                  className="bg-white rounded-2xl p-5 border border-gray-100 flex flex-col gap-3 hover:border-[#1565c0] hover:shadow-md transition"
                >
                  <div className="w-11 h-11 bg-[#e8f0fe] rounded-xl flex items-center justify-center text-xl">
                    📍
                  </div>
                  <div className="text-[#1a237e] text-sm font-medium">{g.naziv}</div>
                  <span className="inline-block bg-[#f8f9fc] border border-gray-200 rounded-lg px-3 py-1 text-xs text-gray-500 w-fit">
                    {g.skracenica}
                  </span>
                  <div className="flex gap-2 mt-1">
                    <button
                      onClick={() => handleOtvoriModal(g)}
                      className="flex-1 bg-[#e8f0fe] text-[#1565c0] border-none rounded-lg py-1.5 text-xs cursor-pointer hover:bg-[#c8d8f8] transition"
                    >
                      Izmeni
                    </button>
                    <button
                      onClick={() => handleObrisi(g.id)}
                      className="flex-1 bg-[#fce8e8] text-[#c62828] border-none rounded-lg py-1.5 text-xs cursor-pointer hover:bg-[#f5c6c6] transition"
                    >
                      Obriši
                    </button>
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>
      </div>

      {/* MODAL */}
      {modalOtvoren && (
        <div className="fixed inset-0 bg-black/35 flex items-center justify-center z-50">
          <div className="bg-white rounded-2xl w-96 p-8">
            <div className="flex justify-between items-center mb-6">
              <h2 className="text-[#1a237e] text-base font-medium">
                {editGrad ? "Izmeni grad" : "Dodaj grad"}
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
                <label className="text-xs text-gray-500 font-medium">Naziv grada</label>
                <input
                  type="text"
                  placeholder="npr. Beograd"
                  value={forma.naziv}
                  onChange={(e) => setForma({ ...forma, naziv: e.target.value })}
                  className="border border-gray-200 rounded-xl px-4 py-2.5 text-sm text-[#1a237e] outline-none focus:border-[#1565c0]"
                />
              </div>
              <div className="flex flex-col gap-1">
                <label className="text-xs text-gray-500 font-medium">Skracenica</label>
                <input
                  type="text"
                  placeholder="npr. BG"
                  value={forma.skracenica}
                  onChange={(e) => setForma({ ...forma, skracenica: e.target.value })}
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