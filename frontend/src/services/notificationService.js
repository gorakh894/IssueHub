import api from './api';
import { API_ENDPOINTS } from '../utils/constants';

/**
 * Notification Service
 * All API calls related to notifications
 */

/**
 * Get all notifications for current user
 * @param {number} page - Page number
 * @param {number} size - Page size
 * @returns {Promise} API response
 */
export const getNotifications = async (page = 0, size = 20) => {
  const response = await api.get(API_ENDPOINTS.NOTIFICATIONS.BASE, {
    params: { page, size }
  });
  return response.data;
};

/**
 * Get unread notification count
 * @returns {Promise} API response
 */
export const getUnreadCount = async () => {
  const response = await api.get(API_ENDPOINTS.NOTIFICATIONS.UNREAD_COUNT);
  return response.data;
};

/**
 * Mark notification as read
 * @param {string} id - Notification ID
 * @returns {Promise} API response
 */
export const markAsRead = async (id) => {
  const response = await api.patch(API_ENDPOINTS.NOTIFICATIONS.MARK_READ(id));
  return response.data;
};

/**
 * Mark all notifications as read
 * @returns {Promise} API response
 */
export const markAllAsRead = async () => {
  const response = await api.patch(API_ENDPOINTS.NOTIFICATIONS.MARK_ALL_READ);
  return response.data;
};

/**
 * Delete a notification
 * @param {string} id - Notification ID
 * @returns {Promise} API response
 */
export const deleteNotification = async (id) => {
  const response = await api.delete(API_ENDPOINTS.NOTIFICATIONS.BY_ID(id));
  return response.data;
};

export default {
  getNotifications,
  getUnreadCount,
  markAsRead,
  markAllAsRead,
  deleteNotification
};
