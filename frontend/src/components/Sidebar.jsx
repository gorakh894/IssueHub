import { NavLink } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import {
  LayoutDashboard,
  FileText,
  PlusCircle,
  Users,
  Settings,
  FolderKanban,
  BarChart3,
  Bell,
  User,
} from 'lucide-react';

const Sidebar = ({ isOpen }) => {
  const { user } = useAuth();

  const getMenuItems = () => {
    const baseItems = [
      {
        icon: Bell,
        label: 'Notifications',
        path: '/notifications',
        roles: ['EMPLOYEE', 'MANAGER', 'TECHNICIAN', 'ADMIN'],
      },
      {
        icon: User,
        label: 'Profile',
        path: '/profile',
        roles: ['EMPLOYEE', 'MANAGER', 'TECHNICIAN', 'ADMIN'],
      },
    ];

    const roleMenus = {
      EMPLOYEE: [
        { icon: LayoutDashboard, label: 'Dashboard', path: '/employee/dashboard' },
        { icon: PlusCircle, label: 'Create Issue', path: '/employee/create-issue' },
        { icon: FileText, label: 'My Issues', path: '/employee/my-issues' },
      ],
      MANAGER: [
        { icon: LayoutDashboard, label: 'Dashboard', path: '/manager/dashboard' },
        { icon: FileText, label: 'All Issues', path: '/manager/issues' },
        { icon: Users, label: 'Users', path: '/manager/users' },
        { icon: Users, label: 'Technicians', path: '/manager/technicians' },
        { icon: FolderKanban, label: 'Categories', path: '/manager/categories' },
        { icon: BarChart3, label: 'Reports', path: '/manager/reports' },
      ],
      TECHNICIAN: [
        { icon: LayoutDashboard, label: 'Dashboard', path: '/technician/dashboard' },
        { icon: FileText, label: 'Assigned Issues', path: '/technician/issues' },
      ],
      ADMIN: [
        { icon: LayoutDashboard, label: 'Dashboard', path: '/admin/dashboard' },
        { icon: FileText, label: 'All Issues', path: '/admin/issues' },
        { icon: Users, label: 'Users', path: '/admin/users' },
        { icon: Users, label: 'Technicians', path: '/admin/technicians' },
        { icon: FolderKanban, label: 'Categories', path: '/admin/categories' },
        { icon: BarChart3, label: 'Reports', path: '/admin/reports' },
      ],
    };

    return [...(roleMenus[user?.role] || []), ...baseItems];
  };

  const menuItems = getMenuItems();

  return (
    <aside
      className={`fixed left-0 top-16 h-[calc(100vh-4rem)] bg-white border-r border-gray-200 transition-all duration-300 z-10 ${
        isOpen ? 'w-64' : 'w-0'
      } overflow-hidden`}
    >
      <nav className="p-4 space-y-1">
        {menuItems.map((item, index) => (
          <NavLink
            key={index}
            to={item.path}
            className={({ isActive }) =>
              `flex items-center space-x-3 px-4 py-3 rounded-lg transition-colors ${
                isActive
                  ? 'bg-primary-50 text-primary-700 font-medium'
                  : 'text-gray-700 hover:bg-gray-50'
              }`
            }
          >
            <item.icon className="w-5 h-5" />
            <span>{item.label}</span>
          </NavLink>
        ))}
      </nav>
    </aside>
  );
};

export default Sidebar;
