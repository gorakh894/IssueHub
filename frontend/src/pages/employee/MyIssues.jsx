import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import { getAllIssues, searchIssues } from '../../services/issueService';
import { STATUS, PRIORITY, PAGINATION } from '../../utils/constants';
import { debounce } from '../../utils/helpers';
import IssueCard from '../../components/IssueCard';
import LoadingSpinner from '../../components/LoadingSpinner';
import toast from 'react-hot-toast';

const MyIssues = () => {
  const { user } = useAuth();
  const [issues, setIssues] = useState([]);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState('');
  const [filters, setFilters] = useState({
    status: '',
    priority: '',
    page: 0,
    size: PAGINATION.DEFAULT_SIZE
  });
  const [pagination, setPagination] = useState({
    totalPages: 0,
    totalElements: 0,
    currentPage: 0
  });

  useEffect(() => {
    fetchIssues();
  }, [filters]);

  const fetchIssues = async () => {
    setLoading(true);
    try {
      const params = {
        reportedBy: user.id,
        page: filters.page,
        size: filters.size,
        sortBy: 'createdAt',
        sortDir: 'DESC'
      };

      if (filters.status) params.status = filters.status;
      if (filters.priority) params.priority = filters.priority;

      const response = await getAllIssues(params);
      setIssues(response.data.content || []);
      setPagination({
        totalPages: response.data.totalPages,
        totalElements: response.data.totalElements,
        currentPage: response.data.number
      });
    } catch (error) {
      console.error('Error fetching issues:', error);
      toast.error('Failed to load issues');
    } finally {
      setLoading(false);
    }
  };

  const handleSearch = debounce(async (keyword) => {
    if (!keyword.trim()) {
      fetchIssues();
      return;
    }

    setLoading(true);
    try {
      const response = await searchIssues(keyword, filters.page, filters.size);
      // Filter by current user
      const userIssues = response.data.content.filter(
        issue => issue.reportedBy.id === user.id
      );
      setIssues(userIssues);
      setPagination({
        totalPages: response.data.totalPages,
        totalElements: userIssues.length,
        currentPage: response.data.number
      });
    } catch (error) {
      console.error('Error searching issues:', error);
      toast.error('Search failed');
    } finally {
      setLoading(false);
    }
  }, 500);

  const handleSearchChange = (e) => {
    const value = e.target.value;
    setSearchTerm(value);
    handleSearch(value);
  };

  const handleFilterChange = (key, value) => {
    setFilters(prev => ({
      ...prev,
      [key]: value,
      page: 0 // Reset to first page on filter change
    }));
  };

  const handlePageChange = (newPage) => {
    setFilters(prev => ({ ...prev, page: newPage }));
  };

  const clearFilters = () => {
    setSearchTerm('');
    setFilters({
      status: '',
      priority: '',
      page: 0,
      size: PAGINATION.DEFAULT_SIZE
    });
  };

  const hasActiveFilters = filters.status || filters.priority || searchTerm;

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex justify-between items-center">
        <div>
          <h1 className="text-3xl font-bold text-gray-900">My Issues</h1>
          <p className="text-gray-600 mt-1">
            {pagination.totalElements} issue(s) found
          </p>
        </div>
        <Link to="/employee/create-issue" className="btn btn-primary">
          + Create Issue
        </Link>
      </div>

      {/* Search and Filters */}
      <div className="card">
        <div className="space-y-4">
          {/* Search Bar */}
          <div>
            <input
              type="text"
              value={searchTerm}
              onChange={handleSearchChange}
              placeholder="Search issues by title or description..."
              className="input"
            />
          </div>

          {/* Filters */}
          <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
            <div>
              <label className="label">Status</label>
              <select
                value={filters.status}
                onChange={(e) => handleFilterChange('status', e.target.value)}
                className="input"
              >
                <option value="">All Statuses</option>
                <option value={STATUS.REPORTED}>Reported</option>
                <option value={STATUS.UNDER_REVIEW}>Under Review</option>
                <option value={STATUS.ASSIGNED}>Assigned</option>
                <option value={STATUS.IN_PROGRESS}>In Progress</option>
                <option value={STATUS.RESOLVED}>Resolved</option>
                <option value={STATUS.CLOSED}>Closed</option>
                <option value={STATUS.REOPENED}>Reopened</option>
                <option value={STATUS.ON_HOLD}>On Hold</option>
                <option value={STATUS.CANCELLED}>Cancelled</option>
              </select>
            </div>

            <div>
              <label className="label">Priority</label>
              <select
                value={filters.priority}
                onChange={(e) => handleFilterChange('priority', e.target.value)}
                className="input"
              >
                <option value="">All Priorities</option>
                <option value={PRIORITY.LOW}>⬇️ Low</option>
                <option value={PRIORITY.MEDIUM}>➡️ Medium</option>
                <option value={PRIORITY.HIGH}>⬆️ High</option>
                <option value={PRIORITY.CRITICAL}>🔥 Critical</option>
              </select>
            </div>

            <div>
              <label className="label">Items per page</label>
              <select
                value={filters.size}
                onChange={(e) => handleFilterChange('size', parseInt(e.target.value))}
                className="input"
              >
                {PAGINATION.SIZE_OPTIONS.map(size => (
                  <option key={size} value={size}>{size}</option>
                ))}
              </select>
            </div>
          </div>

          {/* Clear Filters */}
          {hasActiveFilters && (
            <div className="flex justify-end">
              <button onClick={clearFilters} className="btn btn-secondary">
                Clear Filters
              </button>
            </div>
          )}
        </div>
      </div>

      {/* Issues List */}
      {loading ? (
        <div className="flex justify-center py-12">
          <LoadingSpinner size="lg" />
        </div>
      ) : issues.length === 0 ? (
        <div className="card text-center py-12">
          <svg
            className="mx-auto h-16 w-16 text-gray-400 mb-4"
            fill="none"
            stroke="currentColor"
            viewBox="0 0 24 24"
          >
            <path
              strokeLinecap="round"
              strokeLinejoin="round"
              strokeWidth={2}
              d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z"
            />
          </svg>
          <h3 className="text-lg font-medium text-gray-900 mb-2">No issues found</h3>
          <p className="text-gray-500 mb-4">
            {hasActiveFilters
              ? 'Try adjusting your filters or search term'
              : "You haven't created any issues yet"}
          </p>
          {!hasActiveFilters && (
            <Link to="/employee/create-issue" className="btn btn-primary">
              Create Your First Issue
            </Link>
          )}
        </div>
      ) : (
        <div className="grid grid-cols-1 gap-4">
          {issues.map(issue => (
            <IssueCard
              key={issue.id}
              issue={issue}
              showAssignee={true}
            />
          ))}
        </div>
      )}

      {/* Pagination */}
      {!loading && pagination.totalPages > 1 && (
        <div className="card">
          <div className="flex items-center justify-between">
            <div className="text-sm text-gray-600">
              Page {pagination.currentPage + 1} of {pagination.totalPages}
            </div>
            
            <div className="flex gap-2">
              <button
                onClick={() => handlePageChange(pagination.currentPage - 1)}
                disabled={pagination.currentPage === 0}
                className="btn btn-secondary"
              >
                Previous
              </button>
              
              {/* Page Numbers */}
              <div className="flex gap-1">
                {[...Array(pagination.totalPages)].map((_, index) => {
                  // Show first, last, current, and adjacent pages
                  if (
                    index === 0 ||
                    index === pagination.totalPages - 1 ||
                    Math.abs(index - pagination.currentPage) <= 1
                  ) {
                    return (
                      <button
                        key={index}
                        onClick={() => handlePageChange(index)}
                        className={`px-3 py-1 rounded ${
                          index === pagination.currentPage
                            ? 'bg-blue-600 text-white'
                            : 'bg-gray-200 text-gray-700 hover:bg-gray-300'
                        }`}
                      >
                        {index + 1}
                      </button>
                    );
                  } else if (
                    index === pagination.currentPage - 2 ||
                    index === pagination.currentPage + 2
                  ) {
                    return <span key={index} className="px-2">...</span>;
                  }
                  return null;
                })}
              </div>
              
              <button
                onClick={() => handlePageChange(pagination.currentPage + 1)}
                disabled={pagination.currentPage === pagination.totalPages - 1}
                className="btn btn-secondary"
              >
                Next
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default MyIssues;
