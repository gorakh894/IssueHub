import { useState, useEffect } from 'react';
import { useParams, useNavigate, Link } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import {
  getIssueById,
  getIssueComments,
  addIssueComment,
  getIssueHistory
} from '../../services/issueService';
import { formatDate, formatRelativeTime, getSLAStatus } from '../../utils/helpers';
import StatusBadge from '../../components/StatusBadge';
import PriorityBadge from '../../components/PriorityBadge';
import LoadingSpinner from '../../components/LoadingSpinner';
import Modal from '../../components/Modal';
import toast from 'react-hot-toast';

const IssueDetails = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const { user } = useAuth();

  const [issue, setIssue] = useState(null);
  const [comments, setComments] = useState([]);
  const [history, setHistory] = useState([]);
  const [loading, setLoading] = useState(true);
  const [activeTab, setActiveTab] = useState('details'); // details, comments, history

  const [newComment, setNewComment] = useState('');
  const [submittingComment, setSubmittingComment] = useState(false);

  const [selectedImage, setSelectedImage] = useState(null);
  const [showImageModal, setShowImageModal] = useState(false);

  useEffect(() => {
    fetchIssueData();
  }, [id]);

  const fetchIssueData = async () => {
    setLoading(true);
    try {
      const [issueRes, commentsRes, historyRes] = await Promise.all([
        getIssueById(id),
        getIssueComments(id),
        getIssueHistory(id)
      ]);

      setIssue(issueRes.data);
      setComments(commentsRes.data || []);
      setHistory(historyRes.data || []);
    } catch (error) {
      console.error('Error fetching issue:', error);
      toast.error('Failed to load issue details');
      navigate('/employee/my-issues');
    } finally {
      setLoading(false);
    }
  };

  const handleAddComment = async (e) => {
    e.preventDefault();
    
    if (!newComment.trim()) {
      toast.error('Comment cannot be empty');
      return;
    }

    setSubmittingComment(true);
    try {
      const response = await addIssueComment(id, newComment);
      setComments(prev => [...prev, response.data]);
      setNewComment('');
      toast.success('Comment added');
    } catch (error) {
      console.error('Error adding comment:', error);
      toast.error('Failed to add comment');
    } finally {
      setSubmittingComment(false);
    }
  };

  const openImageModal = (imageUrl) => {
    setSelectedImage(imageUrl);
    setShowImageModal(true);
  };

  if (loading) {
    return (
      <div className="flex justify-center items-center h-96">
        <LoadingSpinner size="xl" />
      </div>
    );
  }

  if (!issue) {
    return (
      <div className="text-center py-12">
        <p className="text-gray-500">Issue not found</p>
      </div>
    );
  }

  const slaStatus = getSLAStatus(issue.slaDeadline);

  return (
    <div className="space-y-6">
      {/* Breadcrumb */}
      <nav className="flex items-center gap-2 text-sm text-gray-600">
        <Link to="/employee/my-issues" className="hover:text-blue-600">
          My Issues
        </Link>
        <span>/</span>
        <span className="text-gray-900 font-medium">{issue.issueId}</span>
      </nav>

      {/* Header */}
      <div className="card">
        <div className="flex items-start justify-between mb-4">
          <div className="flex-1">
            <div className="flex items-center gap-2 mb-2">
              <span className="text-sm text-gray-500 font-mono">{issue.issueId}</span>
              <PriorityBadge priority={issue.priority} />
              <StatusBadge status={issue.status} />
              {issue.slaBreached && (
                <span className="badge badge-danger">SLA Breached</span>
              )}
            </div>
            <h1 className="text-3xl font-bold text-gray-900">{issue.title}</h1>
          </div>
        </div>

        {/* Meta Information */}
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4 mt-6 pt-6 border-t">
          <div>
            <p className="text-sm text-gray-500">Reported By</p>
            <p className="font-medium text-gray-900">{issue.reportedBy?.name}</p>
            <p className="text-sm text-gray-500">{issue.reportedBy?.email}</p>
          </div>

          <div>
            <p className="text-sm text-gray-500">Category</p>
            <p className="font-medium text-gray-900">{issue.category?.name}</p>
            {issue.category?.description && (
              <p className="text-sm text-gray-500">{issue.category.description}</p>
            )}
          </div>

          <div>
            <p className="text-sm text-gray-500">Location</p>
            <p className="font-medium text-gray-900">{issue.location?.building}</p>
            <p className="text-sm text-gray-500">
              Floor {issue.location?.floor}, {issue.location?.room}
            </p>
          </div>

          <div>
            <p className="text-sm text-gray-500">Assigned To</p>
            {issue.assignedTo ? (
              <>
                <p className="font-medium text-gray-900">{issue.assignedTo.name}</p>
                <p className="text-sm text-gray-500">{issue.assignedTo.email}</p>
              </>
            ) : (
              <p className="text-gray-500">Not assigned yet</p>
            )}
          </div>
        </div>

        {/* Dates and SLA */}
        <div className="grid grid-cols-1 md:grid-cols-3 gap-4 mt-4 pt-4 border-t">
          <div>
            <p className="text-sm text-gray-500">Created</p>
            <p className="font-medium text-gray-900">{formatDate(issue.createdAt)}</p>
            <p className="text-sm text-gray-500">{formatRelativeTime(issue.createdAt)}</p>
          </div>

          {issue.updatedAt && issue.updatedAt !== issue.createdAt && (
            <div>
              <p className="text-sm text-gray-500">Last Updated</p>
              <p className="font-medium text-gray-900">{formatDate(issue.updatedAt)}</p>
              <p className="text-sm text-gray-500">{formatRelativeTime(issue.updatedAt)}</p>
            </div>
          )}

          {issue.slaDeadline && (
            <div>
              <p className="text-sm text-gray-500">SLA Deadline</p>
              <p className="font-medium text-gray-900">{formatDate(issue.slaDeadline)}</p>
              <p
                className={`text-sm font-medium ${
                  slaStatus.isBreached ? 'text-red-600' : 'text-green-600'
                }`}
              >
                {slaStatus.text}
              </p>
            </div>
          )}
        </div>
      </div>

      {/* Tabs */}
      <div className="border-b border-gray-200">
        <div className="flex gap-6">
          <button
            onClick={() => setActiveTab('details')}
            className={`pb-3 px-1 border-b-2 font-medium text-sm transition-colors ${
              activeTab === 'details'
                ? 'border-blue-600 text-blue-600'
                : 'border-transparent text-gray-500 hover:text-gray-700'
            }`}
          >
            Details
          </button>
          <button
            onClick={() => setActiveTab('comments')}
            className={`pb-3 px-1 border-b-2 font-medium text-sm transition-colors ${
              activeTab === 'comments'
                ? 'border-blue-600 text-blue-600'
                : 'border-transparent text-gray-500 hover:text-gray-700'
            }`}
          >
            Comments ({comments.length})
          </button>
          <button
            onClick={() => setActiveTab('history')}
            className={`pb-3 px-1 border-b-2 font-medium text-sm transition-colors ${
              activeTab === 'history'
                ? 'border-blue-600 text-blue-600'
                : 'border-transparent text-gray-500 hover:text-gray-700'
            }`}
          >
            History ({history.length})
          </button>
        </div>
      </div>

      {/* Tab Content */}
      {activeTab === 'details' && (
        <div className="card">
          <h3 className="text-lg font-semibold mb-4">Description</h3>
          <p className="text-gray-700 whitespace-pre-wrap mb-6">{issue.description}</p>

          {/* Attachments */}
          {issue.attachments && issue.attachments.length > 0 && (
            <div className="mt-6 pt-6 border-t">
              <h3 className="text-lg font-semibold mb-4">
                Attachments ({issue.attachments.length})
              </h3>
              <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
                {issue.attachments.map((url, index) => (
                  <div
                    key={index}
                    className="relative group cursor-pointer rounded-lg overflow-hidden border border-gray-200 hover:border-blue-500 transition-colors"
                    onClick={() => openImageModal(url)}
                  >
                    <img
                      src={url}
                      alt={`Attachment ${index + 1}`}
                      className="w-full h-32 object-cover"
                    />
                    <div className="absolute inset-0 bg-black bg-opacity-0 group-hover:bg-opacity-30 transition-all flex items-center justify-center">
                      <svg
                        className="w-8 h-8 text-white opacity-0 group-hover:opacity-100 transition-opacity"
                        fill="none"
                        stroke="currentColor"
                        viewBox="0 0 24 24"
                      >
                        <path
                          strokeLinecap="round"
                          strokeLinejoin="round"
                          strokeWidth={2}
                          d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0zM10 7v3m0 0v3m0-3h3m-3 0H7"
                        />
                      </svg>
                    </div>
                  </div>
                ))}
              </div>
            </div>
          )}
        </div>
      )}

      {activeTab === 'comments' && (
        <div className="space-y-4">
          {/* Add Comment Form */}
          <div className="card">
            <h3 className="text-lg font-semibold mb-4">Add Comment</h3>
            <form onSubmit={handleAddComment} className="space-y-4">
              <textarea
                value={newComment}
                onChange={(e) => setNewComment(e.target.value)}
                rows="4"
                className="input"
                placeholder="Write your comment here..."
                disabled={submittingComment}
              ></textarea>
              <button
                type="submit"
                disabled={submittingComment || !newComment.trim()}
                className="btn btn-primary"
              >
                {submittingComment ? (
                  <span className="flex items-center">
                    <LoadingSpinner size="sm" />
                    <span className="ml-2">Posting...</span>
                  </span>
                ) : (
                  'Post Comment'
                )}
              </button>
            </form>
          </div>

          {/* Comments List */}
          {comments.length === 0 ? (
            <div className="card text-center py-8">
              <p className="text-gray-500">No comments yet. Be the first to comment!</p>
            </div>
          ) : (
            <div className="space-y-4">
              {comments.map((comment) => (
                <div key={comment.id} className="card">
                  <div className="flex items-start gap-4">
                    <div className="flex-shrink-0">
                      <div className="w-10 h-10 rounded-full bg-blue-600 flex items-center justify-center text-white font-semibold">
                        {comment.commentedBy?.name?.charAt(0).toUpperCase()}
                      </div>
                    </div>
                    <div className="flex-1">
                      <div className="flex items-center gap-2 mb-1">
                        <span className="font-semibold text-gray-900">
                          {comment.commentedBy?.name}
                        </span>
                        <span className="text-sm text-gray-500">
                          {formatRelativeTime(comment.createdAt)}
                        </span>
                      </div>
                      <p className="text-gray-700 whitespace-pre-wrap">{comment.message}</p>
                    </div>
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>
      )}

      {activeTab === 'history' && (
        <div className="card">
          {history.length === 0 ? (
            <div className="text-center py-8">
              <p className="text-gray-500">No history available</p>
            </div>
          ) : (
            <div className="space-y-4">
              {history.map((entry, index) => (
                <div key={entry.id || index} className="flex gap-4">
                  <div className="flex flex-col items-center">
                    <div className="w-8 h-8 rounded-full bg-blue-100 flex items-center justify-center">
                      <svg
                        className="w-4 h-4 text-blue-600"
                        fill="none"
                        stroke="currentColor"
                        viewBox="0 0 24 24"
                      >
                        <path
                          strokeLinecap="round"
                          strokeLinejoin="round"
                          strokeWidth={2}
                          d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z"
                        />
                      </svg>
                    </div>
                    {index < history.length - 1 && (
                      <div className="w-0.5 h-full bg-gray-200 mt-2"></div>
                    )}
                  </div>

                  <div className="flex-1 pb-6">
                    <div className="flex items-center gap-2 mb-1">
                      <span className="font-medium text-gray-900">
                        {entry.changedByName}
                      </span>
                      <span className="text-sm text-gray-500">
                        {formatRelativeTime(entry.timestamp)}
                      </span>
                    </div>

                    <p className="text-gray-700 mb-1">
                      <span className="font-medium">{entry.action.replace(/_/g, ' ')}</span>
                      {entry.oldValue && entry.newValue && (
                        <>
                          {' from '}
                          <span className="badge badge-secondary">{entry.oldValue}</span>
                          {' to '}
                          <span className="badge badge-primary">{entry.newValue}</span>
                        </>
                      )}
                    </p>

                    {entry.comment && (
                      <p className="text-sm text-gray-600 mt-2 pl-4 border-l-2 border-gray-300">
                        {entry.comment}
                      </p>
                    )}
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>
      )}

      {/* Image Modal */}
      <Modal
        isOpen={showImageModal}
        onClose={() => setShowImageModal(false)}
        title="Image Preview"
        size="lg"
      >
        {selectedImage && (
          <img
            src={selectedImage}
            alt="Preview"
            className="w-full h-auto rounded-lg"
          />
        )}
      </Modal>
    </div>
  );
};

export default IssueDetails;
