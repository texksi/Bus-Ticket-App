import ReactDOM from "react-dom/client";
import React from "react";
import App from "./App";
import { KorpaProvider } from "./context/KorpaContext";
import "./index.css";

ReactDOM.createRoot(document.getElementById("root")).render(
  <React.StrictMode>
    <KorpaProvider>
      <App />
    </KorpaProvider>
  </React.StrictMode>
);