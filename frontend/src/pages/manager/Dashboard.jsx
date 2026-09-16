const ManagerDashboard = () => {
  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center">
        <h1 className="text-3xl font-bold text-gray-900">Manager Dashboard</h1>
      </div>
      
      <div className="card">
        <h2 className="text-xl font-semibold mb-4">Welcome Manager!</h2>
        <p className="text-gray-600">
          This is the Manager Dashboard with analytics and reports.
        </p>
        <p className="text-sm text-gray-500 mt-4">
          ✅ Authentication working! You're logged in as a Manager/Admin.
        </p>
      </div>
    </div>
  );
};

export default ManagerDashboard;
