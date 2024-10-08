import socket

def start_client():
    host = '127.0.0.1'
    port = 8080

    with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as client_socket:
        client_socket.connect((host, port))

        while True:
            message = input("Escribe un mensaje (o 'exit' para salir): ")
            if message.lower() == 'exit':
                break
            client_socket.sendall(message.encode())
            response = client_socket.recv(1024)
            print(f"Respuesta del servidor: {response.decode()}")

if __name__ == "__main__":
    start_client()
