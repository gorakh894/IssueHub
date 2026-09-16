import { useAuth } from '../../context/AuthContext';

const Profile = () => {
  const { user } = useAuth();
  
  return (
    <div className="space-y-6">
      <h1 className="text-3xl font-bold text-gray-900">Profile</h1>
      
      <div className="card">
        <h2 className="text-xl font-semibold mb-4">User Information</h2>
        <div className="space-y-3">
          <div>
            <span className="font-medium">Name:</span> {user?.name}
          </div>
          <div>
            <span className="font-medium">Email:</span> {user?.email}
          </div>
          <div>
            <span className="font-medium">Role:</span> {user?.role}
          </div>
          <div>
            <span className="font-medium">Department:</span> {user?.department || 'N/A'}
          </div>
          <div>
            <span className="font-medium">Employee ID:</span> {user?.employeeId || 'N/A'}
          </div>
        </div>
      </div>
    </div>
  );
};

export default Profile;
