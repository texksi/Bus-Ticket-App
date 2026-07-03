const prednosti = [
  {
    ikona: "⭐",
    naslov: "Odličan odnos prema korisniku",
    opis: "Naš tim je uvek dostupan za sva vaša pitanja i probleme.",
  },
  {
    ikona: "🍽️",
    naslov: "Doručak i ručak",
    opis: "Na dužim putovanjima obezbeđujemo obroke za sve putnike.",
  },
  {
    ikona: "💰",
    naslov: "Konkurentne cene",
    opis: "Pronađite najpovoljniju kartu bez skrivenih troškova.",
  },
  {
    ikona: "🚌",
    naslov: "Moderan prevoz",
    opis: "Vozni park opremljen klimom, WiFi-jem i USB punjačima.",
  },
];

export default function Prednosti() {
  return (
    <section className="py-16 px-6 bg-gray-50">
      <div className="max-w-5xl mx-auto">
        <h2 className="text-2xl font-bold text-[#1a237e] text-center mb-2">
          Naše prednosti
        </h2>
        <p className="text-gray-500 text-center mb-10 text-sm">
          Zašto putnici biraju BusTicket
        </p>

        <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-4 gap-6">
          {prednosti.map((p, i) => (
            <div
              key={i}
              className="bg-white rounded-xl p-6 shadow-sm hover:shadow-md transition text-center"
            >
              <div className="text-4xl mb-3">{p.ikona}</div>
              <h3 className="font-semibold text-[#1a237e] mb-2 text-sm">{p.naslov}</h3>
              <p className="text-gray-500 text-xs leading-relaxed">{p.opis}</p>
            </div>
          ))}
        </div>
      </div>
    </section>
  );
}