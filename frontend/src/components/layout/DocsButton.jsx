import "@/css/DocsButton.css";
import { Link } from "react-router-dom";

const DocsButton = () => {
    return (
        <Link to="/docs">
            <button className="docs-button">📃</button>
        </Link>
    );
}

export default DocsButton;