import { Link } from "react-router-dom";

export default function Footer() {
  return (
    <footer className="bg-[#1a237e] text-white py-12 px-6">
      <div className="max-w-5xl mx-auto grid grid-cols-1 md:grid-cols-3 gap-8">
        <div>
          <h3 className="text-xl font-bold mb-3">🚌 BusTicket</h3>
          <p className="text-blue-200 text-sm leading-relaxed">
            Vaš pouzdani partner za autobuska putovanja širom Srbije.
            Brza rezervacija, povoljne cene, udobna vožnja.
          </p>
        </div>

        <div>
          <h4 className="font-semibold mb-3 text-[#ffa726]">Navigacija</h4>
          <ul className="space-y-2 text-sm text-blue-200">
            <li><Link to="/" className="hover:text-white transition">Početna</Link></li>
            <li><Link to="/putovanja" className="hover:text-white transition">Putovanja</Link></li>
            <li><Link to="/o-nama" className="hover:text-white transition">O nama</Link></li>
            <li><Link to="/kontakt" className="hover:text-white transition">Kontakt</Link></li>
          </ul>
        </div>

        <div>
          <h4 className="font-semibold mb-3 text-[#ffa726]">Kontakt</h4>
          <ul className="space-y-2 text-sm text-blue-200">
            <li>📧 info@busticket.rs</li>
            <li>📞 +381 11 123 456</li>
            <li>📍 Beograd, Srbija</li>
          </ul>
        </div>
      </div>

      <div className="max-w-5xl mx-auto mt-8 pt-6 border-t border-blue-800 text-center text-xs text-blue-300">
        © {new Date().getFullYear()} BusTicket. Sva prava zadržana.
      </div>
    </footer>
  );
}