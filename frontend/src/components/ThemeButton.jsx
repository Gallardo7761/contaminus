import { useTheme } from "../contexts/ThemeContext.jsx";
import "../css/ThemeButton.css";

export default function ThemeButton() {
    const { theme, toggleTheme } = useTheme();

    return (
        <button className="theme-toggle" onClick={toggleTheme}>
            {theme === "dark" ? "☀️" : "🌙"}
        </button>
    );
}
