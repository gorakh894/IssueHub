import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { Toaster } from 'react-hot-toast';
import { AuthProvider } from './context/AuthContext';
import PrivateRoute from './components/PrivateRoute';

// Auth Pages
import Login from './pages/auth/Login';
import Register from './pages/auth/Register';

// Employee Pages
import EmployeeDashboard from './pages/employee/Dashboard';
import CreateIssue from './pages/employee/CreateIssue';
import MyIssues from './pages/employee/MyIssues';
import IssueDetails from './pages/employee/IssueDetails';

// Manager Pages
import ManagerDashboard from './pages/manager/Dashboard';
import AllIssues from './pages/manager/AllIssues';
import Users from './pages/manager/Users';
import Technicians from './pages/manager/Technicians';
import Categories from './pages/manager/Categories';
import Reports from './pages/manager/Reports';

// Technician Pages
import TechnicianDashboard from './pages/technician/Dashboard';
import AssignedIssues from './pages/technician/AssignedIssues';

// Common Pages
import Profile from './pages/common/Profile';
import Notifications from './pages/common/Notifications';
import NotFound from './pages/NotFound';

function App() {
  return (
    <Router>
      <AuthProvider>
        <Toaster position="top-right" />
        <Routes>
          {/* Public Routes */}
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Register />} />

          {/* Employee Routes */}
          <Route path="/employee" element={<PrivateRoute role="EMPLOYEE" />}>
            <Route path="dashboard" element={<EmployeeDashboard />} />
            <Route path="create-issue" element={<CreateIssue />} />
            <Route path="my-issues" element={<MyIssues />} />
            <Route path="issues/:id" element={<IssueDetails />} />
          </Route>

          {/* Manager Routes */}
          <Route path="/manager" element={<PrivateRoute role="MANAGER" />}>
            <Route path="dashboard" element={<ManagerDashboard />} />
            <Route path="issues" element={<AllIssues />} />
            <Route path="issues/:id" element={<IssueDetails />} />
            <Route path="users" element={<Users />} />
            <Route path="technicians" element={<Technicians />} />
            <Route path="categories" element={<Categories />} />
            <Route path="reports" element={<Reports />} />
          </Route>

          {/* Technician Routes */}
          <Route path="/technician" element={<PrivateRoute role="TECHNICIAN" />}>
            <Route path="dashboard" element={<TechnicianDashboard />} />
            <Route path="issues" element={<AssignedIssues />} />
            <Route path="issues/:id" element={<IssueDetails />} />
          </Route>

          {/* Admin Routes (shares Manager routes) */}
          <Route path="/admin" element={<PrivateRoute role="ADMIN" />}>
            <Route path="dashboard" element={<ManagerDashboard />} />
            <Route path="issues" element={<AllIssues />} />
            <Route path="issues/:id" element={<IssueDetails />} />
            <Route path="users" element={<Users />} />
            <Route path="technicians" element={<Technicians />} />
            <Route path="categories" element={<Categories />} />
            <Route path="reports" element={<Reports />} />
          </Route>

          {/* Common Routes */}
          <Route path="/profile" element={<PrivateRoute />}>
            <Route index element={<Profile />} />
          </Route>
          <Route path="/notifications" element={<PrivateRoute />}>
            <Route index element={<Notifications />} />
          </Route>

          {/* Default Routes */}
          <Route path="/" element={<Navigate to="/login" replace />} />
          <Route path="*" element={<NotFound />} />
        </Routes>
      </AuthProvider>
    </Router>
  );
}

export default App;
