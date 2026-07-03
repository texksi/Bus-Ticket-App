import Navbar from "../components/layout/Navbar";
import Hero from "../components/home/Hero";
import Prednosti from "../components/home/Prednosti";
import PopularneDestinacije from "../components/home/PopularneDestinacije";
import Footer from "../components/layout/Footer";

export default function HomePage() {
  return (
    <div className="min-h-screen">
      <Navbar />
      <Hero />
      <Prednosti />
      <PopularneDestinacije />
      <Footer />
    </div>
  );
}