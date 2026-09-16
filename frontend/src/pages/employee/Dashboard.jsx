import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import { getDashboardStats } from '../../services/dashboardService';
import { getAllIssues } from '../../services/issueService';
import { STATUS, PRIORITY } from '../../utils/constants';
import { formatRelativeTime, getSLAStatus } from '../../utils/helpers';
import StatusBadge from '../../components/StatusBadge';
import PriorityBadge from '../../components/PriorityBadge';
import LoadingSpinner from '../../components/LoadingSpinner';
import toast from 'react-hot-toast';

const EmployeeDashboard = () => {
  const { user } = useAuth();
  const [stats, setStats] = useState(null);
  const [recentIssues, setRecentIssues] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchDashboardData();
  }, []);

  const fetchDashboardData = async () => {
    setLoading(true);
    try {
      // Fetch dashboard stats
      const statsResponse = await getDashboardStats();
      setStats(statsResponse.data);

      // Fetch recent issues
      const issuesResponse = await getAllIssues({
        reportedBy: user.id,
        page: 0,
        size: 5,
        sortBy: 'createdAt',
        sortDir: 'DESC'
      });
      setRecentIssues(issuesResponse.data.content || []);
    } catch (error) {
      console.error('Error fetching dashboard data:', error);
      toast.error('Failed to load dashboard');
    } finally {
      setLoading(false);
    }
  };

  if (loading) {
    return (
      <div className="flex justify-center items-center h-96">
        <LoadingSpinner size="xl" />
      </div>
    );
  }

  const statusCounts = stats?.issuesByStatus || [];
  const priorityCounts = stats?.issuesByPriority || [];

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex justify-between items-center">
        <div>
          <h1 className="text-3xl font-bold text-gray-900">Dashboard</h1>
          <p className="text-gray-600 mt-1">Welcome back, {user?.name}!</p>
        </div>
        <Link to="/employee/create-issue" className="btn btn-primary">
          + Create Issue
        </Link>
      </div>

      {/* Statistics Cards */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
        <div className="card bg-gradient-to-br from-blue-500 to-blue-600 text-white">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-blue-100 text-sm font-medium">Total Issues</p>
              <p className="text-3xl font-bold mt-2">{stats?.totalIssues || 0}</p>
            </div>
            <div className="bg-white bg-opacity-20 p-3 rounded-lg">
              <svg className="w-8 h-8" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z" />
              </svg>
            </div>
          </div>
        </div>

        <div className="card bg-gradient-to-br from-yellow-500 to-yellow-600 text-white">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-yellow-100 text-sm font-medium">In Progress</p>
              <p className="text-3xl font-bold mt-2">{stats?.inProgressIssues || 0}</p>
            </div>
            <div className="bg-white bg-opacity-20 p-3 rounded-lg">
              <svg className="w-8 h-8" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z" />
              </svg>
            </div>
          </div>
        </div>

        <div className="card bg-gradient-to-br from-green-500 to-green-600 text-white">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-green-100 text-sm font-medium">Resolved</p>
              <p className="text-3xl font-bold mt-2">{stats?.resolvedIssues || 0}</p>
            </div>
            <div className="bg-white bg-opacity-20 p-3 rounded-lg">
              <svg className="w-8 h-8" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" />
              </svg>
            </div>
          </div>
        </div>

        <div className="card bg-gradient-to-br from-red-500 to-red-600 text-white">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-red-100 text-sm font-medium">SLA Breached</p>
              <p className="text-3xl font-bold mt-2">{stats?.slaBreachedIssues || 0}</p>
            </div>
            <div className="bg-white bg-opacity-20 p-3 rounded-lg">
              <svg className="w-8 h-8" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z" />
              </svg>
            </div>
          </div>
        </div>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        {/* Issues by Status */}
        <div className="card">
          <h3 className="text-lg font-semibold mb-4">Issues by Status</h3>
          {statusCounts.length === 0 ? (
            <p className="text-gray-500 text-center py-8">No data available</p>
          ) : (
            <div className="space-y-3">
              {statusCounts.map(item => (
                <div key={item.status} className="flex items-center justify-between">
                  <div className="flex items-center gap-2">
                    <StatusBadge status={item.status} />
                  </div>
                  <div className="flex items-center gap-3">
                    <div className="w-32 bg-gray-200 rounded-full h-2">
                      <div
                        className="bg-blue-600 h-2 rounded-full"
                        style={{
                          width: `${stats.totalIssues > 0 ? (item.count / stats.totalIssues) * 100 : 0}%`
                        }}
                      ></div>
                    </div>
                    <span className="font-semibold text-gray-900 w-8 text-right">
                      {item.count}
                    </span>
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>

        {/* Issues by Priority */}
        <div className="card">
          <h3 className="text-lg font-semibold mb-4">Issues by Priority</h3>
          {priorityCounts.length === 0 ? (
            <p className="text-gray-500 text-center py-8">No data available</p>
          ) : (
            <div className="space-y-3">
              {priorityCounts.map(item => (
                <div key={item.priority} className="flex items-center justify-between">
                  <div className="flex items-center gap-2">
                    <PriorityBadge priority={item.priority} />
                  </div>
                  <div className="flex items-center gap-3">
                    <div className="w-32 bg-gray-200 rounded-full h-2">
                      <div
                        className="bg-orange-600 h-2 rounded-full"
                        style={{
                          width: `${stats.totalIssues > 0 ? (item.count / stats.totalIssues) * 100 : 0}%`
                        }}
                      ></div>
                    </div>
                    <span className="font-semibold text-gray-900 w-8 text-right">
                      {item.count}
                    </span>
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>
      </div>

      {/* Recent Issues */}
      <div className="card">
        <div className="flex justify-between items-center mb-4">
          <h3 className="text-lg font-semibold">Recent Issues</h3>
          <Link to="/employee/my-issues" className="text-blue-600 hover:text-blue-800 text-sm">
            View All →
          </Link>
        </div>

        {recentIssues.length === 0 ? (
          <div className="text-center py-8">
            <svg
              className="mx-auto h-12 w-12 text-gray-400 mb-3"
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
            <p className="text-gray-500 mb-4">No issues yet</p>
            <Link to="/employee/create-issue" className="btn btn-primary">
              Create Your First Issue
            </Link>
          </div>
        ) : (
          <div className="space-y-3">
            {recentIssues.map(issue => {
              const slaStatus = getSLAStatus(issue.slaDeadline);
              return (
                <Link
                  key={issue.id}
                  to={`/employee/issues/${issue.id}`}
                  className="block border border-gray-200 rounded-lg p-4 hover:border-blue-500 hover:shadow-md transition-all"
                >
                  <div className="flex items-start justify-between">
                    <div className="flex-1">
                      <div className="flex items-center gap-2 mb-2">
                        <span className="text-xs text-gray-500 font-mono">
                          {issue.issueId}
                        </span>
                        <PriorityBadge priority={issue.priority} />
                        <StatusBadge status={issue.status} />
                      </div>
                      <h4 className="font-medium text-gray-900 mb-1">
                        {issue.title}
                      </h4>
                      <p className="text-sm text-gray-600 mb-2">
                        {issue.category?.name} • {issue.location?.building}, {issue.location?.room}
                      </p>
                      <div className="flex items-center gap-4 text-xs text-gray-500">
                        <span>Created {formatRelativeTime(issue.createdAt)}</span>
                        {issue.slaDeadline && (
                          <span className={slaStatus.isBreached ? 'text-red-600 font-medium' : 'text-green-600'}>
                            SLA: {slaStatus.text}
                          </span>
                        )}
                      </div>
                    </div>
                  </div>
                </Link>
              );
            })}
          </div>
        )}
      </div>

      {/* Quick Actions */}
      <div className="card bg-gradient-to-r from-blue-50 to-indigo-50 border-blue-200">
        <h3 className="text-lg font-semibold mb-4 text-gray-900">Quick Actions</h3>
        <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
          <Link
            to="/employee/create-issue"
            className="flex items-center gap-3 p-4 bg-white rounded-lg hover:shadow-md transition-shadow"
          >
            <div className="bg-blue-100 p-3 rounded-lg">
              <svg className="w-6 h-6 text-blue-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 4v16m8-8H4" />
              </svg>
            </div>
            <div>
              <p className="font-medium text-gray-900">Create Issue</p>
              <p className="text-sm text-gray-500">Report new problem</p>
            </div>
          </Link>

          <Link
            to="/employee/my-issues"
            className="flex items-center gap-3 p-4 bg-white rounded-lg hover:shadow-md transition-shadow"
          >
            <div className="bg-green-100 p-3 rounded-lg">
              <svg className="w-6 h-6 text-green-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2" />
              </svg>
            </div>
            <div>
              <p className="font-medium text-gray-900">View All Issues</p>
              <p className="text-sm text-gray-500">Track your issues</p>
            </div>
          </Link>

          <Link
            to="/profile"
            className="flex items-center gap-3 p-4 bg-white rounded-lg hover:shadow-md transition-shadow"
          >
            <div className="bg-purple-100 p-3 rounded-lg">
              <svg className="w-6 h-6 text-purple-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
              </svg>
            </div>
            <div>
              <p className="font-medium text-gray-900">My Profile</p>
              <p className="text-sm text-gray-500">Update your info</p>
            </div>
          </Link>
        </div>
      </div>
    </div>
  );
};

export default EmployeeDashboard;
