// Status enum and configuration
export const STATUS = {
  REPORTED: 'REPORTED',
  UNDER_REVIEW: 'UNDER_REVIEW',
  ASSIGNED: 'ASSIGNED',
  IN_PROGRESS: 'IN_PROGRESS',
  RESOLVED: 'RESOLVED',
  CLOSED: 'CLOSED',
  REOPENED: 'REOPENED',
  CANCELLED: 'CANCELLED',
  ON_HOLD: 'ON_HOLD'
};

export const STATUS_CONFIG = {
  [STATUS.REPORTED]: {
    label: 'Reported',
    color: 'bg-gray-100 text-gray-800',
    badgeColor: 'badge-secondary'
  },
  [STATUS.UNDER_REVIEW]: {
    label: 'Under Review',
    color: 'bg-blue-100 text-blue-800',
    badgeColor: 'badge-info'
  },
  [STATUS.ASSIGNED]: {
    label: 'Assigned',
    color: 'bg-purple-100 text-purple-800',
    badgeColor: 'badge-primary'
  },
  [STATUS.IN_PROGRESS]: {
    label: 'In Progress',
    color: 'bg-yellow-100 text-yellow-800',
    badgeColor: 'badge-warning'
  },
  [STATUS.RESOLVED]: {
    label: 'Resolved',
    color: 'bg-green-100 text-green-800',
    badgeColor: 'badge-success'
  },
  [STATUS.CLOSED]: {
    label: 'Closed',
    color: 'bg-gray-100 text-gray-600',
    badgeColor: 'badge-secondary'
  },
  [STATUS.REOPENED]: {
    label: 'Reopened',
    color: 'bg-orange-100 text-orange-800',
    badgeColor: 'badge-warning'
  },
  [STATUS.CANCELLED]: {
    label: 'Cancelled',
    color: 'bg-red-100 text-red-800',
    badgeColor: 'badge-danger'
  },
  [STATUS.ON_HOLD]: {
    label: 'On Hold',
    color: 'bg-gray-100 text-gray-800',
    badgeColor: 'badge-secondary'
  }
};

// Priority enum and configuration
export const PRIORITY = {
  LOW: 'LOW',
  MEDIUM: 'MEDIUM',
  HIGH: 'HIGH',
  CRITICAL: 'CRITICAL'
};

export const PRIORITY_CONFIG = {
  [PRIORITY.LOW]: {
    label: 'Low',
    color: 'bg-green-100 text-green-800',
    badgeColor: 'badge-success',
    icon: '⬇️'
  },
  [PRIORITY.MEDIUM]: {
    label: 'Medium',
    color: 'bg-yellow-100 text-yellow-800',
    badgeColor: 'badge-warning',
    icon: '➡️'
  },
  [PRIORITY.HIGH]: {
    label: 'High',
    color: 'bg-orange-100 text-orange-800',
    badgeColor: 'badge-warning',
    icon: '⬆️'
  },
  [PRIORITY.CRITICAL]: {
    label: 'Critical',
    color: 'bg-red-100 text-red-800',
    badgeColor: 'badge-danger',
    icon: '🔥'
  }
};

// User roles
export const ROLE = {
  EMPLOYEE: 'EMPLOYEE',
  MANAGER: 'MANAGER',
  TECHNICIAN: 'TECHNICIAN',
  ADMIN: 'ADMIN'
};

// API endpoints
export const API_ENDPOINTS = {
  AUTH: {
    LOGIN: '/auth/login',
    REGISTER: '/auth/register',
    ME: '/auth/me'
  },
  ISSUES: {
    BASE: '/issues',
    SEARCH: '/issues/search',
    BY_ID: (id) => `/issues/${id}`,
    COMMENTS: (id) => `/issues/${id}/comments`,
    HISTORY: (id) => `/issues/${id}/history`,
    STATUS: (id) => `/issues/${id}/status`,
    PRIORITY: (id) => `/issues/${id}/priority`,
    ASSIGN: (id) => `/issues/${id}/assign`
  },
  CATEGORIES: {
    BASE: '/categories',
    BY_ID: (id) => `/categories/${id}`
  },
  USERS: {
    BASE: '/users',
    BY_ID: (id) => `/users/${id}`,
    TECHNICIANS: '/users/technicians',
    PROFILE_IMAGE: (id) => `/users/${id}/profile-image`
  },
  NOTIFICATIONS: {
    BASE: '/notifications',
    BY_ID: (id) => `/notifications/${id}`,
    MARK_READ: (id) => `/notifications/${id}/mark-read`,
    MARK_ALL_READ: '/notifications/mark-all-read',
    UNREAD_COUNT: '/notifications/unread-count'
  },
  DASHBOARD: {
    STATS: '/dashboard/stats'
  },
  FILES: {
    UPLOAD: '/files/upload',
    UPLOAD_MULTIPLE: '/files/upload-multiple'
  }
};

// Pagination defaults
export const PAGINATION = {
  DEFAULT_PAGE: 0,
  DEFAULT_SIZE: 10,
  SIZE_OPTIONS: [5, 10, 20, 50]
};

// File upload configuration
export const FILE_UPLOAD = {
  MAX_SIZE: 10 * 1024 * 1024, // 10MB in bytes
  MAX_FILES: 5,
  ALLOWED_TYPES: ['image/jpeg', 'image/jpg', 'image/png', 'image/webp', 'image/gif'],
  ALLOWED_EXTENSIONS: ['.jpg', '.jpeg', '.png', '.webp', '.gif']
};

// SLA configuration (in hours)
export const SLA = {
  [PRIORITY.LOW]: 72,
  [PRIORITY.MEDIUM]: 48,
  [PRIORITY.HIGH]: 24,
  [PRIORITY.CRITICAL]: 4
};
