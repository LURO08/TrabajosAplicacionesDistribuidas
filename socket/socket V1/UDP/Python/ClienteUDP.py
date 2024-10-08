import socket

# Dirección IP y puerto del servidor
server_ip = '127.0.0.1'
server_port = 8081

# Crear el socket UDP
client_socket = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)

while True:
    # Solicitar entrada del usuario para enviar el mensaje
    message = input("Cliente: Escribe un mensaje para el servidor: ")

    # Enviar mensaje al servidor
    client_socket.sendto(message.encode(), (server_ip, server_port))

    # Recibir respuesta del servidor
    data, addr = client_socket.recvfrom(1024)
    print(f"Respuesta del servidor: {data.decode()}")

    # Opción para salir del loop si el usuario ingresa 'salir'
    if message.lower() == 'salir':
        print("Cliente: Terminando la conexión...")
        break

# Cerrar el socket
client_socket.close()
