import { useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../../api/api";

export default function AuthForma({ mode = "login" }) {
  const navigate = useNavigate();
  const [aktivan, setAktivan] = useState(mode);
  const [error, setError] = useState(null);
  const [forma, setForma] = useState({
    ime: "",
    prezime: "",
    email: "",
    username: "",
    password: "",
  });

  const handleChange = (e) => {
    setForma({ ...forma, [e.target.name]: e.target.value });
  };

  const handleLogin = async (e) => {
    e.preventDefault();
    setError(null);
    try {
      const res = await api.post("/api/auth/login", {
        username: forma.username,
        password: forma.password,
      });
      localStorage.setItem("token", res.data.token);
      navigate("/");
    } catch (err) {
      setError(err.response?.data?.message || "Pogrešno korisničko ime ili lozinka");
    }
  };

  const handleRegister = async (e) => {
    e.preventDefault();
    setError(null);
    try {
      const res = await api.post("/api/auth/register", forma);
      localStorage.setItem("token", res.data.token);
      navigate("/");
    } catch (err) {
      setError(err.response?.data?.message || "Greška pri registraciji");
    }
  };

  return (
    <div className="bg-[#1249a0] min-h-screen flex items-center justify-center p-8">
      <div className="bg-white rounded-2xl w-full max-w-md overflow-hidden">

        {/* TOP */}
        <div
          className="relative px-8 py-10 overflow-hidden"
          style={{ background: "linear-gradient(135deg, #1a3a8f 0%, #1565c0 100%)" }}
        >
          <div className="absolute -right-10 -top-10 w-44 h-44 rounded-full bg-white/5" />
          <div className="absolute right-5 -bottom-16 w-32 h-32 rounded-full bg-white/4" />
          <div className="relative w-12 h-12 bg-white/15 rounded-2xl flex items-center justify-center text-2xl mb-4">
            🚌
          </div>
          <h1 className="relative text-white text-2xl font-medium m-0 mb-1">
            {aktivan === "login" ? "Dobrodošli" : "Novi nalog"}
          </h1>
          <p className="relative text-[#b3cef5] text-sm m-0">
            {aktivan === "login"
              ? "Prijavite se na vaš BusTicket nalog"
              : "Kreirajte vaš BusTicket nalog"}
          </p>
        </div>

        {/* TABS */}
        <div className="flex mx-8 border-b border-gray-100">
          <button
            onClick={() => { setAktivan("login"); setError(null); }}
            className={`flex-1 text-center py-3 text-sm border-b-2 transition cursor-pointer bg-transparent border-none ${
              aktivan === "login"
                ? "text-[#1565c0] border-[#1565c0] font-medium"
                : "text-gray-400 border-transparent"
            }`}
          >
            Prijava
          </button>
          <button
            onClick={() => { setAktivan("register"); setError(null); }}
            className={`flex-1 text-center py-3 text-sm border-b-2 transition cursor-pointer bg-transparent border-none ${
              aktivan === "register"
                ? "text-[#1565c0] border-[#1565c0] font-medium"
                : "text-gray-400 border-transparent"
            }`}
          >
            Registracija
          </button>
        </div>

        {/* FORM */}
        <div className="px-8 py-6">
          {error && (
            <div className="bg-red-50 border border-red-200 text-red-600 text-xs rounded-xl px-4 py-3 mb-4">
              {error}
            </div>
          )}

          {aktivan === "login" ? (
            <form onSubmit={handleLogin} className="flex flex-col gap-4">
              <div className="flex flex-col gap-1">
                <label className="text-xs text-gray-500 font-medium">Korisničko ime</label>
                <input
                  type="text"
                  name="username"
                  placeholder="Unesite korisničko ime"
                  value={forma.username}
                  onChange={handleChange}
                  className="border border-gray-200 rounded-xl px-4 py-2.5 text-sm text-[#1a237e] outline-none focus:border-[#1565c0]"
                />
              </div>
              <div className="flex flex-col gap-1">
                <label className="text-xs text-gray-500 font-medium">Lozinka</label>
                <input
                  type="password"
                  name="password"
                  placeholder="Unesite lozinku"
                  value={forma.password}
                  onChange={handleChange}
                  className="border border-gray-200 rounded-xl px-4 py-2.5 text-sm text-[#1a237e] outline-none focus:border-[#1565c0]"
                />
              </div>
              <button
                type="submit"
                className="w-full bg-[#1565c0] hover:bg-[#0d47a1] text-white rounded-xl py-3 text-sm font-medium border-none cursor-pointer transition mt-1"
              >
                Prijavite se
              </button>
              <p className="text-center text-xs text-gray-400 mt-1">
                Nemate nalog?{" "}
                <span
                  onClick={() => setAktivan("register")}
                  className="text-[#1565c0] font-medium cursor-pointer"
                >
                  Registrujte se
                </span>
              </p>
            </form>
          ) : (
            <form onSubmit={handleRegister} className="flex flex-col gap-4">
              <div className="grid grid-cols-2 gap-3">
                <div className="flex flex-col gap-1">
                  <label className="text-xs text-gray-500 font-medium">Ime</label>
                  <input
                    type="text"
                    name="ime"
                    placeholder="Vaše ime"
                    value={forma.ime}
                    onChange={handleChange}
                    className="border border-gray-200 rounded-xl px-4 py-2.5 text-sm text-[#1a237e] outline-none focus:border-[#1565c0]"
                  />
                </div>
                <div className="flex flex-col gap-1">
                  <label className="text-xs text-gray-500 font-medium">Prezime</label>
                  <input
                    type="text"
                    name="prezime"
                    placeholder="Vaše prezime"
                    value={forma.prezime}
                    onChange={handleChange}
                    className="border border-gray-200 rounded-xl px-4 py-2.5 text-sm text-[#1a237e] outline-none focus:border-[#1565c0]"
                  />
                </div>
              </div>
              <div className="flex flex-col gap-1">
                <label className="text-xs text-gray-500 font-medium">Email</label>
                <input
                  type="email"
                  name="email"
                  placeholder="vas@email.com"
                  value={forma.email}
                  onChange={handleChange}
                  className="border border-gray-200 rounded-xl px-4 py-2.5 text-sm text-[#1a237e] outline-none focus:border-[#1565c0]"
                />
              </div>
              <div className="flex flex-col gap-1">
                <label className="text-xs text-gray-500 font-medium">Korisničko ime</label>
                <input
                  type="text"
                  name="username"
                  placeholder="Izaberite korisničko ime"
                  value={forma.username}
                  onChange={handleChange}
                  className="border border-gray-200 rounded-xl px-4 py-2.5 text-sm text-[#1a237e] outline-none focus:border-[#1565c0]"
                />
              </div>
              <div className="flex flex-col gap-1">
                <label className="text-xs text-gray-500 font-medium">Lozinka</label>
                <input
                  type="password"
                  name="password"
                  placeholder="Minimum 8 karaktera"
                  value={forma.password}
                  onChange={handleChange}
                  className="border border-gray-200 rounded-xl px-4 py-2.5 text-sm text-[#1a237e] outline-none focus:border-[#1565c0]"
                />
              </div>
              <button
                type="submit"
                className="w-full bg-[#1565c0] hover:bg-[#0d47a1] text-white rounded-xl py-3 text-sm font-medium border-none cursor-pointer transition mt-1"
              >
                Registrujte se
              </button>
              <p className="text-center text-xs text-gray-400 mt-1">
                Već imate nalog?{" "}
                <span
                  onClick={() => setAktivan("login")}
                  className="text-[#1565c0] font-medium cursor-pointer"
                >
                  Prijavite se
                </span>
              </p>
            </form>
          )}
        </div>
      </div>
    </div>
  );
}