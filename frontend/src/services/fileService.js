import api from './api';
import { API_ENDPOINTS } from '../utils/constants';

/**
 * File Service
 * All API calls related to file uploads
 */

/**
 * Upload a single file
 * @param {File} file - File to upload
 * @returns {Promise} API response with file URL
 */
export const uploadFile = async (file) => {
  const formData = new FormData();
  formData.append('file', file);
  
  const response = await api.post(API_ENDPOINTS.FILES.UPLOAD, formData, {
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  });
  return response.data;
};

/**
 * Upload multiple files
 * @param {File[]} files - Array of files to upload
 * @returns {Promise} API response with array of file URLs
 */
export const uploadMultipleFiles = async (files) => {
  const formData = new FormData();
  files.forEach(file => {
    formData.append('files', file);
  });
  
  const response = await api.post(API_ENDPOINTS.FILES.UPLOAD_MULTIPLE, formData, {
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  });
  return response.data;
};

export default {
  uploadFile,
  uploadMultipleFiles
};
