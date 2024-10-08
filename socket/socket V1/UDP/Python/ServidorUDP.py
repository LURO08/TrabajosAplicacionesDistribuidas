import socket

# Dirección IP y puerto en los que se va a escuchar
server_ip = '127.0.0.1'
server_port = 8080

# Crear el socket UDP
server_socket = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)

# Asociar el socket con la dirección IP y el puerto
server_socket.bind((server_ip, server_port))

print(f"Servidor UDP escuchando en {server_ip}:{server_port}")

# Almacenar la dirección del cliente
client_addr = None

while True:
    if client_addr:
        # Permitir que el servidor también envíe mensajes al cliente
        server_message = input("Servidor: Escribe un mensaje para el cliente: ")
        server_socket.sendto(server_message.encode(), client_addr)

    # Esperar recibir un mensaje del cliente
    print("Esperando mensaje del cliente...")
    data, addr = server_socket.recvfrom(1024)  # buffer de 1024 bytes
    client_addr = addr  # Guardar la dirección del cliente para responder
    print(f"Mensaje recibido de {client_addr}: {data.decode()}")

    # Responder al cliente
    response = f"Servidor: Recibí tu mensaje '{data.decode()}'"
    server_socket.sendto(response.encode(), client_addr)

    if data.decode().lower() == 'salir':
        print("Servidor: Cliente terminó la conexión.")
        break

# Cerrar el socket
server_socket.close()
