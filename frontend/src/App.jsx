import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import HomePage from "./pages/HomePage";
import LoginPage from "./pages/LoginPage";
import RegisterPage from "./pages/RegisterPage";
import ONamaPage from "./pages/ONamaPage";
import PutovanjaPage from "./pages/PutovanjaPage";
import AdminPutovanjaPage from "./pages/AdminPutovanja";
import AdminDashboardPage from "./pages/AdminDashboardPage";
import AdminKartePage from "./pages/AdminKarta";
import AdminGradoviPage from "./pages/AdminGradovi";
import AdminVozilaPage from "./pages/AdminVozila";
import AdminKompanijaPage from "./pages/AdminKompanija";
import AdminKorisniciPage from "./pages/AdminKorisnici";
import AdminRezervacijePage from "./pages/AdminRezervacije";
import AdminOcenePage from "./pages/AdminOcene";
import AdminPlacanjaPage from "./pages/AdminPlacanja";
import KorpaPage from "./pages/KorpaPage";
import MojeRezervacijePage from "./pages/MojeRezervacije";
import PlacanjePage from "./pages/PlacanjePage";

function ProtectedAdminRoute({ children }) {
  const token = localStorage.getItem("token");
  if (!token) return <Navigate to="/login" />;

  try {
    const payload = JSON.parse(atob(token.split(".")[1]));
    if (payload.role !== "ADMIN") return <Navigate to="/" />;
    return children;
  } catch {
    return <Navigate to="/login" />;
  }
}

export default function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<HomePage />} />
        <Route path="/login" element={<LoginPage />} />
        <Route path="/register" element={<RegisterPage />} />
        <Route path="/o-nama" element={<ONamaPage />} />
        <Route path="/putovanja" element={<PutovanjaPage />} />
        <Route path="/admin" element={<ProtectedAdminRoute><AdminDashboardPage /></ProtectedAdminRoute>} />
        <Route path="/admin/putovanja" element={<ProtectedAdminRoute><AdminPutovanjaPage /></ProtectedAdminRoute>} />
        <Route path="/admin/karte" element={<ProtectedAdminRoute><AdminKartePage /></ProtectedAdminRoute>} />
        <Route path="/admin/gradovi" element={<ProtectedAdminRoute><AdminGradoviPage /></ProtectedAdminRoute>} />
        <Route path="/admin/vozila" element={<ProtectedAdminRoute><AdminVozilaPage /></ProtectedAdminRoute>} />
        <Route path="/admin/kompanije" element={<ProtectedAdminRoute><AdminKompanijaPage /></ProtectedAdminRoute>} />
        <Route path="/admin/korisnici" element={<ProtectedAdminRoute><AdminKorisniciPage /></ProtectedAdminRoute>} />
        <Route path="/admin/rezervacije" element={<ProtectedAdminRoute><AdminRezervacijePage /></ProtectedAdminRoute>} />
        <Route path="/admin/ocene" element={<ProtectedAdminRoute><AdminOcenePage /></ProtectedAdminRoute>} />
        <Route path="/admin/placanja" element={<ProtectedAdminRoute><AdminPlacanjaPage /></ProtectedAdminRoute>} />
        <Route path="/korpa" element={<KorpaPage />} />
        <Route path="/moje-rezervacije" element={<MojeRezervacijePage />} />
        <Route path="/placanje" element={<PlacanjePage />} />
      </Routes>
    </BrowserRouter>
  );
}