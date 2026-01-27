import axios from 'axios';
import dotenv from 'dotenv';

// 初始化 dotenv
dotenv.config();

const SPRING_BOOT_URL = process.env.SPRING_BOOT_URL || 'http://localhost:8081/api';
const INTERNAL_TOKEN = process.env.INTERNAL_TOKEN || 'wfh-drawio-internal-secret-8888';

const api = {
    /**
     * Check authentication with Spring Boot
     * @param {string} cookie - The cookie string from the request headers
     * @param {string} roomId - The room ID
     * @returns {Promise<Object>} - { userId, permission, ... }
     */
    async checkAuth(token, roomId) {
        try {
            console.log(`[API] Checking auth for room ${roomId} with token:`, token ? 'PRESENT' : 'MISSING');
            const response = await axios.post(`${SPRING_BOOT_URL}/internal/auth`, {
                roomId: roomId,
                token: token // Put token in body
            }, {
                headers: {
                    'Authorization': `Bearer ${token}`, // Also in header just in case
                    'Content-Type': 'application/json'
                }
            });

            if (response.data.code === 0 && response.data.data) {
                return response.data.data;
            }
            return null; // Auth failed
        } catch (error) {
            console.error('Auth check failed:', error.message);
            return null;
        }
    },

    /**
     * Save snapshot to Spring Boot
     * @param {string} roomId
     * @param {string} xml
     * @param {number} lastUpdateId
     */
    async saveSnapshot(roomId, xml, lastUpdateId) {
        try {
            await axios.post(`${SPRING_BOOT_URL}/internal/save`, {
                roomId: parseInt(roomId),
                xml: xml,
                lastUpdateId: lastUpdateId
            }, {
                headers: {
                    'X-Internal-Token': INTERNAL_TOKEN,
                    'Content-Type': 'application/json'
                }
            });
            console.log(`[${roomId}] Snapshot saved successfully.`);
        } catch (error) {
            console.error(`[${roomId}] Snapshot save failed:`, error.response?.data || error.message);
        }
    }
};

// 仅保留 ES Modules 的导出方式
export default api;