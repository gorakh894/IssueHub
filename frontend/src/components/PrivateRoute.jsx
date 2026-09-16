import { Navigate, Outlet } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import Layout from './Layout';

const PrivateRoute = ({ role }) => {
  const { user, loading } = useAuth();

  if (loading) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-primary-600"></div>
      </div>
    );
  }

  if (!user) {
    return <Navigate to="/login" replace />;
  }

  // Check if user has required role
  if (role && user.role !== role && user.role !== 'ADMIN') {
    const roleRoutes = {
      EMPLOYEE: '/employee/dashboard',
      MANAGER: '/manager/dashboard',
      TECHNICIAN: '/technician/dashboard',
      ADMIN: '/admin/dashboard'
    };
    return <Navigate to={roleRoutes[user.role]} replace />;
  }

  return (
    <Layout>
      <Outlet />
    </Layout>
  );
};

export default PrivateRoute;
