# IssueHub Frontend

React + Vite frontend for IssueHub - Issue Tracking & Resolution Platform

## 🚀 Quick Start

### Prerequisites
- Node.js 16+ installed
- Backend server running on `http://localhost:8080`

### Installation

```bash
# Navigate to frontend directory
cd frontend

# Install dependencies
npm install

# Create environment file
copy .env.example .env

# Start development server
npm run dev
```

Application will be available at `http://localhost:5173`

## 📦 Tech Stack

- **React 18** - UI library
- **Vite** - Build tool
- **React Router** - Routing
- **Axios** - HTTP client
- **Tailwind CSS** - Styling
- **React Hook Form** - Form management
- **React Hot Toast** - Notifications
- **Lucide React** - Icons
- **Recharts** - Charts and analytics
- **date-fns** - Date formatting

## 🏗️ Project Structure

```
frontend/
├── public/              # Static assets
├── src/
│   ├── components/      # Reusable components
│   │   ├── Layout.jsx
│   │   ├── Sidebar.jsx
│   │   ├── Navbar.jsx
│   │   ├── PrivateRoute.jsx
│   │   └── ...
│   ├── context/         # React context
│   │   └── AuthContext.jsx
│   ├── pages/           # Page components
│   │   ├── auth/        # Login, Register
│   │   ├── employee/    # Employee pages
│   │   ├── manager/     # Manager pages
│   │   ├── technician/  # Technician pages
│   │   └── common/      # Shared pages
│   ├── services/        # API services
│   │   ├── api.js
│   │   ├── issueService.js
│   │   └── ...
│   ├── utils/           # Utility functions
│   ├── App.jsx          # Main app component
│   ├── main.jsx         # Entry point
│   └── index.css        # Global styles
├── index.html
├── package.json
├── vite.config.js
├── tailwind.config.js
└── README.md
```

## 🎨 Features

### Authentication
- ✅ Login
- ✅ Register
- ✅ JWT token management
- ✅ Protected routes
- ✅ Role-based access control

### Employee Features
- ✅ Dashboard with personal statistics
- ✅ Create new issues
- ✅ View my issues
- ✅ Track issue status
- ✅ Add comments
- ✅ Upload attachments
- ✅ View issue history

### Manager Features
- ✅ Comprehensive dashboard with analytics
- ✅ View all issues
- ✅ Assign issues to technicians
- ✅ Change priority
- ✅ Manage categories
- ✅ View users and technicians
- ✅ Analytics and reports
- ✅ Charts (category, status, priority, trend)

### Technician Features
- ✅ Dashboard with workload metrics
- ✅ View assigned issues
- ✅ Update issue status
- ✅ Add progress updates
- ✅ Mark issues as resolved
- ✅ Performance metrics

### Common Features
- ✅ Profile management
- ✅ Upload profile image
- ✅ Notifications
- ✅ Real-time notification count
- ✅ Mark notifications as read
- ✅ Responsive design (mobile, tablet, desktop)

## 🔧 Development

### Available Scripts

```bash
# Start development server
npm run dev

# Build for production
npm run build

# Preview production build
npm run preview

# Lint code
npm run lint
```

### Environment Variables

Create `.env` file:

```env
VITE_API_BASE_URL=http://localhost:8080/api
VITE_APP_NAME=IssueHub
VITE_APP_VERSION=1.0.0
```

## 🎨 Styling

This project uses **Tailwind CSS** for styling. Custom utility classes are defined in `src/index.css`:

- `.btn` - Button base class
- `.btn-primary` - Primary button
- `.btn-secondary` - Secondary button
- `.card` - Card component
- `.badge` - Badge component
- And more...

## 📱 Responsive Design

The application is fully responsive and works on:
- 📱 Mobile (320px+)
- 📱 Tablet (768px+)
- 💻 Desktop (1024px+)
- 🖥️ Large Desktop (1280px+)

## 🔐 Authentication Flow

1. User enters credentials
2. Frontend sends request to `/api/auth/login`
3. Backend validates and returns JWT token
4. Token stored in localStorage
5. Token included in all subsequent API requests
6. User navigated to role-specific dashboard

## 🚀 Build for Production

```bash
# Build
npm run build

# Output directory: dist/
# Deploy dist/ folder to your hosting service
```

## 📦 Deployment

### Deploy to Vercel

```bash
# Install Vercel CLI
npm install -g vercel

# Deploy
vercel
```

### Deploy to Netlify

```bash
# Install Netlify CLI
npm install -g netlify-cli

# Build
npm run build

# Deploy
netlify deploy --prod --dir=dist
```

### Deploy to Static Hosting

1. Build the project: `npm run build`
2. Upload `dist/` folder to your hosting service
3. Configure environment variables on hosting platform

## 🐛 Troubleshooting

### CORS Issues

If you encounter CORS errors:
1. Ensure backend CORS is configured correctly
2. Check `CORS_ALLOWED_ORIGINS` in backend `.env`
3. Make sure frontend URL is included

### API Connection Issues

1. Verify backend is running on `http://localhost:8080`
2. Check `VITE_API_BASE_URL` in frontend `.env`
3. Ensure vite proxy is configured in `vite.config.js`

### Token Expiration

- Default token expiration: 24 hours
- User will be automatically logged out on token expiration
- Re-login required after expiration

## 📖 Documentation

- [React Documentation](https://react.dev/)
- [Vite Documentation](https://vitejs.dev/)
- [Tailwind CSS Documentation](https://tailwindcss.com/)
- [React Router Documentation](https://reactrouter.com/)

## 🤝 Contributing

1. Create a feature branch
2. Make your changes
3. Test thoroughly
4. Submit a pull request

## 📄 License

Copyright © 2026 IssueHub. All rights reserved.

---

**Built with ❤️ using React + Vite + Tailwind CSS**
