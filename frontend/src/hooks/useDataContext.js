import { useContext } from "react";
import { DataContext } from "@/context/DataContext";

export const useDataContext = () => useContext(DataContext);
