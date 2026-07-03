import Navbar from "../components/layout/Navbar";
import Footer from "../components/layout/Footer";

function ONama() {
  return (
    <>
      {/* HERO */}
      <div
        className="grid grid-cols-2 gap-12 items-center px-12 py-16"
        style={{ background: "linear-gradient(135deg, #1a3a8f 0%, #1565c0 100%)" }}
      >
        <div>
          <h1 className="text-white text-4xl font-medium mb-4">O nama</h1>
          <p className="text-[#b3cef5] text-sm leading-relaxed mb-4">
            BusTicket je vodeća platforma za rezervaciju autobuskih karata u Srbiji.
            Od 2020. godine pomažemo putnicima da brzo i jednostavno pronađu i rezervišu
            kartu za željenu destinaciju.
          </p>
          <p className="text-[#b3cef5] text-sm leading-relaxed">
            Naša misija je da putovanje autobusom učinimo što jednostavnijim i prijatnijim
            iskustvom za svakog putnika.
          </p>
        </div>
        <div className="grid grid-cols-2 gap-4">
          {[
            { broj: "10K+", label: "Putnika mesečno" },
            { broj: "50+", label: "Autobuskih linija" },
            { broj: "4.8★", label: "Prosečna ocena" },
            { broj: "5+", label: "Godina iskustva" },
          ].map((s, i) => (
            <div key={i} className="bg-white/10 border border-white/15 rounded-2xl p-5 text-center">
              <div className="text-[#ffa726] text-3xl font-medium">{s.broj}</div>
              <div className="text-[#b3cef5] text-xs mt-1">{s.label}</div>
            </div>
          ))}
        </div>
      </div>

      {/* VREDNOSTI */}
      <div className="bg-white py-16 px-12">
        <h2 className="text-[#1a237e] text-2xl font-medium text-center mb-2">Naše vrednosti</h2>
        <p className="text-gray-400 text-sm text-center mb-10">Principi koji nas vode svaki dan</p>
        <div className="grid grid-cols-3 gap-6 max-w-3xl mx-auto">
          {[
            { ikona: "🎯", naslov: "Pouzdanost", opis: "Svaki polazak na vreme, svaka rezervacija sigurna. Gradimo poverenje korak po korak." },
            { ikona: "💡", naslov: "Inovacija", opis: "Stalno unapređujemo platformu kako bi iskustvo kupovine karte bilo što jednostavnije." },
            { ikona: "🤝", naslov: "Partnerstvo", opis: "Sarađujemo sa najboljim prevoznicima u Srbiji kako bismo vam ponudili najširi izbor." },
          ].map((v, i) => (
            <div key={i} className="bg-[#f8f9fc] rounded-2xl p-7">
              <div className="text-4xl mb-4">{v.ikona}</div>
              <div className="text-[#1a237e] text-sm font-medium mb-2">{v.naslov}</div>
              <div className="text-gray-400 text-xs leading-relaxed">{v.opis}</div>
            </div>
          ))}
        </div>
      </div>

      {/* TIM */}
      <div className="bg-[#f8f9fc] py-16 px-12">
        <h2 className="text-[#1a237e] text-2xl font-medium text-center mb-2">Naš tim</h2>
        <p className="text-gray-400 text-sm text-center mb-10">Ljudi koji stoje iza BusTicket platforme</p>
        <div className="grid grid-cols-3 gap-6 max-w-3xl mx-auto">
          {[
            { inicijali: "MJ", ime: "Marko Jovanović", pozicija: "Osnivač i CEO" },
            { inicijali: "AP", ime: "Ana Petrović", pozicija: "Tehnički direktor" },
            { inicijali: "NS", ime: "Nikola Simić", pozicija: "Menadžer podrške" },
          ].map((t, i) => (
            <div key={i} className="bg-white rounded-2xl p-6 text-center border border-gray-100">
              <div className="w-14 h-14 rounded-full bg-gradient-to-br from-[#1565c0] to-[#1a237e] flex items-center justify-center text-white text-lg font-medium mx-auto mb-4">
                {t.inicijali}
              </div>
              <div className="text-[#1a237e] text-sm font-medium">{t.ime}</div>
              <div className="text-gray-400 text-xs mt-1">{t.pozicija}</div>
            </div>
          ))}
        </div>
      </div>
    </>
  );
}

function Kontakt() {
  return (
    <div className="bg-[#f8f9fc] py-16 px-12">
      <h2 className="text-[#1a237e] text-2xl font-medium text-center mb-2">Kontaktirajte nas</h2>
      <p className="text-gray-400 text-sm text-center mb-10">Tu smo za sva vaša pitanja</p>

      <div className="grid grid-cols-2 gap-12 max-w-4xl mx-auto">
        {/* INFO */}
        <div>
          <h3 className="text-[#1a237e] text-xl font-medium mb-2">Stupite u kontakt</h3>
          <p className="text-gray-400 text-sm mb-8 leading-relaxed">
            Imate pitanje, sugestiju ili problem? Naš tim je dostupan svakog dana od 8h do 20h.
          </p>
          {[
            { ikona: "📧", label: "Email", vrednost: "info@busticket.rs" },
            { ikona: "📞", label: "Telefon", vrednost: "+381 11 123 456" },
            { ikona: "📍", label: "Adresa", vrednost: "Knez Mihailova 10, Beograd" },
            { ikona: "⏰", label: "Radno vreme", vrednost: "Pon - Pet: 8h - 20h" },
          ].map((k, i) => (
            <div key={i} className="flex items-start gap-4 mb-5">
              <div className="w-10 h-10 bg-[#e8f0fe] rounded-xl flex items-center justify-center text-lg flex-shrink-0">
                {k.ikona}
              </div>
              <div>
                <div className="text-gray-400 text-xs">{k.label}</div>
                <div className="text-[#1a237e] text-sm font-medium mt-0.5">{k.vrednost}</div>
              </div>
            </div>
          ))}
        </div>

        {/* FORMA */}
        <div className="bg-white rounded-2xl p-8">
          <h3 className="text-[#1a237e] text-base font-medium mb-6">Pošaljite poruku</h3>
          <div className="flex flex-col gap-4">
            <div className="flex flex-col gap-1">
              <label className="text-xs text-gray-500 font-medium">Ime i prezime</label>
              <input type="text" placeholder="Vaše ime i prezime" className="border border-gray-200 rounded-xl px-4 py-2.5 text-sm text-[#1a237e] outline-none focus:border-[#1565c0]"/>
            </div>
            <div className="flex flex-col gap-1">
              <label className="text-xs text-gray-500 font-medium">Email adresa</label>
              <input type="email" placeholder="vas@email.com" className="border border-gray-200 rounded-xl px-4 py-2.5 text-sm text-[#1a237e] outline-none focus:border-[#1565c0]"/>
            </div>
            <div className="flex flex-col gap-1">
              <label className="text-xs text-gray-500 font-medium">Tema</label>
              <input type="text" placeholder="O čemu se radi?" className="border border-gray-200 rounded-xl px-4 py-2.5 text-sm text-[#1a237e] outline-none focus:border-[#1565c0]"/>
            </div>
            <div className="flex flex-col gap-1">
              <label className="text-xs text-gray-500 font-medium">Poruka</label>
              <textarea placeholder="Napišite vašu poruku..." rows={4} className="border border-gray-200 rounded-xl px-4 py-2.5 text-sm text-[#1a237e] outline-none focus:border-[#1565c0] resize-none"/>
            </div>
            <button className="w-full bg-[#1565c0] hover:bg-[#0d47a1] text-white rounded-xl py-3 text-sm font-medium border-none cursor-pointer transition">
              Pošaljite poruku
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}

export default function ONamaPage() {
  return (
    <div className="min-h-screen">
      <Navbar />
      <ONama />
      <Kontakt />
      <Footer />
    </div>
  );
}