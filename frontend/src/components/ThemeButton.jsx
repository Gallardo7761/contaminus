import { useTheme } from "@/hooks/useTheme";
import "@/css/ThemeButton.css";

const ThemeButton = () => {
    const { theme, toggleTheme } = useTheme();

    return (
        <button className="theme-toggle" onClick={toggleTheme}>
            {theme === "dark" ? "☀️" : "🌙"}
        </button>
    );
}

export default ThemeButton;