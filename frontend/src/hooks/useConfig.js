import { useContext } from "react";
import { ConfigContext } from "@/context/ConfigContext.jsx";

export const useConfig = () => useContext(ConfigContext);
