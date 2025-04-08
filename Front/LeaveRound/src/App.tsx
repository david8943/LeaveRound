import { MenuBar } from '@/components/common/MenuBar';
import { Route, Routes, Outlet } from 'react-router-dom';
import { AccountDonate } from './pages/AccountDonatePage.tsx';
import { AccountDetail } from './pages/AccountDetailPage.tsx';
import DonationKingPage from './pages/Event/DonationKingPage.tsx';
import DonatePage from './pages/Event/DonatePage.tsx';
import Onboarding from '@/pages/OnboardingPage.tsx';
import Login from '@/pages/LoginPage.tsx';
import Signup from '@/pages/SignupPage.tsx';
import Organization from '@/pages/OrganizationPage.tsx';
import TitleLayout from '@/components/layout/TitleLayout.tsx';
import DandelionPage from './pages/Event/DandelionPage.tsx';
import MainPage from './pages/MainPage.tsx';
import ManageAccountsPage from './pages/ManageAccountsPage.tsx';
import { useAuthStore } from './stores/useAuthStore.ts';
import { useEffect } from 'react';
import PublicRouter from './router/PublicRouter.tsx';
import ProtectedRoute from './router/ProtectedRoute.tsx';

function App() {
  const isLoggedIn = useAuthStore((state) => state.isLoggedIn);
  const setIsLoggedIn = useAuthStore((state) => state.setIsLoggedIn);

  useEffect(() => {
    const isInSession = sessionStorage.getItem('isLoggedIn') === 'true';
    setIsLoggedIn(isInSession);
  }, [setIsLoggedIn]);

  return (
    <div className='min-h-screen bg-background'>
      <div className='flex flex-col min-h-screen'>
        <main>
          <Routes>
            <Route element={<PublicRouter />}>
              <Route path='/' element={<Onboarding />} />
              <Route path='/login' element={<Login />} />
              <Route path='/signup' element={<Signup />} />
            </Route>
            <Route element={<ProtectedRoute />}>
              <Route path='/main' element={<MainPage />} />
              <Route path='/manage' element={<ManageAccountsPage />} />
              <Route path='/organization' element={<Organization />} />
              <Route path='/donate' element={<AccountDonate />} />
              <Route path='/donate/:autoDonationId' element={<AccountDetail />} />
              <Route path='/event'>
                <Route index element={<DandelionPage />} />
                <Route
                  element={
                    <TitleLayout title='hihi'>
                      <Outlet />
                    </TitleLayout>
                  }
                >
                  <Route path='donationking' element={<DonationKingPage />} />
                  <Route path='donate' element={<DonatePage />} />
                </Route>
              </Route>
            </Route>
          </Routes>
        </main>
        {isLoggedIn && <MenuBar />}
      </div>
    </div>
  );
}

export default App;
