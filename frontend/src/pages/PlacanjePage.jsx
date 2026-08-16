import { useState, useEffect } from "react";
import { useNavigate, useSearchParams } from "react-router-dom";
import { loadStripe } from "@stripe/stripe-js";
import {
  Elements,
  CardElement,
  useStripe,
  useElements,
} from "@stripe/react-stripe-js";
import Navbar from "../components/layout/Navbar";
import api from "../api/api";

const stripePromise = loadStripe(
  "pk_test_51TLQD3GBfe54Rqwiy9h8fXeqfCuKtraCuak6QVQl2sZQ1rjof3ThSiLgaLtVrpLQr1n9fFyDatsO8rXxcumSfPLC00KCkPK8ds",
);

function PlacanjeForrma({ rezervacijaId, iznos }) {
  const stripe = useStripe();
  const elements = useElements();
  const navigate = useNavigate();
  const [loading, setLoading] = useState(false);
  const [greska, setGreska] = useState(null);
  const [uspeh, setUspeh] = useState(false);

  const handlePlati = async (e) => {
    e.preventDefault();
    if (!stripe || !elements) return;

    setLoading(true);
    setGreska(null);

    try {
      const res = await api.post(
        `/api/placanja/create-payment?rezervacijaId=${rezervacijaId}&iznos=${iznos}`,
      );
      const clientSecret = res.data.clientSecret;
      const { error, paymentIntent } = await stripe.confirmCardPayment(
        clientSecret,
        {
          payment_method: {
            card: elements.getElement(CardElement),
          },
        },
      );

      if (error) {
        setGreska(error.message);
        return;
      }

      if (paymentIntent?.status === "succeeded") {
        await api.put(`/api/rezervacije/${rezervacijaId}`, {
          status: "ZAVRSENA",
          nacinPlacanja: "ONLINE",
          ukupanIznos: Number(iznos),
          korisnikId: Number(
            JSON.parse(atob(localStorage.getItem("token").split(".")[1])).id,
          ),
        });
        setUspeh(true);
        setTimeout(() => navigate("/moje-rezervacije"), 2000);
      }
    } catch (err) {
      console.error("catch greska:", err);
      setGreska("Greška pri plaćanju. Pokušajte ponovo.");
    } finally {
      setLoading(false);
    }
  };

  if (uspeh) {
    return (
      <div className="text-center py-16">
        <h2 className="text-[#1a237e] text-xl font-medium mb-2">
          Plaćanje uspešno!
        </h2>
        <p className="text-gray-400 text-sm">
          Preusmeravamo vas na vaše rezervacije...
        </p>
      </div>
    );
  }

  return (
    <form onSubmit={handlePlati} className="flex flex-col gap-5">
      <div>
        <label className="text-xs text-gray-500 font-medium block mb-2">
          Podaci kartice
        </label>
        <div className="border border-gray-200 rounded-xl px-4 py-3">
          <CardElement
            options={{
              style: {
                base: {
                  fontSize: "14px",
                  color: "#1a237e",
                  "::placeholder": { color: "#aab7c4" },
                },
              },
            }}
          />
        </div>
      </div>

      {greska && (
        <div className="bg-red-50 border border-red-200 text-red-600 text-xs rounded-xl px-4 py-3">
          {greska}
        </div>
      )}

      <button
        type="submit"
        disabled={!stripe || loading}
        className="w-full bg-[#ffa726] hover:bg-[#fb8c00] text-white border-none rounded-xl py-3 text-sm font-medium cursor-pointer transition disabled:bg-gray-200 disabled:cursor-not-allowed"
      >
        {loading ? "Procesiranje..." : `Plati ${iznos} RSD`}
      </button>

      <p className="text-center text-xs text-gray-400">
        🔒 Plaćanje je sigurno i zaštićeno od strane Stripe-a
      </p>
    </form>
  );
}

export default function PlacanjePage() {
  const [searchParams] = useSearchParams();
  const rezervacijaId = searchParams.get("rezervacijaId");
  const iznos = searchParams.get("iznos");

  return (
    <div className="min-h-screen bg-[#f8f9fc]">
      <Navbar />

      <div
        className="px-12 py-6"
        style={{
          background: "linear-gradient(135deg, #1a3a8f 0%, #1565c0 100%)",
        }}
      >
        <h1 className="text-white text-2xl font-medium">Plaćanje</h1>
        <p className="text-[#b3cef5] text-sm mt-1">
          Rezervacija #{rezervacijaId} · {iznos} RSD
        </p>
      </div>

      <div className="max-w-md mx-auto px-6 py-8">
        <div className="bg-white rounded-2xl p-8 border border-gray-100">
          <h2 className="text-[#1a237e] text-base font-medium mb-6">
            Unesite podatke kartice
          </h2>

          <div className="bg-[#e8f0fe] rounded-xl px-4 py-3 mb-6">
            <div className="flex justify-between">
              <span className="text-[#1565c0] text-sm">Ukupan iznos</span>
              <span className="text-[#1a237e] text-sm font-medium">
                {iznos} RSD
              </span>
            </div>
            <div className="flex justify-between mt-1">
              <span className="text-[#1565c0] text-xs">Rezervacija</span>
              <span className="text-[#1a237e] text-xs">#{rezervacijaId}</span>
            </div>
          </div>

          <Elements stripe={stripePromise}>
            <PlacanjeForrma
              rezervacijaId={Number(rezervacijaId)}
              iznos={Number(iznos)}
            />
          </Elements>
        </div>
      </div>
    </div>
  );
}