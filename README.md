# ⚠ REQUISITOS PREVIOS
- Descargar [MariaDB 10.6.21](https://mariadb.org/download/?t=mariadb&p=mariadb&r=10.6.21&os=windows&cpu=x86_64&pkg=msi&mirror=raiolanetworks)
- Descargar [DBeaver](https://dbeaver.io/download/)
- Descargar Node.js
    - Para descargar Node recomiendo fnm (Fast Node Manager) que lo podéis descargar [aquí](https://miarma.net/descargas/fnm.exe)
    - Para instalarlo: `fnm install 23.7.0`

# Para lanzar la API en local 
1. `pip install -r requirements.txt`
2. `uvicorn main:app --reload --port <puerto que quieras>`

# Para lanzar la web en modo desarrollador
1. `npm install`
2. `npm run dev`

# Para transpilar la web de React a HTML/CSS/JS Vanilla
`npm run build` <br>
La encontraréis en `dist/`
