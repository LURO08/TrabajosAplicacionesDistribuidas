import socket
import threading

# Dirección IP y puertos
server_ip = '192.168.211.187'
server_portUDP = 8081
server_ipTCP = '192.168.212.2'
server_portTCP = 8080

def handle_receive_TCP(client_socketTCP, client_socketUDP, host, portUDP):
    """Función para recibir mensajes del servidor TCP y reenviar al servidor UDP"""
    while True:
        try:
            with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as client_socketTCP3:
                client_socketTCP3.connect((server_ipTCP, server_portTCP))
            # Recibir respuesta del servidor TCP
                responseTCP = client_socketTCP.recv(1024).decode()
            
            if not responseTCP:
                print("No se recibió respuesta del servidor TCP, cerrando conexión.")
                break

            print(f"Respuesta del servidor TCP: {responseTCP}")

            # Reenviar el mensaje recibido por TCP al servidor UDP
            client_socketUDP.sendto(responseTCP.encode(), (host, portUDP))
            print(f"Mensaje reenviado al servidor UDP en {host}:{portUDP}")
        
        except Exception as e:
            print(f"Error al recibir mensaje TCP: {e}")
            break

def handle_receive_UDP(client_socketUDP):
    """Función para recibir mensajes del servidor UDP y reenviar al servidor TCP"""
    while True:
        try:
            # Recibir respuesta del servidor UDP
            data, _ = client_socketUDP.recvfrom(1024)
            udp_response = data.decode()
            print(f"Respuesta del servidor UDP: {udp_response}")

            # Crear y enviar al servidor TCP
            with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as client_socketTCP2:
                client_socketTCP2.connect((server_ipTCP, server_portTCP))
                print(f"Reenviando al servidor TCP: {server_ipTCP}:{server_portTCP}")
                client_socketTCP2.sendall(udp_response.encode())
                print(f"Mensaje '{udp_response}' enviado al servidor TCP")

        except socket.timeout:
            # Manejo del timeout sin errores críticos
            continue
        except Exception as e:
            print(f"Error al recibir mensaje UDP: {e}")
            break

def start_client():
    try:
        # Crear socket TCP (solo para recibir datos, se cierra después de usar)
        client_socketTCP = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        client_socketUDP = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
        
        # Enviar mensaje inicial al servidor UDP
        initial_message = "Servidor en línea"
        client_socketUDP.sendto(initial_message.encode(), (server_ip, server_portUDP))
        print(f"Mensaje inicial enviado al servidor UDP en {server_ip}:{server_portUDP}")

        # Conectar al servidor TCP
        client_socketTCP.connect((server_ipTCP, server_portTCP))
        print(f"Conectado al servidor TCP en {server_ipTCP}:{server_portTCP}")
        
        # Crear y lanzar hilos para manejar el envío y recepción simultánea
        tcp_receive_thread = threading.Thread(target=handle_receive_TCP, args=(client_socketTCP, client_socketUDP, server_ip, server_portUDP))
        udp_receive_thread = threading.Thread(target=handle_receive_UDP, args=(client_socketUDP,))

        tcp_receive_thread.start()
        udp_receive_thread.start()

        # Esperar a que ambos hilos terminen
        tcp_receive_thread.join()
        udp_receive_thread.join()

    except Exception as e:
        print(f"Ocurrió un error al iniciar el cliente: {e}")

if __name__ == "__main__":
    start_client()
