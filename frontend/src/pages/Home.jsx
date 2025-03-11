import '../css/Home.css';

const Home = () => {
    return (
        <div className="home-container">
            <header className="hero-section">
                <h1 className="hero-title">ContaminUS</h1>
                <p className="hero-description">
                    Proyecto universitario para monitorear la calidad del aire usando sensores IoT.
                </p>
                <button className="cta-button">Explorar Proyecto</button>
            </header>

            <section className="about-section">
                <h2>Sobre el Proyecto</h2>
                <p>
                    ContaminUS es una solución basada en tecnologías IoT para medir la calidad del aire en tiempo real. 
                    Este proyecto busca crear una herramienta accesible para estudiantes, investigadores y comunidades 
                    interesadas en el monitoreo ambiental.
                </p>
                <div className="features">
                    <div className="feature">
                        <h3>Medición en tiempo real</h3>
                        <p>Monitorea la calidad del aire con sensores MQ-135 y DHT11, mostrando datos precisos y actualizados.</p>
                    </div>
                    <div className="feature">
                        <h3>Aplicación web interactiva</h3>
                        <p>Visualiza los datos de calidad del aire mediante mapas interactivos y gráficos.</p>
                    </div>
                    <div className="feature">
                        <h3>Colaboración en la universidad</h3>
                        <p>El proyecto está orientado a estudiantes que deseen aprender y colaborar con el análisis de datos ambientales.</p>
                    </div>
                </div>
            </section>
        </div>
    );
}


export default Home;