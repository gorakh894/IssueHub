import { STATUS_CONFIG } from '../utils/constants';

const StatusBadge = ({ status }) => {
  const config = STATUS_CONFIG[status];
  
  if (!config) {
    return <span className="badge badge-secondary">Unknown</span>;
  }

  return (
    <span className={`badge ${config.badgeColor}`}>
      {config.label}
    </span>
  );
};

export default StatusBadge;
