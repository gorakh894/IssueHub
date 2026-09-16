import { PRIORITY_CONFIG } from '../utils/constants';

const PriorityBadge = ({ priority }) => {
  const config = PRIORITY_CONFIG[priority];
  
  if (!config) {
    return <span className="badge badge-secondary">Unknown</span>;
  }

  return (
    <span className={`badge ${config.badgeColor}`}>
      {config.icon} {config.label}
    </span>
  );
};

export default PriorityBadge;
