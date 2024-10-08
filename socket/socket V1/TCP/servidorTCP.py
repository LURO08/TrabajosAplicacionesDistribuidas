import socket

def start_server():
    host = 'localhost'
    port = 8080

    server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    server_socket.bind((host, port))
    server_socket.listen(5)
    print(f"Servidor escuchando en {host}:{port}")

    while True:
        client_socket, addr = server_socket.accept()
        print(f"Conexión desde {addr}")
        handle_client(client_socket)

def handle_client(client_socket):
    with client_socket:
        while True:
            data = client_socket.recv(1024)
            if not data:
                break
            print(f"Recibido: {data.decode()}")
            client_socket.sendall(data)

if __name__ == "__main__":
    start_server()
