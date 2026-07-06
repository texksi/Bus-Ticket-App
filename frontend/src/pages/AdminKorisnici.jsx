import { useState, useEffect } from "react";
import Navbar from "../components/layout/Navbar";
import AdminSidebar from "../components/layout/AdminSidebar";
import api from "../api/api";

export default function AdminKorisniciPage() {
  const [korisnici, setKorisnici] = useState([]);
  const [loading, setLoading] = useState(true);
  const [pretraga, setPretraga] = useState("");
  const [filterRola, setFilterRola] = useState("");

  useEffect(() => {
    api.get("/api/korisnici")
      .then((res) => setKorisnici(res.data))
      .finally(() => setLoading(false));
  }, []);

  const handleObrisi = async (id) => {
    if (!window.confirm("Da li ste sigurni da želite da obrišete ovog korisnika?")) return;
    try {
      await api.delete(`/api/korisnici/${id}`);
      setKorisnici(korisnici.filter((k) => k.id !== id));
    } catch (err) {
      console.error(err);
    }
  };

  const handlePromeniRolu = async (korisnik) => {
    const novaRola = korisnik.role === "ADMIN" ? "USER" : "ADMIN";
    if (!window.confirm(`Da li želite da promenite rolu korisnika ${korisnik.username} na ${novaRola}?`)) return;
    try {
      await api.put(`/api/korisnici/${korisnik.id}/role?role=${novaRola}`);
      const res = await api.get("/api/korisnici");
      setKorisnici(res.data);
    } catch (err) {
      console.error(err);
    }
};

  const filtriraniKorisnici = korisnici.filter((k) => {
    const imeMatch = `${k.ime} ${k.prezime}`.toLowerCase().includes(pretraga.toLowerCase());
    const usernameMatch = k.username?.toLowerCase().includes(pretraga.toLowerCase());
    const emailMatch = k.email?.toLowerCase().includes(pretraga.toLowerCase());
    const rolaMatch = filterRola ? k.role === filterRola : true;
    return (imeMatch || usernameMatch || emailMatch) && rolaMatch;
  });

  const getInitials = (ime, prezime) => {
    return `${ime?.charAt(0) || ""}${prezime?.charAt(0) || ""}`.toUpperCase();
  };

  return (
    <div className="flex min-h-screen bg-[#f8f9fc]">
      <AdminSidebar />

      <div className="flex-1 flex flex-col">
        <Navbar />

        <div className="flex-1 p-8">
          <div className="mb-6">
            <h1 className="text-[#1a237e] text-xl font-medium">Korisnici</h1>
            <p className="text-gray-400 text-sm mt-0.5">Pregled i upravljanje korisničkim nalozima</p>
          </div>

          <div className="flex gap-3 mb-4">
            <input
              type="text"
              placeholder="🔍 Pretraži korisnike..."
              value={pretraga}
              onChange={(e) => setPretraga(e.target.value)}
              className="flex-1 border border-gray-200 rounded-xl px-4 py-2.5 text-sm text-[#1a237e] outline-none focus:border-[#1565c0]"
            />
            <select
              value={filterRola}
              onChange={(e) => setFilterRola(e.target.value)}
              className="border border-gray-200 rounded-xl px-4 py-2.5 text-sm text-[#666] outline-none cursor-pointer"
            >
              <option value="">Sve role</option>
              <option value="ADMIN">ADMIN</option>
              <option value="USER">USER</option>
            </select>
          </div>

          <div className="bg-white rounded-2xl border border-gray-100 overflow-hidden">
            <table className="w-full border-collapse">
              <thead>
                <tr className="bg-[#f8f9fc]">
                  <th className="text-left text-xs text-gray-400 font-medium uppercase tracking-wide px-5 py-3 border-b border-gray-100">Korisnik</th>
                  <th className="text-left text-xs text-gray-400 font-medium uppercase tracking-wide px-5 py-3 border-b border-gray-100">Email</th>
                  <th className="text-left text-xs text-gray-400 font-medium uppercase tracking-wide px-5 py-3 border-b border-gray-100">Rola</th>
                  <th className="text-left text-xs text-gray-400 font-medium uppercase tracking-wide px-5 py-3 border-b border-gray-100">Akcije</th>
                </tr>
              </thead>
              <tbody>
                {loading ? (
                  <tr><td colSpan={4} className="text-center text-gray-400 py-12 text-sm">Učitavanje...</td></tr>
                ) : filtriraniKorisnici.length === 0 ? (
                  <tr><td colSpan={4} className="text-center text-gray-400 py-12 text-sm">Nema korisnika</td></tr>
                ) : (
                  filtriraniKorisnici.map((k) => (
                    <tr key={k.id} className="hover:bg-[#fafbff] transition">
                      <td className="px-5 py-4">
                        <div className="flex items-center gap-3">
                          <div className="w-9 h-9 rounded-full bg-gradient-to-br from-[#1565c0] to-[#1a237e] flex items-center justify-center text-white text-xs font-medium flex-shrink-0">
                            {getInitials(k.ime, k.prezime)}
                          </div>
                          <div>
                            <div className="text-[#1a237e] text-sm font-medium">{k.ime} {k.prezime}</div>
                            <div className="text-gray-400 text-xs">@{k.username}</div>
                          </div>
                        </div>
                      </td>
                      <td className="px-5 py-4 text-sm text-gray-500">{k.email}</td>
                      <td className="px-5 py-4">
                        <span className={`text-xs px-3 py-1 rounded-full ${
                          k.role === "ADMIN"
                            ? "bg-[#fce8e8] text-[#c62828]"
                            : "bg-[#e8f0fe] text-[#1565c0]"
                        }`}>
                          {k.role}
                        </span>
                      </td>
                      <td className="px-5 py-4">
                        <div className="flex gap-2">
                          <button
                            onClick={() => handlePromeniRolu(k)}
                            className="bg-[#e8f0fe] text-[#1565c0] border-none rounded-lg px-3 py-1.5 text-xs cursor-pointer hover:bg-[#c8d8f8] transition"
                          >
                            Promeni rolu
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
    </div>
  );
}