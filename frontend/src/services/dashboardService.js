import api from './api';
import { API_ENDPOINTS } from '../utils/constants';

/**
 * Dashboard Service
 * All API calls related to dashboard statistics
 */

/**
 * Get dashboard statistics for current user
 * Role-specific stats will be returned based on user's role
 * @returns {Promise} API response
 */
export const getDashboardStats = async () => {
  const response = await api.get(API_ENDPOINTS.DASHBOARD.STATS);
  return response.data;
};

export default {
  getDashboardStats
};
