import { useState, useEffect } from "react";
import { useKorpa } from "../../context/KorpaContext";
import api from "../../api/api";

const TIPOVI = [
  { tip: "STANDARD", koeficijent: 1.0, label: "Standard", opis: "Puna cena" },
  { tip: "STUDENT", koeficijent: 0.8, label: "Student", opis: "-20% popust" },
  { tip: "VIP", koeficijent: 1.5, label: "VIP", opis: "+50% premium" },
];

const getKolone = (brojKolona) =>
  Array.from({ length: brojKolona }, (_, i) => String.fromCharCode(65 + i));

export default function RezervacijaModal({ putovanje, gradovi, onClose }) {
  const { korpa, dodajUKorpu, ukloniIzKorpe } = useKorpa();
  const [zauzetaSedista, setZauzetaSedista] = useState([]);
  const [vozilo, setVozilo] = useState(null);
  const [odabraniTip, setOdabraniTip] = useState("STANDARD");
  const [loading, setLoading] = useState(true);
  const [poruka, setPoruka] = useState(null);
  const [greska, setGreska] = useState(false);

  const getGradNaziv = (id) =>
    gradovi.find((g) => g.id === Number(id))?.naziv || "";
  const putovanjeNaziv = `${getGradNaziv(putovanje.polazisteId)} → ${getGradNaziv(putovanje.odredisteId)}`;

  useEffect(() => {
    Promise.all([
      api.get(`/api/karte/putovanje/${putovanje.id}`),
      api.get(`/api/vozila/${putovanje.voziloId}`),
    ])
      .then(([karte, voz]) => {
        setZauzetaSedista(karte.data.map((k) => k.brojSedista));
        setVozilo(voz.data);
      })
      .finally(() => setLoading(false));
  }, [putovanje.id]);

  const getFinalCena = (koeficijent) =>
    Math.round(putovanje.osnovnaCena * koeficijent);

  const isZauzeto = (sediste) => zauzetaSedista.includes(sediste);

  const isUKorpi = (sediste) =>
    korpa.some(
      (k) => k.brojSedista === sediste && k.putovanjeId === putovanje.id,
    );

  const handleKlikSediste = (sediste) => {
    if (isZauzeto(sediste)) return;
    if (isUKorpi(sediste)) {
      ukloniIzKorpe(sediste, putovanje.id);
    } else {
      const tip = TIPOVI.find((t) => t.tip === odabraniTip);
      dodajUKorpu({
        putovanjeId: putovanje.id,
        brojSedista: sediste,
        tip: odabraniTip,
        finalnaCena: getFinalCena(tip.koeficijent),
        putovanjeNaziv,
      });
    }
  };

  const kolone = vozilo ? getKolone(vozilo.brojKolona) : [];
  const levaKolone = kolone.slice(0, Math.ceil(kolone.length / 2));
  const desnaKolone = kolone.slice(Math.ceil(kolone.length / 2));

  const renderSediste = (red, kol) => {
    const sediste = `${red}${kol}`;
    const zauzeto = isZauzeto(sediste);
    const uKorpi = isUKorpi(sediste);
    return (
      <button
        key={sediste}
        onClick={() => handleKlikSediste(sediste)}
        disabled={zauzeto}
        className={`w-9 h-9 rounded-lg text-xs font-medium border-2 flex-shrink-0 transition ${
          zauzeto
            ? "bg-gray-100 border-gray-300 text-gray-400 cursor-not-allowed"
            : uKorpi
              ? "bg-[#1565c0] border-[#1565c0] text-white"
              : "bg-[#e8f0fe] border-[#1565c0] text-[#1565c0] hover:bg-[#1565c0] hover:text-white cursor-pointer"
        }`}
      >
        {sediste}
      </button>
    );
  };

  const brRed = vozilo?.brojRedova || 0;

  return (
    <div className="fixed inset-0 bg-black/40 flex items-center justify-center z-50 p-4">
      <div className="bg-white rounded-2xl w-full max-w-2xl max-h-[90vh] overflow-y-auto">
        {/* HEADER */}
        <div
          className="px-6 py-5 rounded-t-2xl flex justify-between items-center"
          style={{ background: "linear-gradient(135deg, #1a3a8f, #1565c0)" }}
        >
          <div>
            <h2 className="text-white text-lg font-medium">{putovanjeNaziv}</h2>
            <p className="text-[#b3cef5] text-sm mt-0.5">
              {putovanje.vremePolaska
                ? new Date(putovanje.vremePolaska).toLocaleString("sr-RS")
                : ""}{" "}
              · {putovanje.osnovnaCena} RSD
            </p>
          </div>
          <button
            onClick={onClose}
            className="bg-white/15 border-none rounded-lg w-8 h-8 text-white cursor-pointer hover:bg-white/25 transition text-lg"
          >
            ✕
          </button>
        </div>

        <div className="p-6">
          {/* TIP KARTE */}
          <p className="text-[#1a237e] text-sm font-medium mb-3">Tip karte</p>
          <div className="grid grid-cols-3 gap-3 mb-5">
            {TIPOVI.map((t) => (
              <div
                key={t.tip}
                onClick={() => setOdabraniTip(t.tip)}
                className={`border-2 rounded-xl p-3 text-center cursor-pointer transition ${
                  odabraniTip === t.tip
                    ? "border-[#1565c0] bg-[#e8f0fe]"
                    : "border-gray-200 hover:border-[#1565c0]"
                }`}
              >
                <div className="text-[#1a237e] text-sm font-medium">
                  {t.label}
                </div>
                <div className="text-[#1a237e] font-medium mt-1">
                  {getFinalCena(t.koeficijent)} RSD
                </div>
                <div className="text-gray-400 text-xs mt-0.5">{t.opis}</div>
              </div>
            ))}
          </div>

          {/* LEGENDA */}
          <div className="flex gap-4 mb-4">
            {[
              {
                boja: "bg-[#e8f0fe] border border-[#1565c0]",
                label: "Slobodno",
              },
              { boja: "bg-gray-100 border border-gray-300", label: "Zauzeto" },
              { boja: "bg-[#1565c0]", label: "Izabrano" },
            ].map((l, i) => (
              <div
                key={i}
                className="flex items-center gap-2 text-xs text-gray-500"
              >
                <div className={`w-4 h-4 rounded-md ${l.boja}`} />
                {l.label}
              </div>
            ))}
          </div>

          {/* GRID SEDISTA */}
          {loading ? (
            <div className="text-center text-gray-400 py-8">
              Učitavanje sedišta...
            </div>
          ) : (
            <div className="bg-[#f8f9fc] rounded-xl p-4">
              <div className="flex gap-3">
                {/* PREDNJI DEO - levo */}
                <div
                  className="bg-[#e8f0fe] rounded-lg flex items-center justify-center px-2 text-xs text-[#1565c0] font-medium flex-shrink-0"
                  style={{
                    writingMode: "vertical-rl",
                    transform: "rotate(180deg)",
                  }}
                >
                  🚌 Prednji deo
                </div>

                <div className="flex-1">
                  {/* Labele redova */}
                  <div className="flex gap-1.5 mb-2 ml-6">
                    {Array.from({ length: brRed }, (_, i) => (
                      <div
                        key={i}
                        className="w-9 text-center text-xs text-gray-400"
                      >
                        {i + 1}
                      </div>
                    ))}
                  </div>

                  {/* Leve kolone */}
                  <div className="flex flex-col gap-1.5">
                    {levaKolone.map((kol) => (
                      <div key={kol} className="flex items-center gap-1.5">
                        <div className="w-5 text-center text-xs text-gray-400">
                          {kol}
                        </div>
                        {Array.from({ length: brRed }, (_, redIdx) =>
                          renderSediste(redIdx + 1, kol),
                        )}
                      </div>
                    ))}

                    {/* PROLAZ */}
                    <div className="h-3" />

                    {/* Desne kolone */}
                    {desnaKolone.map((kol) => (
                      <div key={kol} className="flex items-center gap-1.5">
                        <div className="w-5 text-center text-xs text-gray-400">
                          {kol}
                        </div>
                        {Array.from({ length: brRed }, (_, redIdx) =>
                          renderSediste(redIdx + 1, kol),
                        )}
                      </div>
                    ))}
                  </div>
                </div>
              </div>
            </div>
          )}

          {poruka && (
            <div
              className={`text-sm rounded-xl px-4 py-3 mb-4 border ${
                greska
                  ? "bg-red-50 border-red-200 text-red-600"
                  : "bg-green-50 border-green-200 text-green-700"
              }`}
            >
              {greska ? "⚠ " : "✓ "}
              {poruka}
            </div>
          )}

          {/* FOOTER */}
          <div className="flex justify-between items-center mt-5 pt-4 border-t border-gray-100">
            <div className="text-sm text-gray-500">
              Izabrano:{" "}
              <strong className="text-[#1a237e]">
                {korpa.filter((k) => k.putovanjeId === putovanje.id).length}{" "}
                sedišta
              </strong>
            </div>
            <button
              onClick={() => {
                const izabranaSedista = korpa.filter(
                  (k) => k.putovanjeId === putovanje.id,
                ).length;
                if (izabranaSedista === 0) {
                  setGreska(true);
                  setPoruka("Morate izabrati sedište pre dodavanja karte u korpu.");
                  return;
                }
                setGreska(false);
                setPoruka("Karta je dodata u korpu.");
                setTimeout(onClose, 1200);
              }}
              className="bg-[#ffa726] hover:bg-[#fb8c00] text-white border-none rounded-xl px-6 py-2.5 text-sm font-medium cursor-pointer transition"
            >
              Dodaj u korpu →
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}