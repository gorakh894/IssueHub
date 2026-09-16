import { format, formatDistanceToNow, isAfter, differenceInHours } from 'date-fns';

/**
 * Format date to readable string
 * @param {string|Date} date - Date to format
 * @param {string} formatStr - Format string (default: 'MMM dd, yyyy HH:mm')
 * @returns {string} Formatted date
 */
export const formatDate = (date, formatStr = 'MMM dd, yyyy HH:mm') => {
  if (!date) return 'N/A';
  try {
    return format(new Date(date), formatStr);
  } catch (error) {
    return 'Invalid Date';
  }
};

/**
 * Format date to relative time (e.g., "2 hours ago")
 * @param {string|Date} date - Date to format
 * @returns {string} Relative time string
 */
export const formatRelativeTime = (date) => {
  if (!date) return 'N/A';
  try {
    return formatDistanceToNow(new Date(date), { addSuffix: true });
  } catch (error) {
    return 'Invalid Date';
  }
};

/**
 * Check if SLA is breached
 * @param {string|Date} slaDeadline - SLA deadline
 * @returns {boolean} True if SLA is breached
 */
export const isSLABreached = (slaDeadline) => {
  if (!slaDeadline) return false;
  return isAfter(new Date(), new Date(slaDeadline));
};

/**
 * Get remaining SLA time
 * @param {string|Date} slaDeadline - SLA deadline
 * @returns {object} Object with hours, isBreached, and display text
 */
export const getSLAStatus = (slaDeadline) => {
  if (!slaDeadline) {
    return { hours: null, isBreached: false, text: 'No SLA' };
  }

  const now = new Date();
  const deadline = new Date(slaDeadline);
  const hours = differenceInHours(deadline, now);
  const isBreached = hours < 0;

  if (isBreached) {
    return {
      hours: Math.abs(hours),
      isBreached: true,
      text: `Breached ${Math.abs(hours)}h ago`
    };
  }

  if (hours < 1) {
    return { hours, isBreached: false, text: 'Less than 1 hour' };
  }

  return { hours, isBreached: false, text: `${hours}h remaining` };
};

/**
 * Truncate text to specified length
 * @param {string} text - Text to truncate
 * @param {number} maxLength - Maximum length
 * @returns {string} Truncated text
 */
export const truncateText = (text, maxLength = 100) => {
  if (!text) return '';
  if (text.length <= maxLength) return text;
  return text.substring(0, maxLength) + '...';
};

/**
 * Format file size to human readable format
 * @param {number} bytes - File size in bytes
 * @returns {string} Formatted file size
 */
export const formatFileSize = (bytes) => {
  if (bytes === 0) return '0 Bytes';
  const k = 1024;
  const sizes = ['Bytes', 'KB', 'MB', 'GB'];
  const i = Math.floor(Math.log(bytes) / Math.log(k));
  return Math.round(bytes / Math.pow(k, i) * 100) / 100 + ' ' + sizes[i];
};

/**
 * Validate file type
 * @param {File} file - File to validate
 * @param {string[]} allowedTypes - Allowed MIME types
 * @returns {boolean} True if valid
 */
export const isValidFileType = (file, allowedTypes) => {
  return allowedTypes.includes(file.type);
};

/**
 * Validate file size
 * @param {File} file - File to validate
 * @param {number} maxSize - Maximum size in bytes
 * @returns {boolean} True if valid
 */
export const isValidFileSize = (file, maxSize) => {
  return file.size <= maxSize;
};

/**
 * Capitalize first letter of string
 * @param {string} str - String to capitalize
 * @returns {string} Capitalized string
 */
export const capitalize = (str) => {
  if (!str) return '';
  return str.charAt(0).toUpperCase() + str.slice(1).toLowerCase();
};

/**
 * Get initials from name
 * @param {string} name - Full name
 * @returns {string} Initials
 */
export const getInitials = (name) => {
  if (!name) return '??';
  const parts = name.trim().split(' ');
  if (parts.length === 1) return parts[0].substring(0, 2).toUpperCase();
  return (parts[0][0] + parts[parts.length - 1][0]).toUpperCase();
};

/**
 * Get random color for avatar
 * @param {string} seed - Seed for color generation
 * @returns {string} Tailwind CSS color class
 */
export const getAvatarColor = (seed) => {
  const colors = [
    'bg-blue-500',
    'bg-green-500',
    'bg-yellow-500',
    'bg-red-500',
    'bg-purple-500',
    'bg-pink-500',
    'bg-indigo-500',
    'bg-teal-500'
  ];
  const index = seed ? seed.charCodeAt(0) % colors.length : 0;
  return colors[index];
};

/**
 * Debounce function
 * @param {Function} func - Function to debounce
 * @param {number} delay - Delay in milliseconds
 * @returns {Function} Debounced function
 */
export const debounce = (func, delay = 300) => {
  let timeoutId;
  return (...args) => {
    clearTimeout(timeoutId);
    timeoutId = setTimeout(() => func(...args), delay);
  };
};

/**
 * Download file from URL
 * @param {string} url - File URL
 * @param {string} filename - Desired filename
 */
export const downloadFile = (url, filename) => {
  const link = document.createElement('a');
  link.href = url;
  link.download = filename;
  document.body.appendChild(link);
  link.click();
  document.body.removeChild(link);
};

/**
 * Copy text to clipboard
 * @param {string} text - Text to copy
 * @returns {Promise<boolean>} Success status
 */
export const copyToClipboard = async (text) => {
  try {
    await navigator.clipboard.writeText(text);
    return true;
  } catch (error) {
    console.error('Failed to copy:', error);
    return false;
  }
};

/**
 * Generate issue ID display format
 * @param {string} issueId - Full issue ID
 * @returns {string} Short display ID
 */
export const formatIssueId = (issueId) => {
  if (!issueId) return 'N/A';
  // If it's already in ISS-YYYY-XXXX format, return as is
  if (issueId.startsWith('ISS-')) return issueId;
  // Otherwise return last 8 characters
  return issueId.slice(-8).toUpperCase();
};
