import api from './api';
import { API_ENDPOINTS } from '../utils/constants';

/**
 * Category Service
 * All API calls related to categories
 */

/**
 * Get all categories
 * @returns {Promise} API response
 */
export const getAllCategories = async () => {
  const response = await api.get(API_ENDPOINTS.CATEGORIES.BASE);
  return response.data;
};

/**
 * Get category by ID
 * @param {string} id - Category ID
 * @returns {Promise} API response
 */
export const getCategoryById = async (id) => {
  const response = await api.get(API_ENDPOINTS.CATEGORIES.BY_ID(id));
  return response.data;
};

/**
 * Create a new category
 * @param {object} categoryData - Category data
 * @returns {Promise} API response
 */
export const createCategory = async (categoryData) => {
  const response = await api.post(API_ENDPOINTS.CATEGORIES.BASE, categoryData);
  return response.data;
};

/**
 * Update a category
 * @param {string} id - Category ID
 * @param {object} categoryData - Updated category data
 * @returns {Promise} API response
 */
export const updateCategory = async (id, categoryData) => {
  const response = await api.put(API_ENDPOINTS.CATEGORIES.BY_ID(id), categoryData);
  return response.data;
};

/**
 * Delete a category
 * @param {string} id - Category ID
 * @returns {Promise} API response
 */
export const deleteCategory = async (id) => {
  const response = await api.delete(API_ENDPOINTS.CATEGORIES.BY_ID(id));
  return response.data;
};

export default {
  getAllCategories,
  getCategoryById,
  createCategory,
  updateCategory,
  deleteCategory
};
