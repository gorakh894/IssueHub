import api from './api';
import { API_ENDPOINTS } from '../utils/constants';

/**
 * User Service
 * All API calls related to users
 */

/**
 * Get all users (Manager/Admin only)
 * @returns {Promise} API response
 */
export const getAllUsers = async () => {
  const response = await api.get(API_ENDPOINTS.USERS.BASE);
  return response.data;
};

/**
 * Get all technicians
 * @returns {Promise} API response
 */
export const getTechnicians = async () => {
  const response = await api.get(API_ENDPOINTS.USERS.TECHNICIANS);
  return response.data;
};

/**
 * Get user by ID
 * @param {string} id - User ID
 * @returns {Promise} API response
 */
export const getUserById = async (id) => {
  const response = await api.get(API_ENDPOINTS.USERS.BY_ID(id));
  return response.data;
};

/**
 * Update user profile
 * @param {string} id - User ID
 * @param {object} userData - Updated user data
 * @returns {Promise} API response
 */
export const updateUser = async (id, userData) => {
  const response = await api.put(API_ENDPOINTS.USERS.BY_ID(id), userData);
  return response.data;
};

/**
 * Upload profile image
 * @param {string} userId - User ID
 * @param {File} file - Image file
 * @returns {Promise} API response
 */
export const uploadProfileImage = async (userId, file) => {
  const formData = new FormData();
  formData.append('file', file);
  
  const response = await api.post(
    API_ENDPOINTS.USERS.PROFILE_IMAGE(userId),
    formData,
    {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    }
  );
  return response.data;
};

/**
 * Delete profile image
 * @param {string} userId - User ID
 * @returns {Promise} API response
 */
export const deleteProfileImage = async (userId) => {
  const response = await api.delete(API_ENDPOINTS.USERS.PROFILE_IMAGE(userId));
  return response.data;
};

export default {
  getAllUsers,
  getTechnicians,
  getUserById,
  updateUser,
  uploadProfileImage,
  deleteProfileImage
};
