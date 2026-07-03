import { useNavigate } from "react-router-dom";

const destinacije = [
  { polaziste: "Beograd", odrediste: "Novi Sad", cena: "500", vreme: "1h 30min", slika: "https://upload.wikimedia.org/wikipedia/commons/thumb/4/4e/Novi_Sad_-_panorama.jpg/800px-Novi_Sad_-_panorama.jpg" },
  { polaziste: "Beograd", odrediste: "Niš", cena: "900", vreme: "3h", slika: "https://upload.wikimedia.org/wikipedia/commons/thumb/5/5e/Ni%C5%A1_Fortress.jpg/800px-Ni%C5%A1_Fortress.jpg" },
  { polaziste: "Beograd", odrediste: "Subotica", cena: "700", vreme: "2h 30min", slika: "https://upload.wikimedia.org/wikipedia/commons/thumb/8/8e/Subotica_synagogue.jpg/800px-Subotica_synagogue.jpg" },
  { polaziste: "Novi Sad", odrediste: "Niš", cena: "1100", vreme: "4h", slika: "https://upload.wikimedia.org/wikipedia/commons/thumb/3/34/Ni%C5%A1_panorama.jpg/800px-Ni%C5%A1_panorama.jpg" },
  { polaziste: "Beograd", odrediste: "Kragujevac", cena: "600", vreme: "2h", slika: "https://upload.wikimedia.org/wikipedia/commons/thumb/b/b0/Kragujevac_old_city.jpg/800px-Kragujevac_old_city.jpg" },
  { polaziste: "Beograd", odrediste: "Čačak", cena: "650", vreme: "2h 15min", slika: "https://upload.wikimedia.org/wikipedia/commons/thumb/1/1e/Cacak_panorama.jpg/800px-Cacak_panorama.jpg" },
];

export default function PopularneDestinacije() {
  const navigate = useNavigate();

  return (
    <section className="py-16 px-6 bg-white">
      <div className="max-w-5xl mx-auto">
        <h2 className="text-2xl font-bold text-[#1a237e] text-center mb-2">
          Popularne destinacije
        </h2>
        <p className="text-gray-500 text-center mb-10 text-sm">
          Najpopularnije linije sa direktnim polaskom
        </p>

        <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-6">
          {destinacije.map((d, i) => (
            <div
              key={i}
              className="rounded-xl overflow-hidden shadow-sm hover:shadow-lg transition cursor-pointer group"
              onClick={() => navigate(`/putovanja?polaziste=${d.polaziste}&odrediste=${d.odrediste}`)}
            >
              <div className="relative h-40 overflow-hidden">
                <img
                  src={d.slika}
                  alt={d.odrediste}
                  className="w-full h-full object-cover group-hover:scale-105 transition duration-300"
                  onError={(e) => { e.target.src = "https://via.placeholder.com/400x200?text=" + d.odrediste; }}
                />
                <div className="absolute inset-0 bg-gradient-to-t from-[#1a237e]/70 to-transparent" />
                <div className="absolute bottom-3 left-3 text-white">
                  <p className="text-xs opacity-80">{d.polaziste}</p>
                  <p className="font-bold text-lg">{d.odrediste}</p>
                </div>
              </div>
              <div className="p-4 flex justify-between items-center bg-white">
                <div className="text-xs text-gray-500">⏱ {d.vreme}</div>
                <div className="text-[#1a237e] font-bold">
                  od {d.cena} RSD
                </div>
                <button className="bg-[#ffa726] hover:bg-[#fb8c00] text-white text-xs px-3 py-1 rounded-lg transition">
                  Rezerviši
                </button>
              </div>
            </div>
          ))}
        </div>

        <div className="text-center mt-8">
          <button
            onClick={() => navigate("/putovanja")}
            className="border-2 border-[#1a237e] text-[#1a237e] hover:bg-[#1a237e] hover:text-white px-6 py-2 rounded-lg text-sm font-medium transition"
          >
            Prikaži sve destinacije →
          </button>
        </div>
      </div>
    </section>
  );
}