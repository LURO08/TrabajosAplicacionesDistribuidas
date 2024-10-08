import mariadb from 'mariadb';
import dotenv from 'dotenv';

// Cargar variables de entorno
dotenv.config();

// Configuración de conexión utilizando variables de entorno
const config = {
    host: process.env.DB_HOST || '127.0.0.1',
    user: process.env.DB_USER || 'root',
    password: process.env.DB_PASSWORD || '',
    database: process.env.DB_NAME || 'sockets',
    connectionLimit: 5 // Limite de conexiones simultáneas en el pool
};

class DBConnector {
    constructor() {
        this.dbconnector = mariadb.createPool(config); // Crear pool de conexiones
    }

    async query(sql, params = []) {
        let conn;
        try {
            conn = await this.dbconnector.getConnection(); // Obtener conexión del pool
            const result = await conn.query(sql, params); // Ejecutar consulta con parámetros opcionales
            return result; // Retornar el resultado
        } catch (err) {
            console.error('Error en la consulta: ', err);
            throw err; // Relanzar el error para manejarlo externamente
        } finally {
            if (conn) conn.release(); // Liberar la conexión al pool
        }
    }

    // Método para cerrar el pool de conexiones
    async closePool() {
        try {
            await this.dbconnector.end();
            console.log('Conexión al pool cerrada.');
        } catch (err) {
            console.error('Error al cerrar el pool de conexiones: ', err);
        }
    }
}

// Exportar una instancia de la clase DBConnector
export default new DBConnector();
