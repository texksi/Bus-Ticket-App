import { createContext, useContext, useState, useEffect } from "react";

const KorpaContext = createContext();

export function KorpaProvider({ children }) {
  const [korpa, setKorpa] = useState(() => {
    try {
      return JSON.parse(localStorage.getItem("korpa")) || [];
    } catch {
      return [];
    }
  });

  useEffect(() => {
    localStorage.setItem("korpa", JSON.stringify(korpa));
  }, [korpa]);

  const dodajUKorpu = (item) => {
    const postoji = korpa.find(
      (k) => k.brojSedista === item.brojSedista && k.putovanjeId === item.putovanjeId
    );
    if (postoji) return;
    setKorpa([...korpa, item]);
  };

  const ukloniIzKorpe = (brojSedista, putovanjeId) => {
    setKorpa(korpa.filter(
      (k) => !(k.brojSedista === brojSedista && k.putovanjeId === putovanjeId)
    ));
  };

  const ocistiKorpu = () => setKorpa([]);

const ukupanIznos = korpa.reduce((sum, k) => sum + Number(k.finalnaCena), 0);
  return (
    <KorpaContext.Provider value={{ korpa, dodajUKorpu, ukloniIzKorpe, ocistiKorpu, ukupanIznos }}>
      {children}
    </KorpaContext.Provider>
  );
}

export const useKorpa = () => useContext(KorpaContext);