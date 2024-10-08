import socket
import threading

def handle_receive_TCP(client_socketTCP, client_socketUDP, host, portUDP):
    """Función para recibir mensajes del servidor TCP y reenviar al servidor UDP"""
    while True:
        try:
            # Recibir respuesta del servidor TCP
            responseTCP = client_socketTCP.recv(1024).decode()
            
            if not responseTCP:
                break

            print(f"Respuesta del servidor TCP: {responseTCP}")

            # Reenviar el mensaje recibido por TCP al servidor UDP
            client_socketUDP.sendto(responseTCP.encode(), (host, portUDP))
            print(f"Mensaje reenviado al servidor UDP en {host}:{portUDP}")
        
        except Exception as e:
            print(f"Error al recibir mensaje TCP: {e}")
            break

def handle_receive_UDP(client_socketUDP):
    """Función para recibir mensajes del servidor UDP"""
    while True:
        try:
            # Recibir respuesta del servidor UDP
            data, _ = client_socketUDP.recvfrom(1024) 
            print(f"Respuesta del servidor UDP desde: {data.decode()}")
        except socket.timeout:
                # Manejo del timeout sin errores críticos
                continue

        except Exception as e:
            print(f"Error al recibir mensaje UDP: {e}")
            break
            
def start_client():
    host = '192.168.212.2'
    portTCP = 8080  # Puerto TCP
    hostUDP = '192.168.211.187'
    portUDP = 8081  # Puerto UDP
    
    try:
        # Crear socket TCP
        client_socketTCP = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        # Crear socket UDP
        client_socketUDP = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
        
        # Conectar al servidor TCP
        client_socketTCP.connect((host, portTCP))
        print(f"Conectado al servidor TCP en {host}:{portTCP}")
        
        # Crear y lanzar hilos para manejar el envío y recepción simultánea
        tcp_receive_thread = threading.Thread(target=handle_receive_TCP, args=(client_socketTCP, client_socketUDP, host, portUDP))
        udp_receive_thread = threading.Thread(target=handle_receive_UDP, args=(client_socketUDP,))

        tcp_receive_thread.start()
        udp_receive_thread.start()

        # Esperar a que ambos hilos terminen
        tcp_receive_thread.join()
        udp_receive_thread.join()

    except Exception as e:
        print(f"Ocurrió un error: {e}")

if __name__ == "__main__":
    start_client()
