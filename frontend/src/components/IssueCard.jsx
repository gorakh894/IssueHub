import { Link } from 'react-router-dom';
import StatusBadge from './StatusBadge';
import PriorityBadge from './PriorityBadge';
import { formatRelativeTime, getSLAStatus, truncateText } from '../utils/helpers';

const IssueCard = ({ issue, showReporter = false, showAssignee = false }) => {
  const slaStatus = getSLAStatus(issue.slaDeadline);

  return (
    <Link to={`/employee/issues/${issue.id}`}>
      <div className="card hover:shadow-lg transition-shadow cursor-pointer">
        <div className="flex justify-between items-start mb-3">
          <div className="flex-1">
            <div className="flex items-center gap-2 mb-1">
              <span className="text-xs text-gray-500 font-mono">
                {issue.issueId}
              </span>
              <PriorityBadge priority={issue.priority} />
            </div>
            <h3 className="text-lg font-semibold text-gray-900 mb-2">
              {issue.title}
            </h3>
          </div>
          <StatusBadge status={issue.status} />
        </div>

        <p className="text-gray-600 text-sm mb-4">
          {truncateText(issue.description, 150)}
        </p>

        <div className="space-y-2 text-sm">
          {/* Category */}
          {issue.category && (
            <div className="flex items-center gap-2">
              <span className="text-gray-500">Category:</span>
              <span className="badge badge-secondary">{issue.category.name}</span>
            </div>
          )}

          {/* Location */}
          {issue.location && (
            <div className="flex items-center gap-2">
              <span className="text-gray-500">Location:</span>
              <span className="text-gray-700">
                {issue.location.building}, Floor {issue.location.floor}, {issue.location.room}
              </span>
            </div>
          )}

          {/* Reporter */}
          {showReporter && issue.reportedBy && (
            <div className="flex items-center gap-2">
              <span className="text-gray-500">Reported by:</span>
              <span className="text-gray-700">{issue.reportedBy.name}</span>
            </div>
          )}

          {/* Assignee */}
          {showAssignee && issue.assignedTo && (
            <div className="flex items-center gap-2">
              <span className="text-gray-500">Assigned to:</span>
              <span className="text-gray-700">{issue.assignedTo.name}</span>
            </div>
          )}

          {/* SLA Status */}
          {issue.slaDeadline && (
            <div className="flex items-center gap-2">
              <span className="text-gray-500">SLA:</span>
              <span
                className={`font-medium ${
                  slaStatus.isBreached ? 'text-red-600' : 'text-green-600'
                }`}
              >
                {slaStatus.text}
              </span>
            </div>
          )}

          {/* Timestamps */}
          <div className="flex items-center justify-between pt-2 border-t border-gray-200">
            <span className="text-gray-500">
              Created {formatRelativeTime(issue.createdAt)}
            </span>
            {issue.updatedAt && issue.updatedAt !== issue.createdAt && (
              <span className="text-gray-500">
                Updated {formatRelativeTime(issue.updatedAt)}
              </span>
            )}
          </div>
        </div>

        {/* Attachments indicator */}
        {issue.attachments && issue.attachments.length > 0 && (
          <div className="mt-3 flex items-center gap-1 text-sm text-gray-500">
            <svg
              className="w-4 h-4"
              fill="none"
              stroke="currentColor"
              viewBox="0 0 24 24"
            >
              <path
                strokeLinecap="round"
                strokeLinejoin="round"
                strokeWidth={2}
                d="M15.172 7l-6.586 6.586a2 2 0 102.828 2.828l6.414-6.586a4 4 0 00-5.656-5.656l-6.415 6.585a6 6 0 108.486 8.486L20.5 13"
              />
            </svg>
            <span>{issue.attachments.length} attachment(s)</span>
          </div>
        )}
      </div>
    </Link>
  );
};

export default IssueCard;
