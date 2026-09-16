import api from './api';
import { API_ENDPOINTS } from '../utils/constants';

/**
 * Issue Service
 * All API calls related to issues
 */

/**
 * Create a new issue
 * @param {object} issueData - Issue data
 * @returns {Promise} API response
 */
export const createIssue = async (issueData) => {
  const response = await api.post(API_ENDPOINTS.ISSUES.BASE, issueData);
  return response.data;
};

/**
 * Get all issues with filters and pagination
 * @param {object} params - Query parameters
 * @returns {Promise} API response
 */
export const getAllIssues = async (params = {}) => {
  const response = await api.get(API_ENDPOINTS.ISSUES.BASE, { params });
  return response.data;
};

/**
 * Search issues by keyword
 * @param {string} keyword - Search keyword
 * @param {number} page - Page number
 * @param {number} size - Page size
 * @returns {Promise} API response
 */
export const searchIssues = async (keyword, page = 0, size = 10) => {
  const response = await api.get(API_ENDPOINTS.ISSUES.SEARCH, {
    params: { keyword, page, size }
  });
  return response.data;
};

/**
 * Get issue by ID
 * @param {string} id - Issue ID
 * @returns {Promise} API response
 */
export const getIssueById = async (id) => {
  const response = await api.get(API_ENDPOINTS.ISSUES.BY_ID(id));
  return response.data;
};

/**
 * Update an issue
 * @param {string} id - Issue ID
 * @param {object} issueData - Updated issue data
 * @returns {Promise} API response
 */
export const updateIssue = async (id, issueData) => {
  const response = await api.put(API_ENDPOINTS.ISSUES.BY_ID(id), issueData);
  return response.data;
};

/**
 * Delete an issue
 * @param {string} id - Issue ID
 * @returns {Promise} API response
 */
export const deleteIssue = async (id) => {
  const response = await api.delete(API_ENDPOINTS.ISSUES.BY_ID(id));
  return response.data;
};

/**
 * Update issue status
 * @param {string} id - Issue ID
 * @param {string} status - New status
 * @param {string} comment - Optional comment
 * @returns {Promise} API response
 */
export const updateIssueStatus = async (id, status, comment = '') => {
  const response = await api.patch(API_ENDPOINTS.ISSUES.STATUS(id), {
    status,
    comment
  });
  return response.data;
};

/**
 * Update issue priority
 * @param {string} id - Issue ID
 * @param {string} priority - New priority
 * @param {string} comment - Optional comment
 * @returns {Promise} API response
 */
export const updateIssuePriority = async (id, priority, comment = '') => {
  const response = await api.patch(API_ENDPOINTS.ISSUES.PRIORITY(id), {
    priority,
    comment
  });
  return response.data;
};

/**
 * Assign issue to technician
 * @param {string} id - Issue ID
 * @param {string} technicianId - Technician user ID
 * @param {string} comment - Optional comment
 * @returns {Promise} API response
 */
export const assignIssue = async (id, technicianId, comment = '') => {
  const response = await api.patch(API_ENDPOINTS.ISSUES.ASSIGN(id), {
    technicianId,
    comment
  });
  return response.data;
};

/**
 * Get issue comments
 * @param {string} id - Issue ID
 * @returns {Promise} API response
 */
export const getIssueComments = async (id) => {
  const response = await api.get(API_ENDPOINTS.ISSUES.COMMENTS(id));
  return response.data;
};

/**
 * Add comment to issue
 * @param {string} id - Issue ID
 * @param {string} message - Comment message
 * @returns {Promise} API response
 */
export const addIssueComment = async (id, message) => {
  const response = await api.post(API_ENDPOINTS.ISSUES.COMMENTS(id), {
    message
  });
  return response.data;
};

/**
 * Get issue history
 * @param {string} id - Issue ID
 * @returns {Promise} API response
 */
export const getIssueHistory = async (id) => {
  const response = await api.get(API_ENDPOINTS.ISSUES.HISTORY(id));
  return response.data;
};

export default {
  createIssue,
  getAllIssues,
  searchIssues,
  getIssueById,
  updateIssue,
  deleteIssue,
  updateIssueStatus,
  updateIssuePriority,
  assignIssue,
  getIssueComments,
  addIssueComment,
  getIssueHistory
};
