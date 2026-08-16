import { useState, useEffect } from "react";
import Navbar from "../components/layout/Navbar";
import AdminSidebar from "../components/layout/AdminSidebar";
import api from "../api/api";

export default function AdminVozilaPage() {
  const [vozila, setVozila] = useState([]);
  const [kompanije, setKompanije] = useState([]);
  const [loading, setLoading] = useState(true);
  const [pretraga, setPretraga] = useState("");
  const [modalOtvoren, setModalOtvoren] = useState(false);
  const [editVozilo, setEditVozilo] = useState(null);
  const [forma, setForma] = useState({
    registracija: "",
    brojRedova: "",
    brojKolona: "",
    kompanijaId: "",
  });
  const [greska, setGreska] = useState(null);
  const [poruka, setPoruka] = useState(null);

  useEffect(() => {
    Promise.all([
      api.get("/api/vozila"),
      api.get("/api/kompanije"),
    ]).then(([v, k]) => {
      setVozila(v.data);
      setKompanije(k.data);
    }).finally(() => setLoading(false));
  }, []);

  const getKompanijaNaziv = (id) => kompanije.find((k) => k.id === Number(id))?.naziv || "";

  const handleOtvoriModal = (vozilo = null) => {
    if (vozilo) {
      setEditVozilo(vozilo);
      setForma({
        registracija: vozilo.registracija,
        brojRedova: String(vozilo.brojRedova),
        brojKolona: String(vozilo.brojKolona),
        kompanijaId: String(vozilo.kompanijaId),
      });
    } else {
      setEditVozilo(null);
      setForma({ registracija: "", brojRedova: "", brojKolona: "", kompanijaId: "" });
    }
    setGreska(null);
    setPoruka(null);
    setModalOtvoren(true);
  };

  const handleSacuvaj = async () => {
    try {
      const payload = {
        registracija: forma.registracija,
        brojRedova: Number(forma.brojRedova),
        brojKolona: Number(forma.brojKolona),
        kapacitet: Number(forma.brojRedova) * Number(forma.brojKolona),
        kompanijaId: Number(forma.kompanijaId),
      };
      if (editVozilo) {
        await api.put(`/api/vozila/${editVozilo.id}`, payload);
      } else {
        await api.post("/api/vozila", payload);
      }
      const res = await api.get("/api/vozila");
      setVozila(res.data);
      setPoruka("Sistem je zapamtio vozilo.");
      setTimeout(() => {
        setModalOtvoren(false);
        setPoruka(null);
      }, 1500);
    } catch (err) {
      const errorCode = err.response?.data?.errorCode;
      if (errorCode === "ERR_VALIDATION" || errorCode === "ERR_ENTITY_EXISTS") {
        setGreska(err.response?.data?.message);
      } else {
        setGreska("Sistem ne može da zapamti vozilo.");
      }
    }
  };

  const handleObrisi = async (id) => {
    if (!window.confirm("Da li ste sigurni da želite da obrišete ovo vozilo?")) return;
    try {
      await api.delete(`/api/vozila/${id}`);
      setVozila(vozila.filter((v) => v.id !== id));
    } catch (err) {
      console.error(err);
    }
  };

  const filtriranaVozila = vozila.filter((v) =>
    v.registracija?.toLowerCase().includes(pretraga.toLowerCase()) ||
    getKompanijaNaziv(v.kompanijaId).toLowerCase().includes(pretraga.toLowerCase())
  );

  return (
    <div className="flex min-h-screen bg-[#f8f9fc]">
      <AdminSidebar />

      <div className="flex-1 flex flex-col">
        <Navbar />

        <div className="flex-1 p-8">
          <div className="flex justify-between items-center mb-6">
            <div>
              <h1 className="text-[#1a237e] text-xl font-medium">Vozila</h1>
              <p className="text-gray-400 text-sm mt-0.5">Upravljanje voznim parkom</p>
            </div>
            <button
              onClick={() => handleOtvoriModal()}
              className="bg-[#ffa726] hover:bg-[#fb8c00] text-white border-none rounded-xl px-5 py-2.5 text-sm font-medium cursor-pointer transition"
            >
              + Dodaj vozilo
            </button>
          </div>

          <div className="mb-4">
            <input
              type="text"
              placeholder="🔍 Pretraži vozila..."
              value={pretraga}
              onChange={(e) => setPretraga(e.target.value)}
              className="w-full border border-gray-200 rounded-xl px-4 py-2.5 text-sm text-[#1a237e] outline-none focus:border-[#1565c0]"
            />
          </div>

          <div className="bg-white rounded-2xl border border-gray-100 overflow-hidden">
            <table className="w-full border-collapse">
              <thead>
                <tr className="bg-[#f8f9fc]">
                  <th className="text-left text-xs text-gray-400 font-medium uppercase tracking-wide px-5 py-3 border-b border-gray-100">Registracija</th>
                  <th className="text-left text-xs text-gray-400 font-medium uppercase tracking-wide px-5 py-3 border-b border-gray-100">Kapacitet</th>
                  <th className="text-left text-xs text-gray-400 font-medium uppercase tracking-wide px-5 py-3 border-b border-gray-100">Raspored sedišta</th>
                  <th className="text-left text-xs text-gray-400 font-medium uppercase tracking-wide px-5 py-3 border-b border-gray-100">Kompanija</th>
                  <th className="text-left text-xs text-gray-400 font-medium uppercase tracking-wide px-5 py-3 border-b border-gray-100">Akcije</th>
                </tr>
              </thead>
              <tbody>
                {loading ? (
                  <tr><td colSpan={5} className="text-center text-gray-400 py-12 text-sm">Učitavanje...</td></tr>
                ) : filtriranaVozila.length === 0 ? (
                  <tr><td colSpan={5} className="text-center text-gray-400 py-12 text-sm">Nema vozila</td></tr>
                ) : (
                  filtriranaVozila.map((v) => (
                    <tr key={v.id} className="hover:bg-[#fafbff] transition">
                      <td className="px-5 py-4">
                        <span className="bg-[#f8f9fc] border border-gray-200 rounded-lg px-3 py-1 text-xs text-[#1a237e] font-medium">
                          {v.registracija}
                        </span>
                      </td>
                      <td className="px-5 py-4">
                        <span className="text-[#1a237e] font-medium text-sm">{v.kapacitet}</span>
                        <span className="text-gray-400 text-xs ml-1">mesta</span>
                      </td>
                      <td className="px-5 py-4 text-xs text-gray-400">
                        {v.brojRedova} redova × {v.brojKolona} kolone
                      </td>
                      <td className="px-5 py-4">
                        <span className="bg-[#e8f0fe] text-[#1565c0] text-xs px-3 py-1 rounded-full">
                          {getKompanijaNaziv(v.kompanijaId)}
                        </span>
                      </td>
                      <td className="px-5 py-4">
                        <div className="flex gap-2">
                          <button
                            onClick={() => handleOtvoriModal(v)}
                            className="bg-[#e8f0fe] text-[#1565c0] border-none rounded-lg px-3 py-1.5 text-xs cursor-pointer hover:bg-[#c8d8f8] transition"
                          >
                            Izmeni
                          </button>
                          <button
                            onClick={() => handleObrisi(v.id)}
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
          <div className="bg-white rounded-2xl w-[480px] p-8">
            <div className="flex justify-between items-center mb-6">
              <h2 className="text-[#1a237e] text-base font-medium">
                {editVozilo ? "Izmeni vozilo" : "Dodaj vozilo"}
              </h2>
              <button
                onClick={() => { setModalOtvoren(false); setPoruka(null); }}
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

            {poruka && (
              <div className="bg-green-50 border border-green-200 text-green-700 text-xs rounded-xl px-4 py-3 mb-4">
                ✓ {poruka}
              </div>
            )}

            <div className="grid grid-cols-2 gap-4">
              <div className="flex flex-col gap-1 col-span-2">
                <label className="text-xs text-gray-500 font-medium">Registracija</label>
                <input
                  type="text"
                  placeholder="npr. BG234-AB"
                  value={forma.registracija}
                  onChange={(e) => setForma({ ...forma, registracija: e.target.value })}
                  className="border border-gray-200 rounded-xl px-4 py-2.5 text-sm text-[#1a237e] outline-none focus:border-[#1565c0]"
                />
              </div>
              <div className="flex flex-col gap-1">
                <label className="text-xs text-gray-500 font-medium">Broj redova</label>
                <input
                  type="number"
                  placeholder="npr. 8"
                  value={forma.brojRedova}
                  onChange={(e) => setForma({ ...forma, brojRedova: e.target.value })}
                  className="border border-gray-200 rounded-xl px-4 py-2.5 text-sm text-[#1a237e] outline-none focus:border-[#1565c0]"
                />
              </div>
              <div className="flex flex-col gap-1">
                <label className="text-xs text-gray-500 font-medium">Broj kolona</label>
                <input
                  type="number"
                  placeholder="npr. 4"
                  value={forma.brojKolona}
                  onChange={(e) => setForma({ ...forma, brojKolona: e.target.value })}
                  className="border border-gray-200 rounded-xl px-4 py-2.5 text-sm text-[#1a237e] outline-none focus:border-[#1565c0]"
                />
              </div>
              {forma.brojRedova && forma.brojKolona && (
                <div className="col-span-2 bg-[#e8f0fe] rounded-xl px-4 py-2.5 text-sm text-[#1565c0]">
                  Kapacitet: <strong>{Number(forma.brojRedova) * Number(forma.brojKolona)} mesta</strong>
                </div>
              )}
              <div className="flex flex-col gap-1 col-span-2">
                <label className="text-xs text-gray-500 font-medium">Kompanija</label>
                <select
                  value={forma.kompanijaId}
                  onChange={(e) => setForma({ ...forma, kompanijaId: e.target.value })}
                  className="border border-gray-200 rounded-xl px-4 py-2.5 text-sm text-[#1a237e] outline-none focus:border-[#1565c0]"
                >
                  <option value="">Izaberi kompaniju</option>
                  {kompanije.map((k) => <option key={k.id} value={k.id}>{k.naziv}</option>)}
                </select>
              </div>
            </div>

            <div className="flex justify-end gap-3 mt-6">
              <button
                onClick={() => { setModalOtvoren(false); setPoruka(null); }}
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