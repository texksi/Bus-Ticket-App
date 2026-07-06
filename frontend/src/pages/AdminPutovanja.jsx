import { useState, useEffect } from "react";
import Navbar from "../components/layout/Navbar";
import AdminSidebar from "../components/layout/AdminSidebar";
import api from "../api/api";

export default function AdminPutovanjaPage() {
  const [putovanja, setPutovanja] = useState([]);
  const [gradovi, setGradovi] = useState([]);
  const [kompanije, setKompanije] = useState([]);
  const [vozila, setVozila] = useState([]);
  const [loading, setLoading] = useState(true);
  const [pretraga, setPretraga] = useState("");
  const [modalOtvoren, setModalOtvoren] = useState(false);
  const [editPutovanje, setEditPutovanje] = useState(null);
  const [forma, setForma] = useState({
    polazisteId: "",
    odredisteId: "",
    vremePolaska: "",
    vremeDolaska: "",
    osnovnaCena: "",
    kompanijaId: "",
    voziloId: "",
  });

 useEffect(() => {
  Promise.all([
    api.get("/api/putovanja"),
    api.get("/api/gradovi"),
    api.get("/api/kompanije"),
    api.get("/api/vozila"),
  ]).then(([put, grad, komp, voz]) => {
    console.log("putovanja:", put.data);
    console.log("gradovi:", grad.data);
    console.log("kompanije:", komp.data);
    console.log("vozila:", voz.data);
    setPutovanja(put.data);
    setGradovi(grad.data);
    setKompanije(komp.data);
    setVozila(voz.data);
  }).catch((e) => console.error("Greška:", e))
  .finally(() => setLoading(false));
}, []);

  const getGradNaziv = (id) => gradovi.find((g) => g.id === Number(id))?.naziv || "";
  const getKompanijaNaziv = (id) => kompanije.find((k) => k.id === Number(id))?.naziv || "";

  const formatVreme = (dateStr) => {
    if (!dateStr) return "";
    return new Date(dateStr).toLocaleTimeString("sr-RS", { hour: "2-digit", minute: "2-digit" });
  };

  const formatTrajanje = (polazak, dolazak) => {
    if (!polazak || !dolazak) return "";
    const diff = new Date(dolazak) - new Date(polazak);
    const sati = Math.floor(diff / 3600000);
    const minuti = Math.floor((diff % 3600000) / 60000);
    return `${sati}h ${minuti}min`;
  };

  const handleOtvoriModal = (putovanje = null) => {
    if (putovanje) {
      setEditPutovanje(putovanje);
      setForma({
        polazisteId: String(putovanje.polazisteId),
        odredisteId: String(putovanje.odredisteId),
        vremePolaska: putovanje.vremePolaska?.slice(0, 16) || "",
        vremeDolaska: putovanje.vremeDolaska?.slice(0, 16) || "",
        osnovnaCena: String(putovanje.osnovnaCena),
        kompanijaId: String(putovanje.kompanijaId),
        voziloId: String(putovanje.voziloId),
      });
    } else {
      setEditPutovanje(null);
      setForma({
        polazisteId: "",
        odredisteId: "",
        vremePolaska: "",
        vremeDolaska: "",
        osnovnaCena: "",
        kompanijaId: "",
        voziloId: "",
      });
    }
    setModalOtvoren(true);
  };

  const handleSacuvaj = async () => {
    try {
      const payload = {
        ...forma,
        polazisteId: Number(forma.polazisteId),
        odredisteId: Number(forma.odredisteId),
        kompanijaId: Number(forma.kompanijaId),
        voziloId: Number(forma.voziloId),
        osnovnaCena: Number(forma.osnovnaCena),
      };
      if (editPutovanje) {
        await api.put(`/api/putovanja/${editPutovanje.id}`, payload);
      } else {
        await api.post("/api/putovanja", payload);
      }
      const res = await api.get("/api/putovanja");
      setPutovanja(res.data);
      setModalOtvoren(false);
    } catch (err) {
      console.error(err);
    }
  };

  const handleObrisi = async (id) => {
    if (!window.confirm("Da li ste sigurni da želite da obrišete ovo putovanje?")) return;
    try {
      await api.delete(`/api/putovanja/${id}`);
      setPutovanja(putovanja.filter((p) => p.id !== id));
    } catch (err) {
      console.error(err);
    }
  };

  const filtriranaPutovanja = putovanja.filter((p) => {
    const polaziste = getGradNaziv(p.polazisteId).toLowerCase();
    const odrediste = getGradNaziv(p.odredisteId).toLowerCase();
    return polaziste.includes(pretraga.toLowerCase()) || odrediste.includes(pretraga.toLowerCase());
  });

  return (
    <div className="flex min-h-screen bg-[#f8f9fc]">
      <AdminSidebar />

      <div className="flex-1 flex flex-col">
        <Navbar />

        <div className="flex-1 p-8">
          <div className="flex justify-between items-center mb-6">
            <div>
              <h1 className="text-[#1a237e] text-xl font-medium">Putovanja</h1>
              <p className="text-gray-400 text-sm mt-0.5">Upravljanje svim putovanjima</p>
            </div>
            <button
              onClick={() => handleOtvoriModal()}
              className="bg-[#ffa726] hover:bg-[#fb8c00] text-white border-none rounded-xl px-5 py-2.5 text-sm font-medium cursor-pointer transition"
            >
              + Dodaj putovanje
            </button>
          </div>

          <div className="flex gap-3 mb-4">
            <input
              type="text"
              placeholder="🔍 Pretraži putovanja..."
              value={pretraga}
              onChange={(e) => setPretraga(e.target.value)}
              className="flex-1 border border-gray-200 rounded-xl px-4 py-2.5 text-sm text-[#1a237e] outline-none focus:border-[#1565c0]"
            />
          </div>

          <div className="bg-white rounded-2xl border border-gray-100 overflow-hidden">
            <table className="w-full border-collapse">
              <thead>
                <tr className="bg-[#f8f9fc]">
                  <th className="text-left text-xs text-gray-400 font-medium uppercase tracking-wide px-5 py-3 border-b border-gray-100">Ruta</th>
                  <th className="text-left text-xs text-gray-400 font-medium uppercase tracking-wide px-5 py-3 border-b border-gray-100">Polazak</th>
                  <th className="text-left text-xs text-gray-400 font-medium uppercase tracking-wide px-5 py-3 border-b border-gray-100">Dolazak</th>
                  <th className="text-left text-xs text-gray-400 font-medium uppercase tracking-wide px-5 py-3 border-b border-gray-100">Trajanje</th>
                  <th className="text-left text-xs text-gray-400 font-medium uppercase tracking-wide px-5 py-3 border-b border-gray-100">Cena</th>
                  <th className="text-left text-xs text-gray-400 font-medium uppercase tracking-wide px-5 py-3 border-b border-gray-100">Kompanija</th>
                  <th className="text-left text-xs text-gray-400 font-medium uppercase tracking-wide px-5 py-3 border-b border-gray-100">Akcije</th>
                </tr>
              </thead>
              <tbody>
                {loading ? (
                  <tr><td colSpan={7} className="text-center text-gray-400 py-12 text-sm">Učitavanje...</td></tr>
                ) : filtriranaPutovanja.length === 0 ? (
                  <tr><td colSpan={7} className="text-center text-gray-400 py-12 text-sm">Nema putovanja</td></tr>
                ) : (
                  filtriranaPutovanja.map((p) => (
                    <tr key={p.id} className="hover:bg-[#fafbff] transition">
                      <td className="px-5 py-4">
                        <div className="flex items-center gap-2">
                          <span className="text-[#1a237e] font-medium text-sm">{getGradNaziv(p.polazisteId)}</span>
                          <span className="text-[#b3c1e8] text-xs">→</span>
                          <span className="text-[#1a237e] font-medium text-sm">{getGradNaziv(p.odredisteId)}</span>
                        </div>
                      </td>
                      <td className="px-5 py-4 text-sm text-gray-600">{formatVreme(p.vremePolaska)}</td>
                      <td className="px-5 py-4 text-sm text-gray-600">{formatVreme(p.vremeDolaska)}</td>
                      <td className="px-5 py-4 text-sm text-gray-600">{formatTrajanje(p.vremePolaska, p.vremeDolaska)}</td>
                      <td className="px-5 py-4 text-sm font-medium text-[#1a237e]">{p.osnovnaCena} RSD</td>
                      <td className="px-5 py-4">
                        <span className="bg-[#e8f0fe] text-[#1565c0] text-xs px-3 py-1 rounded-full">
                          {getKompanijaNaziv(p.kompanijaId)}
                        </span>
                      </td>
                      <td className="px-5 py-4">
                        <div className="flex gap-2">
                          <button
                            onClick={() => handleOtvoriModal(p)}
                            className="bg-[#e8f0fe] text-[#1565c0] border-none rounded-lg px-3 py-1.5 text-xs cursor-pointer hover:bg-[#c8d8f8] transition"
                          >
                            Izmeni
                          </button>
                          <button
                            onClick={() => handleObrisi(p.id)}
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
          <div className="bg-white rounded-2xl w-[500px] p-8">
            <div className="flex justify-between items-center mb-6">
              <h2 className="text-[#1a237e] text-base font-medium">
                {editPutovanje ? "Izmeni putovanje" : "Dodaj putovanje"}
              </h2>
              <button
                onClick={() => setModalOtvoren(false)}
                className="bg-gray-100 border-none rounded-lg w-8 h-8 cursor-pointer text-gray-500 hover:bg-gray-200 transition"
              >
                ✕
              </button>
            </div>

            <div className="grid grid-cols-2 gap-4">
              <div className="flex flex-col gap-1">
                <label className="text-xs text-gray-500 font-medium">Polazište</label>
                <select
                  value={forma.polazisteId}
                  onChange={(e) => setForma({ ...forma, polazisteId: e.target.value })}
                  className="border border-gray-200 rounded-xl px-4 py-2.5 text-sm text-[#1a237e] outline-none focus:border-[#1565c0]"
                >
                  <option value="">Izaberi grad</option>
                  {gradovi.map((g) => <option key={g.id} value={g.id}>{g.naziv}</option>)}
                </select>
              </div>
              <div className="flex flex-col gap-1">
                <label className="text-xs text-gray-500 font-medium">Odredište</label>
                <select
                  value={forma.odredisteId}
                  onChange={(e) => setForma({ ...forma, odredisteId: e.target.value })}
                  className="border border-gray-200 rounded-xl px-4 py-2.5 text-sm text-[#1a237e] outline-none focus:border-[#1565c0]"
                >
                  <option value="">Izaberi grad</option>
                  {gradovi.map((g) => <option key={g.id} value={g.id}>{g.naziv}</option>)}
                </select>
              </div>
              <div className="flex flex-col gap-1">
                <label className="text-xs text-gray-500 font-medium">Vreme polaska</label>
                <input
                  type="datetime-local"
                  value={forma.vremePolaska}
                  onChange={(e) => setForma({ ...forma, vremePolaska: e.target.value })}
                  className="border border-gray-200 rounded-xl px-4 py-2.5 text-sm text-[#1a237e] outline-none focus:border-[#1565c0]"
                />
              </div>
              <div className="flex flex-col gap-1">
                <label className="text-xs text-gray-500 font-medium">Vreme dolaska</label>
                <input
                  type="datetime-local"
                  value={forma.vremeDolaska}
                  onChange={(e) => setForma({ ...forma, vremeDolaska: e.target.value })}
                  className="border border-gray-200 rounded-xl px-4 py-2.5 text-sm text-[#1a237e] outline-none focus:border-[#1565c0]"
                />
              </div>
              <div className="flex flex-col gap-1">
                <label className="text-xs text-gray-500 font-medium">Osnovna cena (RSD)</label>
                <input
                  type="number"
                  placeholder="npr. 500"
                  value={forma.osnovnaCena}
                  onChange={(e) => setForma({ ...forma, osnovnaCena: e.target.value })}
                  className="border border-gray-200 rounded-xl px-4 py-2.5 text-sm text-[#1a237e] outline-none focus:border-[#1565c0]"
                />
              </div>
              <div className="flex flex-col gap-1">
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
              <div className="flex flex-col gap-1 col-span-2">
                <label className="text-xs text-gray-500 font-medium">Vozilo</label>
                <select
                  value={forma.voziloId}
                  onChange={(e) => setForma({ ...forma, voziloId: e.target.value })}
                  className="border border-gray-200 rounded-xl px-4 py-2.5 text-sm text-[#1a237e] outline-none focus:border-[#1565c0]"
                >
                  <option value="">Izaberi vozilo</option>
                  {vozila.map((v) => <option key={v.id} value={v.id}>{v.registracija} ({v.kapacitet} mesta)</option>)}
                </select>
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