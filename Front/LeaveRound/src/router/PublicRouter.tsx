import { isAuthenticated } from '@/utils/auth';
import { Navigate, Outlet } from 'react-router-dom';

const PublicRouter = () => {
  return isAuthenticated() ? <Navigate to='/main' replace /> : <Outlet />;
};

export default PublicRouter;
