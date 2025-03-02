import subprocess

def run_api():
    try:
        subprocess.run(
            ['uvicorn', 'main:app', '--reload', '--port', '9091'],
            check=True,
            cwd='./'
        )
    except subprocess.CalledProcessError as e:
        print(f"Error al iniciar API DAD: {e.stderr}")

if __name__ == "__main__":
    run_api()
